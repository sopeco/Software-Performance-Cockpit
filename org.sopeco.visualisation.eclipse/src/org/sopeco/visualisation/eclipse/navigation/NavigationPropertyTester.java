package org.sopeco.visualisation.eclipse.navigation;

import java.util.List;

import org.eclipse.core.expressions.PropertyTester;
import org.sopeco.persistence.entities.ExperimentSeriesRun;

public class NavigationPropertyTester extends PropertyTester{

	@Override
	public boolean test(Object receiver, String property, Object[] args,
			Object expectedValue) {
		List<Object> selectedNodes =  PersistenceNavigation.getInstance().getSelectedNodes();
		if ("selectedOne".equals(property)) {
			return selectedNodes.size() == 1;
		} else if("runSelected".equals(property)){
			for(Object obj : selectedNodes){
				if(obj instanceof ExperimentSeriesRun){
					return true;
				}
			}
			return false;
		} else if("homogenousSelection".equals(property)){
			if(selectedNodes.isEmpty()){
				return false;
			}
			Object firstObject = selectedNodes.get(0);
			for(Object obj : selectedNodes){
				if(!obj.getClass().equals(firstObject.getClass())){
					return false;
				}
			}
			return true;
		}
		return false;
	}

}
