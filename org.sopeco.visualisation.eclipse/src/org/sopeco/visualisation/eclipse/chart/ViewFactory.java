package org.sopeco.visualisation.eclipse.chart;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.ui.IElementFactory;
import org.eclipse.ui.IMemento;

public class ViewFactory implements IElementFactory {

	public static String FACTORY_ID = "org.sopeco.visualisation.eclipse.ViewFactory";
	
	@Override
	public IAdaptable createElement(IMemento memento) {
	
		return null;
	}

}
