package org.sopeco.visualisation.model;

import org.sopeco.visualisation.model.boxplot.BoxPlotViewWrapper;
import org.sopeco.visualisation.model.view.function.CorrelationViewWrapper;
import org.sopeco.visualisation.model.view.function.FunctionViewWrapper;

public class ViewModelFactory {
	private static ViewModelFactory instance;
	public static ViewModelFactory getInstance(){
		if(instance == null){
			instance = new ViewModelFactory();
		}
		
		return instance;
	}
	
	private ViewModelFactory() {
	}
	
	public IFunctionViewModel createFunctionViewModel(){
		return new FunctionViewWrapper();
	}
	
	public IBoxPlotViewModel createBoxPlotViewModel(){
		return new BoxPlotViewWrapper();
	}
	
	public IFunctionViewModel createCorrelationViewModel(){
		return new CorrelationViewWrapper();
	}
}
