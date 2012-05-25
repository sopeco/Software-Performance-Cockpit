package org.sopeco.visualisation.model.boxplot;

import org.sopeco.persistence.entities.ExperimentSeriesRun;
import org.sopeco.persistence.entities.definition.ParameterDefinition;

public class BoxPlotDataItem {
	private ExperimentSeriesRun data;
	private ParameterDefinition yParameter;
	private ParameterDefinition xParameter;
	
	public ExperimentSeriesRun getData() {
		return data;
	}
	public void setData(ExperimentSeriesRun data) {
		this.data = data;
	}
	public ParameterDefinition getyParameter() {
		return yParameter;
	}
	public void setyParameter(ParameterDefinition yParameter) {
		this.yParameter = yParameter;
	}
	public ParameterDefinition getxParameter() {
		return xParameter;
	}
	public void setxParameter(ParameterDefinition xParameter) {
		this.xParameter = xParameter;
	}
	
	public boolean xParameterUsed(){
		return xParameter != null;
	}
	
}
