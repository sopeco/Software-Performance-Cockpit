package org.sopeco.visualisation.model.boxplot;

import java.util.ArrayList;
import java.util.List;

import org.sopeco.visualisation.model.view.AbstractView;
import org.sopeco.visualisation.model.view.DataItem;

public class BoxPlotViewModel extends AbstractView{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5999338372932893070L;
	
	private List<DataItem> dataSelection;

	public BoxPlotViewModel() {
		setDataSelection(new ArrayList<DataItem>());
	}
	
	public List<DataItem> getDataSelection() {
		return dataSelection;
	}

	public void addToDataSelection(DataItem item) {
		this.getDataSelection().add(item);
	}

	public void setDataSelection(List<DataItem> dataSelection) {
		this.dataSelection = dataSelection;
	}

}
