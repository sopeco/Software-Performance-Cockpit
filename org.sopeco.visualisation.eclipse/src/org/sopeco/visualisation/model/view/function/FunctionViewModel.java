package org.sopeco.visualisation.model.view.function;

import java.util.ArrayList;
import java.util.List;

import org.sopeco.visualisation.model.view.AbstractView;
import org.sopeco.visualisation.model.view.DataItem;

public class FunctionViewModel extends AbstractView {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7065440902932752327L;
	
	private List<DataItem> dataSelection;
	private FunctionViewChartStyle style;
	
	
	public FunctionViewModel() {
		dataSelection = new ArrayList<DataItem>();
	}
	
	public void setDataSelection(List<DataItem> dataSelection){
		this.dataSelection = dataSelection;
	}
	
	public void addToDataSelection(DataItem selectedNode){
		this.dataSelection.add(selectedNode);
	}
	
	public List<DataItem> getDataSelection(){
		return dataSelection;
	}

	public FunctionViewChartStyle getStyle() {
		return style;
	}

	public void setStyle(FunctionViewChartStyle style) {
		this.style = style;
	}
	
	
	
	

}
