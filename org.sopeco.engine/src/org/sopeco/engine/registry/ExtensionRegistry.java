/**
 * 
 */
package org.sopeco.engine.registry;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
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
		final String pluginsDirName = Tools.concatFileName(config.getAppRootDirectory(), config.getPropertyAsStr(IConfiguration.CONF_PLUGINS_FOLDER));
		final File pluginsDir = new File(pluginsDirName);
		final String defExtensionsFileName = pluginsDirName + File.separatorChar + EXTENSIONS_FILE_NAME;
		final File defExtensionsFile = new File(defExtensionsFileName);

		// 1. Look for all 'extensions.info' files in the classpath
		// and the default location
		final String EXTENSION_FILE_PATH = config.DEFAULT_PLUGINS_FOLDER_IN_CLASSPATH + '/' + EXTENSIONS_FILE_NAME;
		Set<URL> extensioInfoURLs = new HashSet<URL>();
		Enumeration<URL> eURLs;
		try {
			eURLs = ClassLoader.getSystemResources(EXTENSION_FILE_PATH);
			while (eURLs.hasMoreElements()) {
				extensioInfoURLs.add(eURLs.nextElement());
			}
			URL enginePathURL = this.getClass().getClassLoader().getResource(EXTENSION_FILE_PATH);
			if (enginePathURL != null) {
				extensioInfoURLs.add(enginePathURL);
			}
		} catch (IOException e1) {
		}

		// 2. add 'plugins/extensions.info' if it exists
		if (defExtensionsFile.exists()) {
			try {
				extensioInfoURLs.add(new URL("file:" + defExtensionsFileName));
			} catch (MalformedURLException e1) {
				logger.warn("Could not read '{}'. Reason: (MalformedURLException) {}", defExtensionsFile, e1.getMessage());
			}
		}

		for (URL url : extensioInfoURLs)
			logger.debug("Found extensions info at: {}", url);
		if (extensioInfoURLs.size() == 0)
			logger.warn("Found no extensions information.");

		// 2. gather the list of all extension class files
		Set<String> extensionClasses = new HashSet<String>();
		for (URL url : extensioInfoURLs) {
			try {
				extensionClasses.addAll(Tools.readLines(url));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		ClassLoader cl = this.getClass().getClassLoader();

		if (pluginsDir.exists()) {
			String[] names = Tools.getFileNames(pluginsDirName, "*.jar");

			// load jar files
			ArrayList<URL> urls = new ArrayList<URL>();
			if (names.length > 0) {
				for (String str : names) {
					final String jarName = Tools.concatFileName(pluginsDirName, str);
					try {
						urls.add(new URL("file://" + jarName));
					} catch (MalformedURLException e) {
						logger.warn("Ignoring JAR file {}", jarName);
					}
				}
			}
			for (URL url : urls)
				logger.debug("Found extension JAR file {}", url);

			cl = new URLClassLoader(urls.toArray(new URL[] {}), this.getClass().getClassLoader());

		} else {
			logger.debug("Could not locate the plugins folder ({}), but it is OK.", pluginsDirName);
		}

		for (String extClassName : extensionClasses) {
			Class<?> c;
			try {
				c = cl.loadClass(extClassName);
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
