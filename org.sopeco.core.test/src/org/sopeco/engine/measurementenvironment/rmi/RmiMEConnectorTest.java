package org.sopeco.engine.measurementenvironment.rmi;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.net.MalformedURLException;
import java.net.URI;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

import org.junit.Test;
import org.sopeco.config.Configuration;
import org.sopeco.config.IConfiguration;
import org.sopeco.core.SoPeCoRunner;
import org.sopeco.core.test.SampleMEController;
import org.sopeco.engine.measurementenvironment.IMeasurementEnvironmentController;
import org.sopeco.persistence.dataset.DataSetAggregated;
import org.sopeco.persistence.dataset.DataSetInputColumn;
import org.sopeco.persistence.entities.ScenarioInstance;

public class RmiMEConnectorTest {
	
	private static final String ME_URI = "rmi://localhost:1099/SampleMEController";
	

	@Test
	public void createRemoteServer() throws RemoteException, MalformedURLException, NotBoundException{
		// use this project folder as root.
		Configuration.getSingleton(RmiMEConnectorTest.class);
		
		IMeasurementEnvironmentController meController = new SampleMEController();
		URI meURI = URI.create("rmi://localhost:1099/CreateRemoteServer");
		
		RmiMEConnector.startRemoteMEController(meController, meURI);
		LocateRegistry.getRegistry(meURI.getHost(), meURI.getPort());
		assertNotNull(Naming.lookup(meURI.toString()));		
	}
	
	
	@Test
	public void connectToMeasurementEnvironment() throws Exception {
		// use this project folder as root.
		Configuration.getSingleton(RmiMEConnectorTest.class);
		
		// create remote MEController
		IMeasurementEnvironmentController meController = new SampleMEController();
		RmiMEConnector.startRemoteMEController(meController, ME_URI);

		IConfiguration config = Configuration.getSingleton();
		
		config.setMeasurementControllerURI(ME_URI);

		// setting the scenario definition file name
		config.setScenarioDescriptionFileName("rsc/test.configuration");

		SoPeCoRunner runner = new SoPeCoRunner();
		runner.run();
		
		ScenarioInstance si = runner.getScenarioInstance();
		
		DataSetAggregated data = si.getExperimentSeriesList().get(0).getExperimentSeriesRuns().get(0).getResultDataSet();
		
		assertTrue(data.getInputColumns().size() > 0);
		
		for (DataSetInputColumn<?> ic: data.getInputColumns()) {
			assertNotNull(ic.getValueList());
			assertTrue(ic.getValueList().size()>0);
		}
		
	}

}
