package org.sopeco.visualisation.model.boxplot;

import java.util.ArrayList;
import java.util.List;

import org.sopeco.visualisation.model.view.AbstractView;

public class BoxPlotViewModel extends AbstractView{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5999338372932893070L;
	
	private List<BoxPlotDataItem> dataSelection;

	public BoxPlotViewModel() {
		setDataSelection(new ArrayList<BoxPlotDataItem>());
	}
	
	public List<BoxPlotDataItem> getDataSelection() {
		return dataSelection;
	}

	public void addToDataSelection(BoxPlotDataItem item) {
		this.getDataSelection().add(item);
	}

	public void setDataSelection(List<BoxPlotDataItem> dataSelection) {
		this.dataSelection = dataSelection;
	}

}
