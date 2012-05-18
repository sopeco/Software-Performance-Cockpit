/**
 * 
 */
package org.sopeco.core.test;

import java.util.ArrayList;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sopeco.engine.measurementenvironment.IMeasurementEnvironmentController;
import org.sopeco.persistence.dataset.ParameterValue;
import org.sopeco.persistence.dataset.ParameterValueList;
import org.sopeco.persistence.entities.definition.ExperimentTerminationCondition;
import org.sopeco.persistence.entities.definition.NumberOfRepetitions;
import org.sopeco.persistence.entities.definition.ParameterDefinition;
import org.sopeco.persistence.util.ParameterCollection;
import org.sopeco.util.Tools;

/**
 * An example of a Measurement Environment Controller.
 * 
 * @author Roozbeh Farahbod
 *
 */
public class SampleMEController implements IMeasurementEnvironmentController {
	
	private final Logger logger = LoggerFactory.getLogger(SampleMEController.class);
	
	private ParameterDefinition responseTime;

	@Override
	public void initialize(ParameterCollection<ParameterValue<?>> initializationPVList) {
		logger.debug("ME Controller initialized with:");
		for (ParameterValue<?> pv: initializationPVList) 
			logger.debug("   - {} = {}", pv.getParameter().getFullName(), pv.getValue());
	}

	@Override
	public void prepareExperimentSeries(ParameterCollection<ParameterValue<?>> preparationPVList) {
		logger.debug("ME Controller is prepared for experiment series with:");
		for (ParameterValue<?> pv: preparationPVList) 
			logger.debug("   - {} = {}", pv.getParameter().getFullName(), pv.getValue());
	}

	@Override
	public Collection<ParameterValueList<?>> runExperiment(ParameterCollection<ParameterValue<?>> inputPVList, ExperimentTerminationCondition terminationCondition) {
		final ArrayList<ParameterValueList<?>> result = new ArrayList<ParameterValueList<?>>();
		final ParameterValueList<Object> list = new ParameterValueList<Object>(responseTime);
		result.add(list);
		
		int value = 0;
		String implementation = "";
		
		for (ParameterValue<?> parameterValue : inputPVList) {
			// get the 'value' argument
			if (Tools.strEqualName(parameterValue.getParameter().getName(), "value")) {
				value = parameterValue.getValueAsInteger();
			}
			
			// get the 'implementation' argument
			if (Tools.strEqualName(parameterValue.getParameter().getName(), "implementation")) {
				implementation = parameterValue.getValueAsString();
			}
		}

		for (int i=0; i< ((NumberOfRepetitions)terminationCondition).getNumberOfRepetitions(); i++){
			logger.debug("Running repetition {}.", i+1);
			
			logger.debug("fibo({}, {})", value, implementation);
			
			final double time = runAndTimeFoo(value, implementation);
			
			list.addValue(time);
		}
		logger.debug("Finished experiment.");
		return result;
	}

	/**
	 * Runs the Fibonacci algorithm with the given arguments and returns the time it took.
	 * 
	 * @param value the input value
	 * @param implementation the implementation (see {@link Fibonacci})
	 * @return the time it took in nanoseconds
	 */
	private double runAndTimeFoo(int value, String implementation) {
		// TODO SoPeCo monitoring interface
		long startTime = System.nanoTime();
		foo(value, implementation);
		
		return (System.nanoTime() - startTime) / 1000;
	}

	private double foo(double value, String imp) {
		return (0.1 * Math.random() + 0.95) * value; 
	}
	
	@Override
	public void finalizeExperimentSeries() {
		logger.debug("ME Controller finalized.");
	}

	@Override
	public void setObservationParameters(ParameterCollection<ParameterDefinition> observationParameters) {
		responseTime = observationParameters.getOne();
		logger.debug("Observation parameter is set as {}.", responseTime.getName());
	}

}
