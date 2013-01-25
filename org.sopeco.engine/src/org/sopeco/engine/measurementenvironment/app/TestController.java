package org.sopeco.engine.measurementenvironment.app;


import org.sopeco.engine.measurementenvironment.AbstractMEController;
import org.sopeco.engine.measurementenvironment.InputParameter;
import org.sopeco.persistence.entities.exceptions.ExperimentFailedException;

public class TestController extends AbstractMEController{

	@InputParameter
	int abc;
	
	@Override
	protected void defineResultSet() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void initialize() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void prepareExperimentSeries() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void runExperiment() throws ExperimentFailedException {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void finalizeExperimentSeries() {
		// TODO Auto-generated method stub
		
	}
	
	public static void main(String[] args) {
		MECApplication mecapp = MECApplication.get();
		mecapp.addMeasurementController("ABC", new TestController());
		mecapp.startREST(1300);
	}

}
