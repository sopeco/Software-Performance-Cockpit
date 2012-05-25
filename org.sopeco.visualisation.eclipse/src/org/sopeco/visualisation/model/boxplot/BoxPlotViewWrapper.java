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
import org.sopeco.visualisation.model.BoxPlotViewConfiguration;
import org.sopeco.visualisation.model.ErrorStatus;
import org.sopeco.visualisation.model.ErrorType;
import org.sopeco.visualisation.model.IBoxPlotViewModel;
import org.sopeco.visualisation.model.view.AbstractViewWrapper;

public class BoxPlotViewWrapper extends AbstractViewWrapper implements IBoxPlotViewModel {
	private BoxPlotViewModel model;

	public BoxPlotViewWrapper() {
		model = new BoxPlotViewModel();
	}

	@Override
	public BoxPlotViewConfiguration getConfigurationAlternatives(ExperimentSeriesRun experimentSeriesRun, ErrorStatus errorStatus) {
		if (errorStatus != null) {
			errorStatus.setErrorType(ErrorType.None);
		}
		BoxPlotViewConfiguration configAlternatives = new BoxPlotViewConfiguration();

		configAlternatives.setOutputParameters(getNumericObservationParameters(experimentSeriesRun));
		if (configAlternatives.getOutputParameters().size() < 1) {
			if (errorStatus != null) {
				errorStatus.setErrorType(ErrorType.NoObservationParameter);
			}

		}
		configAlternatives.setInputParameters(getVariedInputParameters(experimentSeriesRun));

		return configAlternatives;
	}

	@Override
	public void addDataItem(ExperimentSeriesRun experimentSeriesRun, ParameterDefinition yPar, ErrorStatus errorStatus) {
		addDataItem(experimentSeriesRun, null, yPar, errorStatus);
	}

	@Override
	public void addDataItem(ExperimentSeriesRun experimentSeriesRun, ParameterDefinition xPar, ParameterDefinition yPar, ErrorStatus errorStatus) {
		if (errorStatus != null) {
			errorStatus.setErrorType(ErrorType.None);
		}
		if (xPar != null && !getVariedInputParameters(experimentSeriesRun).contains(xPar)) {
			if (errorStatus != null) {
				errorStatus.setErrorType(ErrorType.InvalidParameter);
			}
			return;
		}
		BoxPlotDataItem item = new BoxPlotDataItem();
		item.setData(experimentSeriesRun);
		item.setxParameter(xPar);
		item.setyParameter(yPar);
		model.addToDataSelection(item);

	}

	@Override
	public List<BoxPlotData> getBoxesToVisualize() {
		List<BoxPlotData> boxes = new ArrayList<BoxPlotData>();

		int i = 0;
		for (BoxPlotDataItem item : model.getDataSelection()) {
			DataSetAggregated dataset = item.getData().getSuccessfulResultDataSet();
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
