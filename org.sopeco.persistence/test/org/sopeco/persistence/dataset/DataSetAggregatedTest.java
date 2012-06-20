package org.sopeco.persistence.dataset;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.fail;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.sopeco.persistence.DummyFactory;
import org.sopeco.persistence.dataset.util.ParameterType;
import org.sopeco.persistence.entities.ExperimentSeries;
import org.sopeco.persistence.entities.ExperimentSeriesRun;
import org.sopeco.persistence.entities.ScenarioInstance;
import org.sopeco.persistence.entities.definition.ExperimentSeriesDefinition;
import org.sopeco.persistence.entities.definition.ParameterDefinition;
import org.sopeco.persistence.entities.definition.ParameterRole;
import org.sopeco.persistence.entities.definition.ScenarioDefinition;
import org.sopeco.persistence.util.ScenarioDefinitionBuilder;

/**
 * Test class for {@link DataSetAggregated}.
 * 
 * @author Dennis Westermann
 * 
 */
public class DataSetAggregatedTest {


	@Test
	public void testConvertToSimpleDataSet() throws IOException {

		DataSetAggregated dataSetAggregated = DummyFactory.createDummyResultDataSet();
		SimpleDataSet simpleDataSet = dataSetAggregated.convertToSimpleDataSet();
		assertEquals(4, simpleDataSet.size());
	}
}
