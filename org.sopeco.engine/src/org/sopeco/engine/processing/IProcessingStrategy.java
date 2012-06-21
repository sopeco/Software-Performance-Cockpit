/**
 * 
 */
package org.sopeco.engine.processing;

import java.util.Collection;
import java.util.Map;

import org.sopeco.engine.registry.ISoPeCoExtensionArtifact;
import org.sopeco.persistence.dataset.DataSetAggregated;
import org.sopeco.persistence.dataset.ParameterValue;
import org.sopeco.persistence.dataset.ParameterValueList;
import org.sopeco.persistence.entities.definition.ParameterDefinition;
import org.sopeco.persistence.util.ParameterCollection;

/**
 * The interface of data processing strategies. A data processing strategy produces a new data set out of an original data set. 
 * 
 * @author Roozbeh Farahbod
 */
public interface IProcessingStrategy extends ISoPeCoExtensionArtifact {

	/**
	 * Sets the source data set(s) for this processing. This method has to be called
	 * before any call to other methods of this strategy.
	 * 
	 * @param source a list of source data sets
	 */
	public void setSourceDataSet(DataSetAggregated... source);
	
	/**
	 * Sets the source data set(s) for this processing. This method has to be called
	 * before any call to other methods of this strategy.
	 * 
	 * @param source a collection of source data sets
	 */
	public void setSourceDataSet(Collection<DataSetAggregated> source);

	/**
	 * Processes the source data set and returns the result. 
	 * 
	 * @return the processed data set
	 */
	public DataSetAggregated processData();

	/**
	 * Returns a collection of available output parameters based on the set source
	 * data set.
	 *   
	 * @return a collection of available output parameters
	 */
	public ParameterCollection<ParameterDefinition> getAvailableOutputParameters();

	/**
	 * Sets a selected set of output parameters to be included in the processed data. 
	 * If used, this method should be called after a call to {@link #setSourceDataSet(DataSetAggregated)}
	 * and before a call to {@link IProcessingStrategy#processData()}.
	 * 
	 * @param outputParams the selected set of output parameters
	 */
	public void setOutputParameters(ParameterCollection<ParameterDefinition> outputParams);
	
	/**
	 * Provides the collection of parameter definitions needed for the configuration of this strategy.
	 * The definitions are used to pass configuration values to the strategy using the {@link #configure(ParameterCollection)}.
	 * 
	 * @return a collection of parameter definitions
	 */
	public ParameterCollection<ParameterDefinition> getConfigurationParameters();
	
	/**
	 * Configures the strategy with the given configuration values.
	 * 
	 * @param values a collection of parameter values
	 * 
	 * @see #getConfigurationParameters()
	 */
	public void configure(Map<ParameterDefinition, Object> values);
}
