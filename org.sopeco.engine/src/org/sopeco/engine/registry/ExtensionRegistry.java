/**
 * Copyright (c) 2013 SAP
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the SAP nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL SAP BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
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
import org.sopeco.util.system.SystemTools;

/**
 * The extension registry of SoPeCo. This class acts as a wrapper around Eclipse
 * OSGi extensions repository.
 * 
 * @author Roozbeh Farahbod
 * 
 */
public final class ExtensionRegistry implements IExtensionRegistry {

	private static Logger logger = LoggerFactory.getLogger(ExtensionRegistry.class);

	private static final String DEFAULT_PLUGINS_FOLDER_IN_CLASSPATH = "plugins";

	/**
	 * Holds the list of extension point identifiers supported by the engine.
	 */
	public static final String[] EXTENSION_POINTS = { "org.sopeco.engine.experimentseries.explorationstrategy",
			"org.sopeco.engine.experimentseries.parametervariation", "org.sopeco.engine.analysis.analysisstrategy",
			"org.sopeco.engine.experimentseries.constantvalueassignment", "org.sopeco.engine.processing" };

	private static final String EXTENSIONS_FILE_NAME = "extensions.info";

	private static IExtensionRegistry singleton = null;

	/** Holds a mapping of extension names to extensions. */
	private Map<String, ISoPeCoExtension<?>> extensions = new HashMap<String, ISoPeCoExtension<?>>();

	@SuppressWarnings("rawtypes")
	private Map<Class, Extensions> extensionsMap = new HashMap<Class, Extensions>();

	private boolean initialized = false;

	/**
	 * Returns a singleton instance of the extension registry.
	 * 
	 * @return Returns a singleton instance of the extension registry.
	 */
	public synchronized static IExtensionRegistry getSingleton() {
		if (singleton == null) {
			singleton = new ExtensionRegistry();
		}
		return singleton;
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
		} else {
			logger.warn("Plugin registry cannot be re-initialized.");
		}

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
		final IConfiguration config = Configuration.getSessionUnrelatedSingleton(this.getClass());
		String pluginsDirNames = config.getPropertyAsStr(IConfiguration.CONF_PLUGINS_DIRECTORIES);

		Set<String> pluginsDirsSet = new HashSet<String>();
		pluginsDirsSet.add(DEFAULT_PLUGINS_FOLDER_IN_CLASSPATH);
		if (pluginsDirNames != null) {
			String[] pluginsDirs = Tools.tokenize(pluginsDirNames, IConfiguration.DIR_SEPARATOR);

			for (String dir : pluginsDirs) {
				pluginsDirsSet.add(dir);
			}
		}

		Set<URL> jarURLs = new HashSet<URL>();
		Set<URL> extensionsInfoURLs = new HashSet<URL>();

		// 1. Load all plugins information from all sources

		// 1.1 Gather list of JAR files in plugins folders and gathers
		// extensions.info files in those directories
		for (String dir : pluginsDirsSet) {
			loadExtensionsInfoAndJARs(dir, extensionsInfoURLs, jarURLs);
		}

		ClassLoader classLoader = new URLClassLoader(jarURLs.toArray(new URL[] {}), this.getClass().getClassLoader());

		// unpack all extensions.info's separately and gathers them in the list

		try {
			String tempPluginsDir = SystemTools.extractFilesFromClasspath("plugins", "sopecoPlugins", "plugins files", classLoader);

			String[] infoFiles = Tools.getFileNames(tempPluginsDir, "*.info");
			for (String infoFileName : infoFiles) {
				final String fullName = Tools.concatFileName(tempPluginsDir, infoFileName);
				URL url = new URL("file", "", fullName);
				extensionsInfoURLs.add(url);
			}

		} catch (Exception e) {
			logger.error("Could not unpack plugins information in the classpath. Reason:", e);
		}

		// 2. Look for all 'extensions.info' files in the classpath
		// and the default location

		final String extensionFilePath = DEFAULT_PLUGINS_FOLDER_IN_CLASSPATH + '/' + EXTENSIONS_FILE_NAME;
		Enumeration<URL> eURLs;
		try {
			eURLs = classLoader.getResources(extensionFilePath);
			while (eURLs.hasMoreElements()) {
				extensionsInfoURLs.add(eURLs.nextElement());
			}

		} catch (IOException e1) {
			throw new RuntimeException(e1);
		}

		for (URL url : extensionsInfoURLs) {
			logger.debug("Found extensions info at: {}", url);
		}
		if (extensionsInfoURLs.size() == 0) {
			logger.warn("Found no extensions information.");
		}

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
			if (extClassName.trim().isEmpty()) {
				continue;
			}

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
				logger.warn("Could not load extension {}. Reason: ({}) {}", new Object[] { extClassName,
						e.getClass().getSimpleName(), e.getMessage() });
			}
		}
	}

	/**
	 * Loads JAR files and plugin extension information from the given relative
	 * plugins directory.
	 * 
	 * @param pluginsDirName
	 *            relative path to the plugins folder
	 * @param extensionsInfoURLs
	 *            the resulting set of URLs to extension infos
	 * @param jarURLs
	 *            the resulting aggregated JAR URLs
	 */
	private void loadExtensionsInfoAndJARs(String pluginsDirName, Set<URL> extensionsInfoURLs, Set<URL> jarURLs) {
		IConfiguration config = Configuration.getSessionUnrelatedSingleton(this.getClass());

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
			for (URL url : jarURLs) {
				logger.debug("Found extension JAR file {}", url);
			}
		} else {
			logger.debug("Could not locate the plugins folder ({}), but it is OK.", pluginsDirName);
		}

		// 2. add the 'extensions.info' of the plugins directory if it exists

		if (defExtensionsFile.exists()) {
			try {
				extensionsInfoURLs.add(new URL("file", "", defExtensionsFileName));
			} catch (MalformedURLException e1) {
				logger.warn("Could not read '{}'. Reason: (MalformedURLException) {}", defExtensionsFile,
						e1.getMessage());
			}
		}

	}

	@Override
	public <EA extends ISoPeCoExtensionArtifact> EA getExtensionArtifact(Class<? extends ISoPeCoExtension<EA>> c,
			String name) {
		Extensions<? extends ISoPeCoExtension<EA>> extensions = getExtensions(c);

		for (ISoPeCoExtension<EA> ext : extensions) {
			if (Tools.strEqualName(ext.getName(), name)) {
				return ext.createExtensionArtifact();
			}
		}
		logger.warn("Could not find extension {} for extension category {}.", name, c.getSimpleName());

		return null;
	}

}
