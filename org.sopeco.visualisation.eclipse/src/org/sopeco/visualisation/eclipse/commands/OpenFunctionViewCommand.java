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
import org.sopeco.visualisation.model.FunctionViewConfiguration;
import org.sopeco.visualisation.model.IFunctionViewModel;
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
			FunctionViewConfiguration alternatives = model.getConfigurationAlternatives(nodeToShow, errorStatus);

			if (errorStatus.getErrorType().equals(ErrorType.NoInputParameter)) {
				boolean showWithoutInputParameter = Message.question("No input parameter found!",
						"No input Parameter found! Show the observation data in dependence on the repetition number?");
				if (!showWithoutInputParameter) {
					return;
				} else {

					ParameterDefinition ypDef = ParameterSelection.selectParameter("Select output parameter (for the y-axis)!",
							alternatives.getOutputParameters());
					model.addDataItem(nodeToShow, ypDef, errorStatus);

				}
			} else {
				ParameterDefinition xpDef = ParameterSelection.selectParameter("Select input parameter (for the x-axis)!",
						alternatives.getNumericInputParameters());
				ParameterDefinition ypDef = ParameterSelection.selectParameter("Select output parameter (for the y-axis)!", alternatives.getOutputParameters());

				Map<ParameterDefinition, Collection<Object>> assignments = new HashMap<ParameterDefinition, Collection<Object>>();
				assignments.putAll(alternatives.getInputParameterAssignmentOptions());
				assignments.remove(xpDef);
				ValueAssignmentsDialog dialog = new ValueAssignmentsDialog(PersistenceNavigation.getInstance().getSite().getShell(), "test", "test",
						assignments);

				if (dialog.open()) {
					model.addDataItem(nodeToShow, xpDef, ypDef, dialog.getResult(), errorStatus);
				} else {
					model.addDataItem(nodeToShow, xpDef, ypDef, errorStatus);
				}

			}

			FunctionEditorInput input = new FunctionEditorInput(model.getFunctionsToVisualize());
			IWorkbenchPage page = VisualisationPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage();
			page.openEditor(input, FunctionViewEditor.getEditorId());
		}

	}

}
