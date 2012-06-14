/**
 * 
 */
package org.sopeco.plugin.std.processing.basic;

import java.util.HashMap;
import java.util.Map;

import org.sopeco.engine.processing.IProcessingStrategy;
import org.sopeco.engine.registry.AbstractSoPeCoExtensionArtifact;
import org.sopeco.engine.registry.ISoPeCoExtension;
import org.sopeco.persistence.EntityFactory;
import org.sopeco.persistence.dataset.DataSetAggregated;
import org.sopeco.persistence.dataset.DataSetRow;
import org.sopeco.persistence.dataset.DataSetRowBuilder;
import org.sopeco.persistence.dataset.ParameterValueList;
import org.sopeco.persistence.entities.definition.ParameterDefinition;
import org.sopeco.persistence.entities.definition.ParameterRole;
import org.sopeco.persistence.util.ParameterCollection;
import org.sopeco.persistence.util.ParameterCollectionFactory;
import org.sopeco.util.Tools;

/**
 * Provides some basic statistical data processing, mainly used as a proof of concept for the Data Processing extension.
 * 
 * @author Roozbeh Farahbod
 *
 */
public class BasicStatisticalProcessingStrategy extends AbstractSoPeCoExtensionArtifact implements IProcessingStrategy {

	private static final String DOUBLE_TYPE_NAME = "Double";
	protected DataSetAggregated sourceDataSet = null;
	protected ParameterCollection<ParameterDefinition> availableParameters = ParameterCollectionFactory.createParameterDefinitionCollection();

	protected Map<ParameterDefinition, ParameterDefinition> meanParams = new HashMap<ParameterDefinition, ParameterDefinition>();
	protected Map<ParameterDefinition, ParameterDefinition> stdDevParams = new HashMap<ParameterDefinition, ParameterDefinition>();

	protected ParameterCollection<ParameterDefinition> outputParameters = null;
	
	public BasicStatisticalProcessingStrategy(ISoPeCoExtension<?> provider) {
		super(provider);
	}

	@Override
	public void setSourceDataSet(DataSetAggregated source) {
		this.sourceDataSet = source;
		updateAvailableParameters();
	}

	/**
	 * Updates available parameters collection.
	 */
	private void updateAvailableParameters() {
		availableParameters.clear();
		meanParams.clear();
		stdDevParams.clear();
		
		if (sourceDataSet.getRow(0) != null) {
			for (ParameterDefinition pd: sourceDataSet.getParameterDefinitions()) {

				availableParameters.add(pd);

				// among observation parameters 
				// if they are numeric and have more than one value in the first 
				// row, add mean and stddev as well
				// do this if the first 
				if (pd.getRole() == ParameterRole.OBSERVATION) {

					if (pd.isNumeric()) {
						final int valueCollectionSize = sourceDataSet.getRow(0).getObservableParameterValues(pd).getSize();
						if (valueCollectionSize > 1) {
							addStatVariations(pd, availableParameters);
						}
					}
				}
			}
		}
	}

	/**
	 * Adds mean and standard deviation parameters to the available parameter list.
	 * @param pd source parameter definition
	 * @param availableParams collection of available parameters
	 */
	private void addStatVariations(ParameterDefinition pd, ParameterCollection<ParameterDefinition> availableParams) {
		ParameterDefinition meanPD = EntityFactory.createParameterDefinition("Mean " + pd.getName(), DOUBLE_TYPE_NAME, pd.getRole());
		ParameterDefinition stdDevPD = EntityFactory.createParameterDefinition("Std. Dev. of " + pd.getName(), DOUBLE_TYPE_NAME, pd.getRole());
		
		availableParams.add(meanPD);
		availableParams.add(stdDevPD);
		
		meanParams.put(meanPD, pd);
		stdDevParams.put(stdDevPD, pd);
		
	}

	@Override
	public DataSetAggregated processData() {
		DataSetRowBuilder builder = new DataSetRowBuilder();
		
		for (DataSetRow row: sourceDataSet.getRowList()) {
			builder.startRow();

			// go over parameter definitions that must be included in the output
			for (ParameterDefinition pd: outputParameters) {
				
				if (pd.getRole() == ParameterRole.INPUT) {
					builder.addInputParameterValue(pd, row.getInputParameterValue(pd));
				}
				
				else 
					if (pd.getRole() == ParameterRole.OBSERVATION) {
						ParameterValueList<?> pvl = null;
						try { 
							pvl = row.getObservableParameterValues(pd);
						} catch (Exception e) {}
						
						if (pvl != null) {
							
							// if it is in the source data source
							builder.addObservationParameterValues(pd, pvl.getValues());
							
						} else {
							
							// if it is NOT in the source data source

							// mean values
							if (meanParams.containsKey(pd)) {
								pvl = row.getObservableParameterValues(meanParams.get(pd));
								
								double mean = Tools.average(pvl.getValuesAsDouble());
								
								builder.addObservationParameterValue(pd, mean);
							}
							
							// std dev values
							else 
								if (stdDevParams.containsKey(pd)) {
									pvl = row.getObservableParameterValues(stdDevParams.get(pd));
									
									double stdDev = Tools.stdDev(pvl.getValuesAsDouble());
									
									builder.addObservationParameterValue(pd, stdDev);
								}
						}
					}
			}
			
			builder.finishRow();
		}

		return builder.createDataSet();
	}

	@Override
	public ParameterCollection<ParameterDefinition> getAvailableOutputParameters() {
		return availableParameters;
	}

	@Override
	public void setOutputParameters(ParameterCollection<ParameterDefinition> outputParams) {
		this.outputParameters = outputParams;
	}

}
