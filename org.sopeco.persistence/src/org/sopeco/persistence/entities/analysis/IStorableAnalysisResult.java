package org.sopeco.persistence.entities.analysis;

import java.io.Serializable;


/**
 * Interface is introduced to avoid dependency cycles between engine and persistence component.
 * 
 * @author Dennis Westermann
 *
 */
public interface IStorableAnalysisResult extends Serializable {
	
	String getId();
	String setId(String id);

}
