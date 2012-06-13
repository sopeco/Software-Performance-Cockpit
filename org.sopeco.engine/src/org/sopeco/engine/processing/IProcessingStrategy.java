/**
 * 
 */
package org.sopeco.engine.processing;

import org.sopeco.engine.registry.ISoPeCoExtensionArtifact;
import org.sopeco.persistence.dataset.DataSetAggregated;
import org.sopeco.persistence.entities.definition.ParameterDefinition;
import org.sopeco.persistence.util.ParameterCollection;

/**
 * The interface of data processing strategies. A data processing strategy produces a new data set out of an original data set. 
 * 
 * @author Roozbeh Farahbod
 */
public interface IProcessingStrategy extends ISoPeCoExtensionArtifact {

	/**
	 * Sets the source data set for this processing. This method has to be called
	 * before any call to other methods of this strategy.
	 * 
	 * @param source a source data set
	 */
	public void setSourceDataSet(DataSetAggregated source);
	
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
	
}
