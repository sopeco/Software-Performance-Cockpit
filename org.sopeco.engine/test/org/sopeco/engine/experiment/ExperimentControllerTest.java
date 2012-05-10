package org.sopeco.engine.experiment;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.sopeco.engine.helper.ConfigurationBuilder;
import org.sopeco.engine.helper.DummyDataSet;
import org.sopeco.engine.helper.DummyMEController;
import org.sopeco.persistence.dataset.DataSetAggregated;
import org.sopeco.persistence.dataset.util.ParameterType;
import org.sopeco.persistence.entities.definition.ParameterRole;

public class ExperimentControllerTest {
	
	private static final Double INITIALIZATION_VALUE = 10.5;
	private static final Object PREPARATION_VALUE = "prepare";
	private static final Object EXPERIMENT_VALUE = 100;
	
	private DummyMEController meController;
	private IExperimentController expController;
	
	@Before
	public void before(){
		ConfigurationBuilder builder = new ConfigurationBuilder("test");
		meController = new DummyMEController();
		expController = builder.createExperimentController(meController);
	}
	
	@Test 
	public void create(){
		assertNotNull(expController);
	}
	
	@Test
	public void initialize(){
		ConfigurationBuilder builder = new ConfigurationBuilder("test");
		builder.createNamespace("initialization");
		builder.createParameter("initParameter",ParameterType.DOUBLE, ParameterRole.INPUT);
		builder.createParameterValue(INITIALIZATION_VALUE);

		//TODO
		expController.initialize(builder.getPVList(),null);
		assertEquals(INITIALIZATION_VALUE, meController.getInitializationValue().getValue());
		assertEquals(builder.getCurrentParameter(), meController.getInitializationValue().getParameter());
	}
	
	@Test
	public void prepareExperimentSeries(){
		ConfigurationBuilder builder = new ConfigurationBuilder("test");
		builder.createNamespace("preparation");
		builder.createParameter("prepParameter",ParameterType.STRING, ParameterRole.INPUT);
		builder.createParameterValue(PREPARATION_VALUE);
		
		// TODO do it right!
//		expController.prepareExperimentSeries(builder.getPVList());
//		assertEquals(PREPARATION_VALUE, meController.getPreparationValue().getValue());
//		assertEquals(builder.getCurrentParameter(), meController.getPreparationValue().getParameter());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void runExperimentWithoutTerminationCondition(){
		ConfigurationBuilder builder = new ConfigurationBuilder("test");
		builder.createNamespace("run");
		builder.createParameter("rumParameter",ParameterType.INTEGER, ParameterRole.INPUT);
		builder.createParameterValue(EXPERIMENT_VALUE);
		
		expController.runExperiment(builder.getPVList(), null);
	}
	
	@Test
	public void runExperiment(){
		ConfigurationBuilder builder = new ConfigurationBuilder("test");
		builder.createNamespace("run");
		builder.createParameter("rumParameter",ParameterType.INTEGER, ParameterRole.INPUT);
		builder.createParameterValue(EXPERIMENT_VALUE);
		builder.createNumberOfRunsCondition(10);
		
		expController.runExperiment(builder.getPVList(), builder.getTerminationCondition());
		DataSetAggregated resultDataSet = expController.getLastSuccessfulExperimentResults();
		DummyDataSet result = new DummyDataSet(resultDataSet);
		
		assertEquals(1, result.getNumberOfRows());
		assertEquals(EXPERIMENT_VALUE, result.getResultValue());
		assertEquals(builder.getPVList().size(), result.getInputPVList().size());
		assertTrue(builder.getPVList().containsAll(result.getInputPVList()));
	}
	
}
