/**
 * 
 */
package org.sopeco.core.test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sopeco.engine.measurementenvironment.IMeasurementEnvironmentController;
import org.sopeco.model.configuration.environment.ParameterDefinition;
import org.sopeco.model.configuration.measurements.ExperimentTerminationCondition;
import org.sopeco.model.configuration.measurements.NumberOfRepetitions;
import org.sopeco.persistence.dataset.ParameterValue;
import org.sopeco.persistence.dataset.ParameterValueList;
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
	public void initialize(List<ParameterValue<?>> initializationPVList) {
		logger.debug("ME Controller initialized with:");
		for (ParameterValue<?> pv: initializationPVList) 
			logger.debug("   - {} = {}", pv.getParameter().getFullName(), pv.getValue());
	}

	@Override
	public void prepareExperimentSeries(Collection<ParameterValue<?>> preparationPVList) {
		logger.debug("ME Controller is prepared for experiment series with:");
		for (ParameterValue<?> pv: preparationPVList) 
			logger.debug("   - {} = {}", pv.getParameter().getFullName(), pv.getValue());
	}

	@Override
	public Collection<ParameterValueList<?>> runExperiment(List<ParameterValue<?>> inputPVList, ExperimentTerminationCondition terminationCondition) {
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
	public void setObservationParameters(List<ParameterDefinition> observationParameters) {
		responseTime = observationParameters.get(0);
		logger.debug("Observation parameter is set as {}.", responseTime.getName());
	}

}
