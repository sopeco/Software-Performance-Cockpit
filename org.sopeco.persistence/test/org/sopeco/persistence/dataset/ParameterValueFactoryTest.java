package org.sopeco.persistence.dataset;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.fail;

import org.junit.Before;
import org.junit.Test;
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
 * Test class for the {@link ParameterValueFactory}.
 * 
 * @author Dennis Westermann
 * 
 */
public class ParameterValueFactoryTest {


	@Test
	public void testCreateParameterValues() {

		ScenarioDefinitionBuilder sdb = new ScenarioDefinitionBuilder("TestScenario");
		sdb.createNewNamespace("org");
		sdb.createChildNamespace("sopeco");
		ParameterDefinition pd = sdb.createParameter("dummy", ParameterType.STRING, ParameterRole.INPUT);
		ParameterValue<?> pv = ParameterValueFactory.createParameterValue(pd, "stringvalue");
		System.out.println(pv.getValueAsString());
	}
}
