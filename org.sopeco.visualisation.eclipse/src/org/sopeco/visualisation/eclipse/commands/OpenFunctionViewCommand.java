package org.sopeco.visualisation.eclipse.commands;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.sopeco.persistence.entities.ExperimentSeriesRun;
import org.sopeco.persistence.entities.definition.ParameterDefinition;
import org.sopeco.visualisation.eclipse.VisualisationPlugin;
import org.sopeco.visualisation.eclipse.chart.function.FunctionEditorInput;
import org.sopeco.visualisation.eclipse.chart.function.FunctionViewEditor;
import org.sopeco.visualisation.eclipse.dialogs.Message;
import org.sopeco.visualisation.eclipse.dialogs.ParameterSelection;
import org.sopeco.visualisation.eclipse.dialogs.ValueAssignmentsDialog;
import org.sopeco.visualisation.eclipse.navigation.PersistenceNavigation;
import org.sopeco.visualisation.model.ErrorStatus;
import org.sopeco.visualisation.model.ErrorType;
import org.sopeco.visualisation.model.ViewConfigurationOptions;
import org.sopeco.visualisation.model.IFunctionViewModel;
import org.sopeco.visualisation.model.ViewItemConfiguration;
import org.sopeco.visualisation.model.ViewModelFactory;

public class OpenFunctionViewCommand extends AbstractCommand {

	/**
	 * Opens the ChartView for the selected nodes. If the selection contains
	 * several nodes all nodes are merged to one dataset.
	 * 
	 * @throws PartInitException
	 * @throws FrameworkException
	 */
	@Override
	public void execute(Object data) throws CoreException, IOException {

		PersistenceNavigation navigationTreeView = PersistenceNavigation.getInstance();

		List<Object> selectedNodes = navigationTreeView.getSelectedNodes();

		if (selectedNodes.size() == 1 && selectedNodes.get(0) instanceof ExperimentSeriesRun) {
			ExperimentSeriesRun nodeToShow = (ExperimentSeriesRun) selectedNodes.get(0);
			IFunctionViewModel model = ViewModelFactory.getInstance().createFunctionViewModel();

			ErrorStatus errorStatus = new ErrorStatus();
			ViewConfigurationOptions alternatives = model.getConfigurationAlternatives(nodeToShow, errorStatus);
			ViewItemConfiguration configuration = new ViewItemConfiguration();
			configuration.setExperimentSeriesRun(nodeToShow);
			if (errorStatus.getErrorType().equals(ErrorType.EmptyDataset)) {
				Message.show("Empty Dataset", "Unable to open selected node! Corresponding dataset is empty or does not exist!");
				return;
			}

			if (alternatives.getNumericInputParameters().isEmpty()) {
				boolean showWithoutInputParameter = Message.question("No input parameter found!",
						"No input Parameter found! Show the observation data in dependence on the repetition number?");
				if (!showWithoutInputParameter) {
					return;
				} else {

	
					configuration.setyParameter(ParameterSelection.selectParameter("Select output parameter (for the y-axis)!",
							alternatives.getOutputParameters()));
					if(!alternatives.getInputParameterAssignmentOptions().isEmpty()){
						ValueAssignmentsDialog dialog = new ValueAssignmentsDialog(PersistenceNavigation.getInstance().getSite().getShell(), "Value Assignments", "Specify value assignments",
								alternatives.getInputParameterAssignmentOptions());

						if (dialog.open()) {
							configuration.setValueAssignments(dialog.getValueAssignments());
							configuration.setComparisonParameter(dialog.getComparisonParameter());
						} 
					}
					
					model.addDataItem(configuration, errorStatus);

				}
			} else {
				
				
				
			
				ParameterDefinition xpDef = ParameterSelection.selectParameter("Select input parameter (for the x-axis)!",
						alternatives.getNumericInputParameters());
				configuration.setxParameter(xpDef);
				ParameterDefinition ypDef = ParameterSelection.selectParameter("Select output parameter (for the y-axis)!", alternatives.getOutputParameters());
				configuration.setyParameter(ypDef);
				
				Map<ParameterDefinition, Collection<Object>> assignments = new HashMap<ParameterDefinition, Collection<Object>>();
				assignments.putAll(alternatives.getInputParameterAssignmentOptions());
				assignments.remove(xpDef);
				if(!assignments.isEmpty()){
					ValueAssignmentsDialog dialog = new ValueAssignmentsDialog(PersistenceNavigation.getInstance().getSite().getShell(), "Value Assignments", "Specify value assignments",
							assignments);

					if (dialog.open()) {
						configuration.setValueAssignments(dialog.getValueAssignments());
						configuration.setComparisonParameter(dialog.getComparisonParameter());
					} 
				}
				
				model.addDataItem(configuration, errorStatus);

			}

			FunctionEditorInput input = new FunctionEditorInput(model.getFunctionsToVisualize());
			IWorkbenchPage page = VisualisationPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage();
			page.openEditor(input, FunctionViewEditor.getEditorId());
		}

	}

}
