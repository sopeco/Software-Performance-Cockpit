package org.sopeco.visualisation.eclipse.navigation;

import static org.sopeco.visualisation.eclipse.VisualisationPlugin.DATA_SET_ICON;
import static org.sopeco.visualisation.eclipse.VisualisationPlugin.DATA_STORE_ICON;
import static org.sopeco.visualisation.eclipse.VisualisationPlugin.GROUP_NODE_ICON;
import static org.sopeco.visualisation.eclipse.VisualisationPlugin.getDefault;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.sopeco.persistence.entities.ExperimentSeries;
import org.sopeco.persistence.entities.ExperimentSeriesRun;
import org.sopeco.persistence.entities.ScenarioInstance;



public class PersistenceNavigationLabelProvider extends LabelProvider {
	/**
	 * Returns the text label for the passed element
	 */
	@Override
	public String getText(Object element) {
		if (element instanceof ScenarioInstance) {
			return ((ScenarioInstance) element).getName();
		} else if (element instanceof ExperimentSeries) {
			return ((ExperimentSeries) element).getName();
		} else if (element instanceof ExperimentSeriesRun) {
			Date date = new Date(((ExperimentSeriesRun) element).getTimestamp());
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd - hh:mm:ss");
			return dateFormat.format(date);
		}

		return super.getText(element);
	}

	/**
	 * Returns the image for the passed element
	 */
	@Override
	public Image getImage(Object element) {
		getDefault().getImageRegistry();
		if (element instanceof ScenarioInstance) {
			return getDefault().getImageRegistry().get(DATA_STORE_ICON);
		} else if (element instanceof ExperimentSeries) {
			return getDefault().getImageRegistry().get(GROUP_NODE_ICON);
		} else if (element instanceof ExperimentSeriesRun) {
			return getDefault().getImageRegistry().get(DATA_SET_ICON);
		}

		return super.getImage(element);
	}
}
