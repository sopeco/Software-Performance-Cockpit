package org.sopeco.plugin.std.exploration.breakdown.space;

import org.sopeco.plugin.std.exploration.breakdown.environment.AbstractEnvironmentValue;
import org.sopeco.plugin.std.exploration.breakdown.environment.EnvironmentCachedAccess.MeasurementCacheResult;

public class SectorRandomPointPredictionResult {
	RelativePosition point;
	MeasurementCacheResult measuredValue;
	AbstractEnvironmentValue predictedValue;
	Double predictionError;
	
	
	
	
	public SectorRandomPointPredictionResult(
			RelativePosition point, MeasurementCacheResult measuredValue,
			AbstractEnvironmentValue predictedValue, Double predictionError) {
		super();
		
		this.point = point;
		this.measuredValue = measuredValue;
		this.predictedValue = predictedValue;
		this.predictionError = predictionError;

	}
	
	

	public RelativePosition getPoint() {
		return point;
	}
	public void setPoint(RelativePosition point) {
		this.point = point;
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
	
	
	
	
	
}
