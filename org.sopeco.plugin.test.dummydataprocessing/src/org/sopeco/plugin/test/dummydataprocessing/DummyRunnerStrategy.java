/**
 * 
 */
package org.sopeco.plugin.test.dummydataprocessing;

import java.util.Collection;
import java.util.Map;

import org.sopeco.engine.processing.IProcessingStrategy;
import org.sopeco.engine.registry.AbstractSoPeCoExtensionArtifact;
import org.sopeco.engine.registry.ISoPeCoExtension;
import org.sopeco.persistence.dataset.DataSetAggregated;
import org.sopeco.persistence.entities.definition.ParameterDefinition;
import org.sopeco.persistence.util.ParameterCollection;
import org.sopeco.persistence.util.ParameterCollectionFactory;

/**
 * A test strategy (empty) to test extension loading mechanism in the runner project.
 * 
 * 
 * @author Roozbeh Farahbod
 *
 */
public class DummyRunnerStrategy extends AbstractSoPeCoExtensionArtifact implements IProcessingStrategy {

	public DummyRunnerStrategy(ISoPeCoExtension<?> provider) {
		super(provider);
	}

	@Override
	public void setSourceDataSet(DataSetAggregated... source) {
	}

	@Override
	public void setSourceDataSet(Collection<DataSetAggregated> source) {
		
	}

	@Override
	public DataSetAggregated processData() {
		return null;
	}

	@Override
	public ParameterCollection<ParameterDefinition> getAvailableOutputParameters() {
		return ParameterCollectionFactory.createParameterDefinitionCollection();
	}

	@Override
	public void setOutputParameters(ParameterCollection<ParameterDefinition> outputParams) {
	}

	@Override
	public ParameterCollection<ParameterDefinition> getConfigurationParameters() {
		return null;
	}

	@Override
	public void configure(Map<ParameterDefinition, Object> values) {
	}


}
