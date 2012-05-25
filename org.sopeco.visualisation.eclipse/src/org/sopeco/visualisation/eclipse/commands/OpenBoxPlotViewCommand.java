package org.sopeco.visualisation.eclipse.commands;

import java.io.IOException;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.IWorkbenchPage;
import org.sopeco.persistence.entities.ExperimentSeriesRun;
import org.sopeco.persistence.entities.definition.ParameterDefinition;
import org.sopeco.visualisation.eclipse.VisualisationPlugin;
import org.sopeco.visualisation.eclipse.chart.boxplot.BoxPlotEditorInput;
import org.sopeco.visualisation.eclipse.chart.boxplot.BoxPlotViewEditor;
import org.sopeco.visualisation.eclipse.dialogs.ParameterSelection;
import org.sopeco.visualisation.eclipse.navigation.PersistenceNavigation;
import org.sopeco.visualisation.model.BoxPlotViewConfiguration;
import org.sopeco.visualisation.model.ErrorStatus;
import org.sopeco.visualisation.model.IBoxPlotViewModel;
import org.sopeco.visualisation.model.ViewModelFactory;

public class OpenBoxPlotViewCommand extends AbstractCommand {

	@Override
	public void execute(Object data) throws CoreException, IOException {
		PersistenceNavigation navigationTreeView = PersistenceNavigation.getInstance();

		List<Object> selectedNodes = navigationTreeView.getSelectedNodes();

		if (selectedNodes.size() == 1 && selectedNodes.get(0) instanceof ExperimentSeriesRun) {
			ExperimentSeriesRun nodeToShow = (ExperimentSeriesRun) selectedNodes.get(0);
			IBoxPlotViewModel model = ViewModelFactory.getInstance().createBoxPlotViewModel();
			ErrorStatus errorStatus = new ErrorStatus();
			BoxPlotViewConfiguration alternatives = model.getConfigurationAlternatives(nodeToShow, errorStatus);

			ParameterDefinition ypDef = ParameterSelection.selectParameter("Select output parameter!", alternatives.getOutputParameters());
			ParameterDefinition xpDef = null;
			if (alternatives.getInputParameters() != null && !alternatives.getInputParameters().isEmpty()) {
				xpDef = ParameterSelection.selectParameterWithIgnoreOption("Select input parameter!", alternatives.getInputParameters());
			}
			model.addDataItem(nodeToShow, xpDef, ypDef, errorStatus);

			BoxPlotEditorInput input = new BoxPlotEditorInput(model.getBoxesToVisualize());
			IWorkbenchPage page = VisualisationPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage();
			page.openEditor(input, BoxPlotViewEditor.getEditorId());
		}
	}

}
