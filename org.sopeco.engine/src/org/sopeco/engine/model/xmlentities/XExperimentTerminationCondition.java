package org.sopeco.engine.model.xmlentities;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ExperimentTerminationCondition", propOrder = {
    "condition"
})
public class XExperimentTerminationCondition {
	protected List<XExtensibleElement> condition;
	
	  public List<XExtensibleElement> getConditions() {
	        if (condition == null) {
	        	condition = new ArrayList<XExtensibleElement>();
	        }
	        return this.condition;
	    }
}
