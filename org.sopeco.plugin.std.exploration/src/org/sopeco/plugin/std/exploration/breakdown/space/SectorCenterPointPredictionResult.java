package org.sopeco.plugin.std.exploration.breakdown.space;

import java.util.List;
import java.util.Map;

import org.sopeco.plugin.std.exploration.breakdown.environment.AbstractEnvironmentValue;
import org.sopeco.plugin.std.exploration.breakdown.environment.EnvironmentCachedAccess.MeasurementCacheResult;

public class SectorCenterPointPredictionResult {
	List<RelativePosition> allEdges;
	RelativePosition centerPoint;
	MeasurementCacheResult measuredValue;
	AbstractEnvironmentValue predictedValue;
	Double predictionError;
	Map<String,Double> parametersToIgnore;
	
	
	
	public SectorCenterPointPredictionResult(List<RelativePosition> allEdges,
			RelativePosition centerPoint, MeasurementCacheResult measuredValue,
			AbstractEnvironmentValue predictedValue, Double predictionError,
			Map<String, Double> parametersToIgnore) {
		super();
		this.allEdges = allEdges;
		this.centerPoint = centerPoint;
		this.measuredValue = measuredValue;
		this.predictedValue = predictedValue;
		this.predictionError = predictionError;
		this.parametersToIgnore = parametersToIgnore;
	}
	
	
	public List<RelativePosition> getAllEdges() {
		return allEdges;
	}
	public void setAllEdges(List<RelativePosition> allEdges) {
		this.allEdges = allEdges;
	}
	public RelativePosition getCenterPoint() {
		return centerPoint;
	}
	public void setCenterPoint(RelativePosition centerPoint) {
		this.centerPoint = centerPoint;
	}
	public MeasurementCacheResult getMeasuredValue() {
		return measuredValue;
	}
	public void setMeasuredValue(MeasurementCacheResult measuredValue) {
		this.measuredValue = measuredValue;
	}
	public AbstractEnvironmentValue getPredictedValue() {
		return predictedValue;
	}
	public void setPredictedValue(AbstractEnvironmentValue predictedValue) {
		this.predictedValue = predictedValue;
	}
	public Double getPredictionError() {
		return predictionError;
	}
	public void setPredictionError(Double predictionError) {
		this.predictionError = predictionError;
	}
	public Map<String, Double> getParametersToIgnore() {
		return parametersToIgnore;
	}
	public void setParametersToIgnore(Map<String, Double> parametersToIgnore) {
		this.parametersToIgnore = parametersToIgnore;
	}
	
	
	
	
}
