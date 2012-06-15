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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((analysisConfigurations == null) ? 0 : analysisConfigurations.hashCode());
		result = prime * result + ((configuration == null) ? 0 : configuration.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;

		ExplorationStrategy other = (ExplorationStrategy) obj;
		if (analysisConfigurations == null) {
			if (other.analysisConfigurations != null)
				return false;
		} else if (!analysisConfigurations.equals(other.analysisConfigurations))
			return false;
		if (configuration == null) {
			if (other.configuration != null)
				return false;
		} else if (!configuration.equals(other.configuration))
			return false;
		return true;
	}

} 