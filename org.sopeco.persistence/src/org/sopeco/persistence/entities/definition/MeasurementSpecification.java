package org.sopeco.persistence.entities.definition;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Dennis Westermann
 * 
 */
public class MeasurementSpecification implements Serializable {

	private static final long serialVersionUID = 1L;

	private String name;

	protected List<ExperimentSeriesDefinition> experimentSeriesDefinitions;

	protected List<ConstantValueAssignment> initializationAssignemts;

	public MeasurementSpecification() {
		super();
	}

	public List<ExperimentSeriesDefinition> getExperimentSeriesDefinitions() {
		if (experimentSeriesDefinitions == null) {
			experimentSeriesDefinitions = new LinkedList<ExperimentSeriesDefinition>();
		}
		return experimentSeriesDefinitions;
	}

	public List<ConstantValueAssignment> getInitializationAssignemts() {
		if (initializationAssignemts == null) {
			initializationAssignemts = new LinkedList<ConstantValueAssignment>();
		}
		return initializationAssignemts;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Checks whether this instance of measurement specification comprises the
	 * given instance. "Comprises" means that for each experiment series of the
	 * other measurement specification an equivalent in this mesaurement
	 * specification exists
	 * 
	 * @param other
	 *            - measurement specification instance to check against
	 * @return returns true if this instance contains the other
	 */
	public boolean containsAllElementsOf(MeasurementSpecification other) {
		for (ExperimentSeriesDefinition esdOther : other.getExperimentSeriesDefinitions()) {
			boolean found = false;
			long matchingVersion = 0;
			for(ExperimentSeriesDefinition esdThis : this.getExperimentSeriesDefinitions()){
				if(esdThis.equals(esdOther)){
					found = true;
					matchingVersion = esdThis.getVersion();
					break;
				}
			}
			
			if(found){
				esdOther.setVersion(matchingVersion);
			}else{
				return false;
			}
		}
		
		for (ConstantValueAssignment cva : other.getInitializationAssignemts()) {
			if (!this.getInitializationAssignemts().contains(cva)) {
				return false;
			}
		}
		
		return true;
	}

	/**
	 * Extends this instance by additional experiment series of the other
	 * measurement specification instance.
	 * 
	 * @param other
	 *            - the measurement specification instance containing additional
	 *            experiment series
	 * 
	 * @return a list of added experiment series definitions
	 * 
	 */
	public List<ExperimentSeriesDefinition> extendBy(MeasurementSpecification other) {
		List<ExperimentSeriesDefinition> addedESDList = new ArrayList<ExperimentSeriesDefinition>();
		for (ExperimentSeriesDefinition esdOther : other.getExperimentSeriesDefinitions()) {
			boolean foundEqualName = false;
			boolean foundEqualSeries = false;
			long maxVersion = 0;
			for (ExperimentSeriesDefinition esdThis : this.getExperimentSeriesDefinitions()) {
				if (esdOther.getName().equals(esdThis.getName())) {
					// expSeries with same name found
					foundEqualName = true;
					if (esdThis.getVersion() > maxVersion) {
						maxVersion = esdThis.getVersion();
					}
					if (esdOther.equals(esdThis)) {
						foundEqualSeries = true;
					}
				}
			}

			if (!foundEqualSeries) {
				if (foundEqualName) {
					// increase version number
					esdOther.setVersion(maxVersion + 1);
				}
				this.experimentSeriesDefinitions.add(esdOther);
				addedESDList.add(esdOther);
			}
		}

		for (ConstantValueAssignment cvaOther : other.getInitializationAssignemts()) {
			if (!this.initializationAssignemts.contains(cvaOther)) {
				this.initializationAssignemts.add(cvaOther);
			}
		}

		return addedESDList;

	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((experimentSeriesDefinitions == null || experimentSeriesDefinitions.isEmpty()) ? 0 : experimentSeriesDefinitions.hashCode());
		result = prime * result + ((initializationAssignemts == null || initializationAssignemts.isEmpty()) ? 0 : initializationAssignemts.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MeasurementSpecification other = (MeasurementSpecification) obj;
		if (experimentSeriesDefinitions == null || experimentSeriesDefinitions.isEmpty()) {
			if (other.experimentSeriesDefinitions != null && !other.experimentSeriesDefinitions.isEmpty())
				return false;
		} else if (!experimentSeriesDefinitions.equals(other.experimentSeriesDefinitions))
			return false;
		if (initializationAssignemts == null || initializationAssignemts.isEmpty()) {
			if (other.initializationAssignemts != null && !other.initializationAssignemts.isEmpty())
				return false;
		} else if (!initializationAssignemts.equals(other.initializationAssignemts))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

}