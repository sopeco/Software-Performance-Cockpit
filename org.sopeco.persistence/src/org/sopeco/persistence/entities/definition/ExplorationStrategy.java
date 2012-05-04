package org.sopeco.persistence.entities.definition;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Dennis Westermann
 *
 */
public class ExplorationStrategy extends ExtensibleElement {

	private static final long serialVersionUID = 1L;

	protected List<AnalysisConfiguration> analysisConfigurations;

	public ExplorationStrategy() {
		super();
	}

	public List<AnalysisConfiguration> getAnalysisConfigurations() {
		if (analysisConfigurations == null) {
			analysisConfigurations = new ArrayList<AnalysisConfiguration>();
		}
		return analysisConfigurations;
	}

} 