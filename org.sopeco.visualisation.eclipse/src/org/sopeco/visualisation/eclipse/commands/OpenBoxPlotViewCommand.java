package org.sopeco.visualisation.eclipse.commands;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.IWorkbenchPage;
import org.sopeco.persistence.entities.ExperimentSeriesRun;
import org.sopeco.persistence.entities.definition.ParameterDefinition;
import org.sopeco.visualisation.eclipse.VisualisationPlugin;
import org.sopeco.visualisation.eclipse.chart.boxplot.BoxPlotEditorInput;
import org.sopeco.visualisation.eclipse.chart.boxplot.BoxPlotViewEditor;
import org.sopeco.visualisation.eclipse.dialogs.Message;
import org.sopeco.visualisation.eclipse.dialogs.ParameterSelection;
import org.sopeco.visualisation.eclipse.dialogs.ValueAssignmentsDialog;
import org.sopeco.visualisation.eclipse.navigation.PersistenceNavigation;
import org.sopeco.visualisation.model.ErrorStatus;
import org.sopeco.visualisation.model.ErrorType;
import org.sopeco.visualisation.model.IBoxPlotViewModel;
import org.sopeco.visualisation.model.ViewConfigurationOptions;
import org.sopeco.visualisation.model.ViewItemConfiguration;
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
			ViewConfigurationOptions alternatives = model.getConfigurationAlternatives(nodeToShow, errorStatus);
			ViewItemConfiguration configuration = new ViewItemConfiguration();
			configuration.setExperimentSeriesRun(nodeToShow);
			if (errorStatus.getErrorType().equals(ErrorType.EmptyDataset)) {
				Message.show("Empty Dataset", "Unable to open selected node! Corresponding dataset is empty or does not exist!");
				return;
			}
			ParameterDefinition ypDef = ParameterSelection.selectParameter("Select output parameter!", alternatives.getOutputParameters());
			configuration.setyParameter(ypDef);
			ParameterDefinition xpDef = null;
			if (alternatives.getInputParameters() != null && !alternatives.getInputParameters().isEmpty()) {
				xpDef = ParameterSelection.selectParameterWithIgnoreOption("Select input parameter!", alternatives.getInputParameters());
			}
			configuration.setxParameter(xpDef);

			Map<ParameterDefinition, Collection<Object>> assignments = null;
			if (xpDef == null) {
				assignments = alternatives.getInputParameterAssignmentOptions();
			} else {
				assignments = new HashMap<ParameterDefinition, Collection<Object>>();
				assignments.putAll(alternatives.getInputParameterAssignmentOptions());
				assignments.remove(xpDef);

			}
			if(!assignments.isEmpty()){
				ValueAssignmentsDialog dialog = new ValueAssignmentsDialog(PersistenceNavigation.getInstance().getSite().getShell(), "Value Assignments", "Specify value assignments", assignments);
				if (dialog.open()) {
					configuration.setValueAssignments(dialog.getValueAssignments());

				}
			}
			
			model.addDataItem(configuration, errorStatus);

			BoxPlotEditorInput input = new BoxPlotEditorInput(model.getBoxesToVisualize());
			IWorkbenchPage page = VisualisationPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage();
			page.openEditor(input, BoxPlotViewEditor.getEditorId());
		}
	}

}
