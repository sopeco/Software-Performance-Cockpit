package org.sopeco.engine.measurementenvironment.app;

import java.util.List;

import org.sopeco.engine.measurementenvironment.AbstractMEController;
import org.sopeco.engine.measurementenvironment.InputParameter;
import org.sopeco.engine.status.StatusMessage;
import org.sopeco.persistence.entities.exceptions.ExperimentFailedException;

public class TestController extends AbstractMEController {

	@InputParameter(namespace="test")
	int abc = 0;

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

		for (int i = 0; i < abc; i++) {

			try {

				sendInformation("started");

				Thread.sleep(2500);

				sendInformation("1");
				sendInformation("2");

				Thread.sleep(2500);

				sendInformation("ending");

			} catch (Exception e) {
			}

		}
	}

	@Override
	protected void finalizeExperimentSeries() {
	}

	public static void main(String[] args) {
		MECApplication mecapp = MECApplication.get();
		mecapp.addMeasurementController("ABC", new TestController());
		mecapp.startREST(1300);
		mecapp.startRMI();
	}

}
