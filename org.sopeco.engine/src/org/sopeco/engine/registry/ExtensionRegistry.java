/**
 * 
 */
package org.sopeco.engine.registry;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sopeco.config.Configuration;
import org.sopeco.config.IConfiguration;
import org.sopeco.util.Tools;

/**
 * The extension registry of SoPeCo. This class acts as a wrapper around Eclipse
 * OSGi extensions repository.
 * 
 * @author Roozbeh Farahbod
 * 
 */
public class ExtensionRegistry implements IExtensionRegistry {

	private static Logger logger = LoggerFactory.getLogger(ExtensionRegistry.class);

	public static final String DEFAULT_PLUGINS_FOLDER_IN_CLASSPATH = "plugins";

	/**
	 * Holds the list of extension point identifiers supported by the engine.
	 */
	public static final String[] EXTENSION_POINTS = { "org.sopeco.engine.experimentseries.explorationstrategy",
			"org.sopeco.engine.experimentseries.parametervariation", "org.sopeco.engine.analysis.analysisstrategy",
			"org.sopeco.engine.experimentseries.constantvalueassignment", "org.sopeco.engine.processing" };

	private static final String EXTENSIONS_FILE_NAME = "extensions.info";

	private static IExtensionRegistry SINGLETON = null;

	/** Holds a mapping of extension names to extensions. */
	private Map<String, ISoPeCoExtension<?>> extensions = new HashMap<String, ISoPeCoExtension<?>>();

	@SuppressWarnings("rawtypes")
	private Map<Class, Extensions> extensionsMap = new HashMap<Class, Extensions>();

	private boolean initialized = false;

	/**
	 * Returns a singleton instance of the extension registry.
	 */
	public static IExtensionRegistry getSingleton() {
		if (SINGLETON == null)
			SINGLETON = new ExtensionRegistry();
		return SINGLETON;
	}

	@Override
	public void addExtension(ISoPeCoExtension<?> ext) {
		extensions.put(ext.getName(), ext);
	}

	@Override
	public void removeExtension(String name) {
		extensions.remove(name);
	}

	@Override
	public Collection<? extends ISoPeCoExtension<?>> getExtensions() {
		return Collections.unmodifiableCollection(extensions.values());
	}

	@Override
	public <E extends ISoPeCoExtension<?>> Extensions<E> getExtensions(Class<E> c) {
		@SuppressWarnings("unchecked")
		Extensions<E> exts = extensionsMap.get(c);
		if (exts == null) {
			exts = new Extensions<E>(c);
			extensionsMap.put(c, exts);
		}
		return exts;
	}

	/*
	 * Preventing public instantiation of the registry.
	 */
	private ExtensionRegistry() {
		initialize();
	}

	/**
	 * Initializes the plugin registry.
	 */
	private void initialize() {
		if (!initialized) {
			loadEngineExtensions();
		} else
			logger.warn("Plugin registry cannot be re-initialized.");

		initialized = true;
	}

	/**
	 * Loads all the extensions that are supported by the engine.
	 */
	private void loadEngineExtensions() {
		logger.info("Loading SoPeCo engine extensions...");
		loadExtensions();
	}


	/**
	 * Loads the extensions with basic class loading.
	 * 
	 * @param registry
	 *            the Eclipse platform extension registry
	 * @param eid
	 *            extension point id
	 */
	@SuppressWarnings("rawtypes")
	private void loadExtensions() {
		final IConfiguration config = Configuration.getSingleton();
		String pluginsDirNames = config.getPropertyAsStr(IConfiguration.CONF_PLUGINS_DIRECTORIES);
		
		Set<String> pluginsDirsSet = new HashSet<String>();
		pluginsDirsSet.add(DEFAULT_PLUGINS_FOLDER_IN_CLASSPATH);
		if (pluginsDirNames != null) {
			String[] pluginsDirs = Tools.tokenize(pluginsDirNames, IConfiguration.DIR_SEPARATOR);
		
			for (String dir: pluginsDirs)
				pluginsDirsSet.add(dir);
		}
		
		Set<URL> jarURLs = new HashSet<URL>();
		Set<URL> extensionsInfoURLs = new HashSet<URL>();

		// 1. Load all plugins information from all sources
		
		for (String dir: pluginsDirsSet) {
			loadExtensionsInfoAndJARs(dir, extensionsInfoURLs, jarURLs);
		}
		
		ClassLoader classLoader = new URLClassLoader(jarURLs.toArray(new URL[] {}), this.getClass().getClassLoader());

		// 2. Look for all 'extensions.info' files in the classpath
		//    and the default location
		
		final String EXTENSION_FILE_PATH = DEFAULT_PLUGINS_FOLDER_IN_CLASSPATH + '/' + EXTENSIONS_FILE_NAME;
		Enumeration<URL> eURLs;
		try {
			eURLs = classLoader.getResources(EXTENSION_FILE_PATH);
			while (eURLs.hasMoreElements()) {
				extensionsInfoURLs.add(eURLs.nextElement());
			}
			
		} catch (IOException e1) {
		}

		for (URL url : extensionsInfoURLs)
			logger.debug("Found extensions info at: {}", url);
		if (extensionsInfoURLs.size() == 0)
			logger.warn("Found no extensions information.");

		// 4. gather the list of all extension class files
		
		Set<String> extensionClasses = new HashSet<String>();
		for (URL url : extensionsInfoURLs) {
			try {
				extensionClasses.addAll(Tools.readLines(url));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		// 5. Load extensions
		
		for (String extClassName : extensionClasses) {
			if (extClassName.trim().isEmpty())
				continue;
			
			Class<?> c;
			try {
				c = classLoader.loadClass(extClassName);
				Object o = c.newInstance();
				if (o instanceof ISoPeCoExtension) {
					ISoPeCoExtension ext = (ISoPeCoExtension) o;
					this.addExtension(ext);
					logger.debug("Loading extension {}.", ext.getName());
				}
			} catch (Exception e) {
				logger.warn("Could not load extension {}. Reason: ({}) {}", new Object[] { extClassName, e.getClass().getSimpleName(), e.getMessage() });
			}
		}
	}

	/**
	 * Loads JAR files and plugin extension information from the given relative plugins directory. 
	 * 
	 * @param pluginsDirName relative path to the plugins folder
	 * @param extensionsInfoURLs the resulting set of URLs to extension infos
	 * @param jarURLs the resulting aggregated JAR URLs 
	 */
	private void loadExtensionsInfoAndJARs(String pluginsDirName, Set<URL> extensionsInfoURLs, Set<URL> jarURLs) {
		IConfiguration config = Configuration.getSingleton();

		if (!Tools.isAbsolutePath(pluginsDirName)) {
			pluginsDirName = Tools.concatFileName(config.getAppRootDirectory(), pluginsDirName);
		}
			
		final File pluginsDir = new File(pluginsDirName);
		final String defExtensionsFileName = pluginsDirName + File.separatorChar + EXTENSIONS_FILE_NAME;
		final File defExtensionsFile = new File(defExtensionsFileName);

		// 1. Locate JAR files from the plugins folder

		if (pluginsDir.exists()) {
			String[] names = Tools.getFileNames(pluginsDirName, "*.jar");

			if (names.length > 0) {
				for (String str : names) {
					final String jarName = Tools.concatFileName(pluginsDirName, str);
					try {
						jarURLs.add(new URL("file", "", jarName));
					} catch (MalformedURLException e) {
						logger.warn("Ignoring JAR file {}", jarName);
					}
				}
			}
			for (URL url : jarURLs)
				logger.debug("Found extension JAR file {}", url);

		} else {
			logger.debug("Could not locate the plugins folder ({}), but it is OK.", pluginsDirName);
		}

		// 2. add the 'extensions.info' of the plugins directory if it exists
		
		if (defExtensionsFile.exists()) {
			try {
				extensionsInfoURLs.add(new URL("file", "", defExtensionsFileName));
			} catch (MalformedURLException e1) {
				logger.warn("Could not read '{}'. Reason: (MalformedURLException) {}", defExtensionsFile, e1.getMessage());
			}
		}

	}
	
	
	@Override
	public <EA extends ISoPeCoExtensionArtifact> EA getExtensionArtifact(Class<? extends ISoPeCoExtension<EA>> c, String name) {
		Extensions<? extends ISoPeCoExtension<EA>> extensions = getExtensions(c);

		for (ISoPeCoExtension<EA> ext : extensions)
			if (Tools.strEqualName(ext.getName(), name))
				return ext.createExtensionArtifact();

		logger.warn("Could not find extension {} for extension category {}.", name, c.getSimpleName());

		return null;
	}

	// ----------------------------

	public static void main(String[] args) {
		IExtensionRegistry er = ExtensionRegistry.getSingleton();
	}

	// ------------- Equinox Tests

	// // ---------------------
	//
	// public static void main(String[] args) {
	// ServiceLoader<FrameworkFactory> ffsl =
	// ServiceLoader.load(FrameworkFactory.class);
	// Iterator<FrameworkFactory> iterator = ffsl.iterator();
	// FrameworkFactory ff = null;
	// if (iterator.hasNext())
	// ff = iterator.next();
	// Map<String, String> config = new HashMap<String, String>();
	//
	// config.put("data", "/roozbeh/temp/plugins");
	//
	// Framework fw = ff.newFramework(config);
	// try {
	// fw.start();
	//
	// System.out.println("Framework: " + fw);
	//
	// BundleContext bc = fw.getBundleContext();
	// ServiceTracker tracker = new ServiceTracker(bc,
	// IExtensionRegistry.class.getName(), null);
	// tracker.open();
	// IExtensionRegistry registry =(IExtensionRegistry) tracker.getService();
	//
	// System.out.println(registry);
	//
	// fw.stop();
	// fw.waitForStop(0);
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }

	// public static void main(String[] args) {
	// ServiceLoader<FrameworkFactory> ffsl =
	// ServiceLoader.load(FrameworkFactory.class);
	// Iterator<FrameworkFactory> iterator = ffsl.iterator();
	// FrameworkFactory ff = null;
	// if (iterator.hasNext())
	// ff = iterator.next();
	// Map<String,String> config = new HashMap<String,String>();
	//
	// config.put("data", "/roozbeh/temp/plugins");
	//
	// Framework fw = ff.newFramework(config);
	// try {
	// fw.start();
	//
	// System.out.println(fw);
	//
	// BundleContext bc = fw.getBundleContext();
	//
	// //bc.installBundle("file:///roozbeh/temp/plugins/org.sopeco.plugin.std.exploration_1.0.0.test.jar");
	// bc.installBundle("file:///roozbeh/temp/plugins/org.sopeco.plugin.std.parametervariation_1.0.0.test.jar");
	//
	// System.out.println(bc.getBundles().length);
	//
	// for (Bundle b: bc.getBundles())
	// System.out.println(b);
	//
	// org.eclipse.core.runtime.IExtensionRegistry ier =
	// RegistryFactory.createRegistry(null, "m", "u");
	//
	// System.out.println(ier);
	//
	// // Equinox e = (Equinox) fw;
	// // for (ServiceReference<?> sf: e.getRegisteredServices())
	// // System.out.println(sf);
	// fw.stop();
	// fw.waitForStop(0);
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	//
	// }

}
