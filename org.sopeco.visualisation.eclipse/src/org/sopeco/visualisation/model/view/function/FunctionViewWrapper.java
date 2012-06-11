package org.sopeco.visualisation.model.view.function;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.sopeco.persistence.dataset.DataSetAggregated;
import org.sopeco.persistence.dataset.DataSetObservationColumn;
import org.sopeco.persistence.dataset.SimpleDataSet;
import org.sopeco.persistence.dataset.SimpleDataSetRowBuilder;
import org.sopeco.persistence.entities.ExperimentSeriesRun;
import org.sopeco.persistence.entities.definition.ParameterDefinition;
import org.sopeco.persistence.entities.definition.ParameterNamespace;
import org.sopeco.persistence.entities.definition.ParameterRole;
import org.sopeco.util.Tools;
import org.sopeco.visualisation.model.ErrorStatus;
import org.sopeco.visualisation.model.ErrorType;
import org.sopeco.visualisation.model.FunctionData;
import org.sopeco.visualisation.model.ViewConfigurationOptions;
import org.sopeco.visualisation.model.IFunctionViewModel;
import org.sopeco.visualisation.model.ViewItemConfiguration;
import org.sopeco.visualisation.model.view.AbstractViewWrapper;
import org.sopeco.visualisation.model.view.DataItem;

public class FunctionViewWrapper extends AbstractViewWrapper implements IFunctionViewModel {

	private FunctionViewModel model;
	private ParameterDefinition dummyXParameter;

	public FunctionViewWrapper() {
		model = new FunctionViewModel();
		createDummyXParameter();
	}

	
	@Override
	public void addDataItem(ViewItemConfiguration configuration, ErrorStatus errorStatus) {
		ExperimentSeriesRun experimentSeriesRun = configuration.getExperimentSeriesRun();
		ParameterDefinition xPar = configuration.getxParameter();
		ParameterDefinition yPar = configuration.getyParameter();
		Map<ParameterDefinition, Object> valueAssignments = configuration.getValueAssignments();
		
		resetErrorStatus(errorStatus);
		if(xPar == null){
			xPar = dummyXParameter;
		}
		if (!validateParameters(xPar, yPar, valueAssignments == null ? new ArrayList<ParameterDefinition>() : valueAssignments.keySet(), errorStatus)) {
			return;
		}
		
		if(configuration.getComparisonParameter() == null){
			DataItem item = new DataItem();
			item.setData(experimentSeriesRun);
			item.setxParameter(xPar);
			item.setyParameter(yPar);
			if (valueAssignments != null && !valueAssignments.isEmpty()) {
				item.setValueAssignments(valueAssignments);
			}
			model.addToDataSelection(item);
		}else{
			for(Object value : experimentSeriesRun.getSuccessfulResultDataSet().getInputColumn(configuration.getComparisonParameter()).getValueSet()){
				DataItem item = new DataItem();
				item.setData(experimentSeriesRun);
				item.setxParameter(xPar);
				item.setyParameter(yPar);
				item.setLabel(value.toString());
				Map<ParameterDefinition, Object> extendedValueAssignments = new HashMap<ParameterDefinition, Object>();
				extendedValueAssignments.putAll(valueAssignments);
				extendedValueAssignments.put(configuration.getComparisonParameter(), value);
				if (extendedValueAssignments != null && !extendedValueAssignments.isEmpty()) {
					item.setValueAssignments(extendedValueAssignments);
				}
				model.addToDataSelection(item);
			}
		}
		
		
	}
	
	@Override
	public List<FunctionData> getFunctionsToVisualize() {
		List<FunctionData> functions = new ArrayList<FunctionData>();

		int i = 0;
		for (DataItem item : model.getDataSelection()) {
			DataSetAggregated dataset = item.getData().getSuccessfulResultDataSet();

			SimpleDataSet simpleDataSet = null;
			if (item.getValueAssignments() != null && !item.getValueAssignments().isEmpty()) {
				dataset = dataset.select(item.getValueAssignments());
			}
			if (item.getxParameter().equals(dummyXParameter)) {
				DataSetObservationColumn obsColumn = dataset.getObservationColumn(item.getyParameter());
				simpleDataSet = createDatasetWithArtificialXParameter(obsColumn.getAllValues(), obsColumn.getParameter());
			} else {
				List<ParameterDefinition> xyParameterPair = new ArrayList<ParameterDefinition>();
				xyParameterPair.add(item.getxParameter());
				xyParameterPair.add(item.getyParameter());

				simpleDataSet = dataset.getSubSet(xyParameterPair).convertToSimpleDataSet();
			}

			FunctionData data = new FunctionData();
			data.setId(i);
			data.setData(simpleDataSet);
			Date date = new Date(item.getData().getTimestamp());
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd - hh:mm:ss");
			data.setLabel(item.getLabel() +" - "+ dateFormat.format(date));
			data.setXParameter(item.getxParameter());
			data.setYParameter(item.getyParameter());
			functions.add(data);
			i++;
		}
		return functions;
	}

	private boolean validateParameters(ParameterDefinition xPar, ParameterDefinition yPar, Collection<ParameterDefinition> assignedParameters,
			ErrorStatus errorStatus) {
		if (!isNumericParameter(xPar) || !isNumericParameter(yPar)) {
			if (errorStatus != null) {
				errorStatus.setErrorType(ErrorType.InvalidParameter);
			}
			return false;
		}
		for (ParameterDefinition parameter : assignedParameters) {
			if (!parameter.getRole().equals(ParameterRole.INPUT)) {
				errorStatus.setErrorType(ErrorType.InvalidParameter);
				return false;
			}
		}
		return true;
	}

	

	private SimpleDataSet createDatasetWithArtificialXParameter(List<Object> observationValues, ParameterDefinition observationParameter) {
		SimpleDataSetRowBuilder builder = new SimpleDataSetRowBuilder();
		int i = 1;
		for (Object value : observationValues) {
			builder.startRow();
			builder.addParameterValue(dummyXParameter, i);
			builder.addParameterValue(observationParameter, value);
			builder.finishRow();
			i++;
		}
		return builder.createDataSet();
	}

	private void createDummyXParameter() {
		dummyXParameter = new ParameterDefinition();
		dummyXParameter.setName("Repetition");
		ParameterNamespace artificialNameSpace = new ParameterNamespace();
		artificialNameSpace.setName("artificial");
		dummyXParameter.setNamespace(artificialNameSpace);
		dummyXParameter.setRole(ParameterRole.INPUT);
		dummyXParameter.setType(Tools.SupportedTypes.Integer.toString());
	}



	

}
