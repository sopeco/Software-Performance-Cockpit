/**
 * 
 */
package org.sopeco.runner.test.dummyextension;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.sopeco.engine.processing.IProcessingStrategy;
import org.sopeco.engine.registry.AbstractSoPeCoExtensionArtifact;
import org.sopeco.engine.registry.ISoPeCoExtension;
import org.sopeco.persistence.EntityFactory;
import org.sopeco.persistence.dataset.DataSetAggregated;
import org.sopeco.persistence.dataset.DataSetRow;
import org.sopeco.persistence.dataset.DataSetRowBuilder;
import org.sopeco.persistence.dataset.ParameterValue;
import org.sopeco.persistence.dataset.ParameterValueList;
import org.sopeco.persistence.entities.definition.ParameterDefinition;
import org.sopeco.persistence.entities.definition.ParameterRole;
import org.sopeco.persistence.util.ParameterCollection;
import org.sopeco.persistence.util.ParameterCollectionFactory;
import org.sopeco.util.Tools;

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
