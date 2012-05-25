package org.sopeco.visualisation.model.view.function;

import java.util.ArrayList;
import java.util.List;

import org.sopeco.visualisation.model.view.AbstractView;

public class FunctionViewModel extends AbstractView {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7065440902932752327L;
	
	private List<FunctionDataItem> dataSelection;
	private FunctionViewChartStyle style;
	
	
	public FunctionViewModel() {
		dataSelection = new ArrayList<FunctionDataItem>();
	}
	
	public void setDataSelection(List<FunctionDataItem> dataSelection){
		this.dataSelection = dataSelection;
	}
	
	public void addToDataSelection(FunctionDataItem selectedNode){
		this.dataSelection.add(selectedNode);
	}
	
	public List<FunctionDataItem> getDataSelection(){
		return dataSelection;
	}

	public FunctionViewChartStyle getStyle() {
		return style;
	}

	public void setStyle(FunctionViewChartStyle style) {
		this.style = style;
	}
	
	
	
	

}
