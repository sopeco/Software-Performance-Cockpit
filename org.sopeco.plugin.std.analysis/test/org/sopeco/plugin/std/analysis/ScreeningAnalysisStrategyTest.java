package org.sopeco.plugin.std.analysis;

import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.sopeco.engine.analysis.ICorrelationStrategy;
import org.sopeco.engine.analysis.IScreeningAnalysisResult;
import org.sopeco.engine.analysis.IScreeningAnalysisStrategy;
import org.sopeco.persistence.EntityFactory;
import org.sopeco.persistence.dataset.DataSetAggregated;
import org.sopeco.persistence.dataset.DataSetColumnBuilder;
import org.sopeco.persistence.dataset.ParameterValueList;
import org.sopeco.persistence.entities.definition.AnalysisConfiguration;
import org.sopeco.persistence.entities.definition.MeasurementEnvironmentDefinition;
import org.sopeco.persistence.entities.definition.ParameterDefinition;
import org.sopeco.persistence.entities.definition.ParameterNamespace;
import org.sopeco.persistence.entities.definition.ParameterRole;
import org.sopeco.persistence.entities.definition.ScenarioDefinition;

/**
 * Test class for implementations of the {@link ICorrelationStrategy}
 * interface that implement a correlation analysis.
 * 
 * @author Dennis Westermann
 * 
 */
@RunWith(Parameterized.class)
public class ScreeningAnalysisStrategyTest {

	private IScreeningAnalysisStrategy strategy;
	private AnalysisConfiguration analysisConfiguration;
	private DataSetAggregated dataset;
	
	private ParameterDefinition dummyInput1;
	private ParameterDefinition dummyInput2;
	private ParameterDefinition dummyOutput;

	
	@Parameters
	public static Collection<Object[]> data() {
		Object[][] data = new Object[][] {
				{ (IScreeningAnalysisStrategy) new ScreeningAnalysisStrategyExtension().createExtensionArtifact()}
				};
		return Arrays.asList(data);
	}

	public ScreeningAnalysisStrategyTest(IScreeningAnalysisStrategy strategy) throws IOException {
		this.strategy = strategy;

		this.dataset = createScreeningDesignDummyDataSet();
		
		this.analysisConfiguration = EntityFactory.createAnalysisConfiguration(ScreeningAnalysisStrategyExtension.NAME, new HashMap<String, String>());
		this.analysisConfiguration.getDependentParameters().add(dummyOutput);
		this.analysisConfiguration.getIndependentParameters().add(dummyInput1);
		this.analysisConfiguration.getIndependentParameters().add(dummyInput2);
	}

	@Before
	public void setUp() throws Exception {
		
	}

	@Test
	public void testAnalysis() {

			assertTrue(strategy.supports(analysisConfiguration));

			strategy.analyse(dataset, analysisConfiguration);

			IScreeningAnalysisResult result = strategy.getScreeningAnalysisResult();

			assertNotNull(result);
			assertEquals(analysisConfiguration.getName(), result.getAnalysisStrategyConfiguration().getName());
			
			assertEquals(2, result.getAllMainEffects().size());
			assertNotNull(result.getMainEffectByParam(dummyInput1));
			assertTrue(result.getMainEffectByParam(dummyInput1).getEffectValue() < result.getMainEffectByParam(dummyInput2).getEffectValue());

			System.out.println("MainEffect of " + dummyInput1.getName() + ": " + result.getMainEffectByParam(dummyInput1).getEffectValue());
			System.out.println("MainEffect of " + dummyInput2.getName() + ": " + result.getMainEffectByParam(dummyInput2).getEffectValue());
	}
	

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private DataSetAggregated createScreeningDesignDummyDataSet() throws IOException {
		
		// Full Factorial Design DataSet
		
		ScenarioDefinition sd = EntityFactory.createScenarioDefinition("DummyScenario", "DummyDef");
		MeasurementEnvironmentDefinition med = EntityFactory.createMeasurementEnvironmentDefinition();
		sd.setMeasurementEnvironmentDefinition(med);
		ParameterNamespace pns = EntityFactory.createNamespace("default");
		dummyInput1 = EntityFactory.createParameterDefinition("DummyInput1", "Integer", ParameterRole.INPUT);
		dummyInput1.setNamespace(pns);
		pns.getParameters().add(dummyInput1);
		dummyInput2 = EntityFactory.createParameterDefinition("DummyInput2", "Integer", ParameterRole.INPUT);
		dummyInput2.setNamespace(pns);
		pns.getParameters().add(dummyInput2);
		dummyOutput = EntityFactory.createParameterDefinition("DummyOutput", "Integer", ParameterRole.OBSERVATION);
		dummyOutput.setNamespace(pns);
		pns.getParameters().add(dummyOutput);
		med.setRoot(pns);
	

		DataSetColumnBuilder builder = new DataSetColumnBuilder();
		builder.startInputColumn(dummyInput1);
		builder.addInputValue(1);
		builder.addInputValue(2);
		builder.addInputValue(1);
		builder.addInputValue(2);
		builder.addInputValue(2);
		builder.addInputValue(1);
		builder.finishColumn();
		
		builder.startInputColumn(dummyInput2);
		builder.addInputValue(1);
		builder.addInputValue(2);
		builder.addInputValue(2);
		builder.addInputValue(1);
		builder.addInputValue(1);
		builder.addInputValue(2);
		builder.finishColumn();


		ArrayList<ParameterValueList> obsValueLists = new ArrayList<ParameterValueList>();
		builder.startObservationColumn(dummyOutput);
		ArrayList<Object> obsValues1 = new ArrayList<Object>();
		obsValues1.add(30);
		obsValueLists.add(new ParameterValueList<Object>(dummyOutput, obsValues1));

		ArrayList<Object> obsValues2 = new ArrayList<Object>();
		obsValues2.add(60);
		obsValueLists.add(new ParameterValueList(dummyOutput, obsValues2));

		ArrayList<Object> obsValues3 = new ArrayList<Object>();
		obsValues3.add(50);
		obsValueLists.add(new ParameterValueList<Object>(dummyOutput, obsValues3));

		ArrayList<Object> obsValues4 = new ArrayList<Object>();
		obsValues4.add(40);
		obsValueLists.add(new ParameterValueList(dummyOutput, obsValues4));

		ArrayList<Object> obsValues5 = new ArrayList<Object>();
		obsValues5.add(40);
		obsValueLists.add(new ParameterValueList<Object>(dummyOutput, obsValues5));

		ArrayList<Object> obsValues6 = new ArrayList<Object>();
		obsValues6.add(50);
		obsValueLists.add(new ParameterValueList(dummyOutput, obsValues6));

		
		builder.addObservationValueLists(obsValueLists);
		builder.finishColumn();

		return builder.createDataSet();
	}
	
	
	
	

}
