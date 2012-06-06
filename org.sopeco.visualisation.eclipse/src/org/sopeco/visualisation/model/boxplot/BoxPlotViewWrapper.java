package org.sopeco.visualisation.model.boxplot;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.sopeco.persistence.dataset.DataSetAggregated;
import org.sopeco.persistence.entities.ExperimentSeriesRun;
import org.sopeco.persistence.entities.definition.ParameterDefinition;
import org.sopeco.visualisation.model.BoxPlotData;
import org.sopeco.visualisation.model.ErrorStatus;
import org.sopeco.visualisation.model.ErrorType;
import org.sopeco.visualisation.model.IBoxPlotViewModel;
import org.sopeco.visualisation.model.ViewItemConfiguration;
import org.sopeco.visualisation.model.view.AbstractViewWrapper;
import org.sopeco.visualisation.model.view.DataItem;

public class BoxPlotViewWrapper extends AbstractViewWrapper implements IBoxPlotViewModel {
	private BoxPlotViewModel model;

	public BoxPlotViewWrapper() {
		model = new BoxPlotViewModel();
	}
	
	
	@Override
	public void addDataItem(ViewItemConfiguration configuration, ErrorStatus errorStatus) {
		ExperimentSeriesRun experimentSeriesRun = configuration.getExperimentSeriesRun();
		ParameterDefinition xPar = configuration.getxParameter();
		ParameterDefinition yPar = configuration.getyParameter();
		Map<ParameterDefinition, Object> valueAssignments = configuration.getValueAssignments();
		
		DataItem item = new DataItem();
		item.setData(experimentSeriesRun);
		item.setxParameter(xPar);
		item.setyParameter(yPar);
		if (valueAssignments != null && !valueAssignments.isEmpty()) {
			item.setValueAssignments(valueAssignments);
		}
		model.addToDataSelection(item);
	}



	@Override
	public List<BoxPlotData> getBoxesToVisualize() {
		List<BoxPlotData> boxes = new ArrayList<BoxPlotData>();

		int i = 0;
		for (DataItem item : model.getDataSelection()) {
			DataSetAggregated dataset = item.getData().getSuccessfulResultDataSet();
			
			if (item.getValueAssignments() != null && !item.getValueAssignments().isEmpty()) {
				dataset = dataset.select(item.getValueAssignments());
			}
			
			if (item.xParameterUsed()) {
				for (Object xValue : dataset.getInputColumn(item.getxParameter()).getValueSet()) {
					Map<ParameterDefinition, Object> xValueAllocation = new HashMap<ParameterDefinition, Object>();
					xValueAllocation.put(item.getxParameter(), xValue);
					List<Object> values = dataset.select(xValueAllocation).getObservationColumn(item.getyParameter()).getAllValues();
					boxes.add(createBoxPlotData(i, xValue.toString(), values, item.getyParameter()));
					i++;
				}

			} else {
				List<Object> values = dataset.getObservationColumn(item.getyParameter()).getAllValues();
				Date date = new Date(item.getData().getTimestamp());
				DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd - hh:mm:ss");
				boxes.add(createBoxPlotData(i, dateFormat.format(date), values, item.getyParameter()));
				i++;
			}

		}
		return boxes;
	}

	private BoxPlotData createBoxPlotData(int id, String label, List<Object> values, ParameterDefinition observationParameter) {
		BoxPlotData data = new BoxPlotData();
		data.setId(id);
		data.setData(values);
		data.setLabel(label);
		data.setObservationParameter(observationParameter);
		return data;
	}

}
