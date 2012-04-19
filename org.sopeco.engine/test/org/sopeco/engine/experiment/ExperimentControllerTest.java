package org.sopeco.engine.experiment;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.sopeco.engine.EngineFactory;
import org.sopeco.engine.helper.ConfigurationBuilder;
import org.sopeco.engine.helper.DummyDataSet;
import org.sopeco.engine.helper.DummyMEController;
import org.sopeco.model.configuration.environment.ParameterRole;
import org.sopeco.persistence.dataset.DataSetAggregated;
import org.sopeco.persistence.dataset.util.ParameterType;

public class ExperimentControllerTest {
	
	private static final Double INITIALIZATION_VALUE = 10.5;
	private static final Object PREPARATION_VALUE = "prepare";
	private static final Object EXPERIMENT_VALUE = 100;
	
	private DummyMEController meController;
	private IExperimentController expController;
	
	@Before
	public void before(){
		meController = new DummyMEController();
		expController = EngineFactory.INSTANCE.createExperimentController(meController);
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

		expController.initialize(builder.getPVList());
		assertEquals(INITIALIZATION_VALUE, meController.getInitializationValue().getValue());
		assertEquals(builder.getCurrentParameter(), meController.getInitializationValue().getParameter());
	}
	
	@Test
	public void prepareExperimentSeries(){
		ConfigurationBuilder builder = new ConfigurationBuilder("test");
		builder.createNamespace("preparation");
		builder.createParameter("prepParameter",ParameterType.STRING, ParameterRole.INPUT);
		builder.createParameterValue(PREPARATION_VALUE);
		
		expController.prepareExperimentSeries(builder.getPVList());
		assertEquals(PREPARATION_VALUE, meController.getPreparationValue().getValue());
		assertEquals(builder.getCurrentParameter(), meController.getPreparationValue().getParameter());
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
		
		DataSetAggregated resultDataSet = expController.runExperiment(builder.getPVList(), builder.getTerminationCondition());
		DummyDataSet result = new DummyDataSet(resultDataSet);
		
		assertEquals(1, result.getNumberOfRows());
		assertEquals(EXPERIMENT_VALUE, result.getResultValue());
		assertEquals(builder.getPVList().size(), result.getInputPVList().size());
		assertTrue(builder.getPVList().containsAll(result.getInputPVList()));
	}
	
}
