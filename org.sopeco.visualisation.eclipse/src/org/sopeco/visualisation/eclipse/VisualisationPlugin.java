package org.sopeco.visualisation.eclipse;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

public class VisualisationPlugin extends AbstractUIPlugin {

	// Plug-in icons
	public static final String GROUP_NODE_ICON = "group.gif";
	public static final String DATA_STORE_ICON = "datasource.gif";
	public static final String DATA_SET_ICON = "dataset.gif";
	public static final String CHART_ICON = "chart_curve.png";
	public static final String TABLE_ICON = "table.png";
	private static final String PATH = "icons/";

	// The plug-in ID
	public static final String PLUGIN_ID = "org.sopeco.visualisation.eclipse"; //$NON-NLS-1$
	// The shared instance
	private static VisualisationPlugin plugin;

	/**
	 * Constructor
	 */
	public VisualisationPlugin() {
		
	}

	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;

		getImageRegistry().put(GROUP_NODE_ICON, getImageDescriptor(PATH + GROUP_NODE_ICON));
		getImageRegistry().put(DATA_STORE_ICON, getImageDescriptor(PATH + DATA_STORE_ICON));
		getImageRegistry().put(DATA_SET_ICON, getImageDescriptor(PATH + DATA_SET_ICON));
		getImageRegistry().put(CHART_ICON, getImageDescriptor(PATH + CHART_ICON));
		getImageRegistry().put(TABLE_ICON, getImageDescriptor(PATH + TABLE_ICON));

	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static VisualisationPlugin getDefault() {
		return plugin;
	}
	
	/**
	 * Returns an image descriptor for the image file at the given plug-in
	 * relative path
	 * 
	 * @param path
	 *            the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}
}
