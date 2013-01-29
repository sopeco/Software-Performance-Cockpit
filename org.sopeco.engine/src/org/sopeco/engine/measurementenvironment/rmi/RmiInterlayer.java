package org.sopeco.engine.measurementenvironment.rmi;

import java.net.MalformedURLException;
import java.net.URI;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sopeco.engine.measurementenvironment.IMeasurementEnvironmentController;
import org.sopeco.engine.measurementenvironment.MEControllerState;
import org.sopeco.engine.status.StatusBroker;
import org.sopeco.engine.status.StatusMessage;
import org.sopeco.persistence.dataset.ParameterValue;
import org.sopeco.persistence.dataset.ParameterValueList;
import org.sopeco.persistence.entities.definition.ExperimentTerminationCondition;
import org.sopeco.persistence.entities.definition.MeasurementEnvironmentDefinition;
import org.sopeco.persistence.entities.exceptions.ExperimentFailedException;
import org.sopeco.persistence.util.ParameterCollection;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class RmiInterlayer implements IMeasurementEnvironmentController, Runnable {

	private static final Logger LOGGER = LoggerFactory.getLogger(RmiInterlayer.class);
	private static Executor threadPool = Executors.newCachedThreadPool();
	private static final int SLEEP_TIMEOUT = 1000;

	private IMeasurementEnvironmentController meController;
	private boolean running;
	private URI meURI;

	/**
	 * Constructor.
	 * 
	 * @param uri
	 */
	public RmiInterlayer(URI uri) {
		meURI = uri;

		try {
			LocateRegistry.getRegistry(meURI.getHost(), meURI.getPort());

			LOGGER.debug("Looking up {}", meURI);

			meController = (IMeasurementEnvironmentController) Naming.lookup(meURI.toString());

			LOGGER.info("Received SatelliteController instance from {}", meURI);

		} catch (RemoteException e) {
			LOGGER.error("Cannot access remote controller. Error Message: '{}'", e.getMessage());
			throw new IllegalStateException("Cannot access remote controller.", e);
		} catch (MalformedURLException e) {
			LOGGER.error("Malformed URI. Error Message: '{}'", e.getMessage());
			throw new IllegalStateException("Malformed URI.", e);
		} catch (NotBoundException e) {
			LOGGER.error("Name not found in registry. Error Message: '{}'", e.getMessage());
			throw new IllegalStateException("Name not found in registry.", e);
		}

	}

	/**
	 * Fetches the {@link StatusMessage}s from the MEController server and
	 * forwards them to the StatusBroker.
	 */
	private void checkStatusMessages() {
		try {
			List<StatusMessage> list = fetchStatusMessages();

			for (StatusMessage s : list) {
				StatusBroker.getManager(meURI.toString()).newStatus(s);
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		do {
			checkStatusMessages();

			try {
				Thread.sleep(SLEEP_TIMEOUT);
			} catch (Exception e) {
			}
		} while (running);
	}

	@Override
	public Collection<ParameterValueList<?>> runExperiment(String acquirerID,
			ParameterCollection<ParameterValue<?>> inputPVs) throws RemoteException, ExperimentFailedException {

		running = true;
		threadPool.execute(this);
		Collection<ParameterValueList<?>> result = meController.runExperiment(acquirerID, inputPVs);
		running = false;

		checkStatusMessages();

		return result;
	}

	@Override
	public void initialize(String acquirerID, ParameterCollection<ParameterValue<?>> initializationPVs)
			throws RemoteException {
		meController.initialize(acquirerID, initializationPVs);
	}

	@Override
	public void prepareExperimentSeries(String acquirerID, ParameterCollection<ParameterValue<?>> preparationPVs,
			Set<ExperimentTerminationCondition> terminationConditions) throws RemoteException {
		meController.prepareExperimentSeries(acquirerID, preparationPVs, terminationConditions);
	}

	@Override
	public void finalizeExperimentSeries(String acquirerID) throws RemoteException {
		meController.finalizeExperimentSeries(acquirerID);
	}

	@Override
	public MeasurementEnvironmentDefinition getMEDefinition() throws RemoteException {
		return meController.getMEDefinition();
	}

	@Override
	public boolean acquireMEController(String acquirerID, long timeout) throws RemoteException {
		return meController.acquireMEController(acquirerID, timeout);
	}

	@Override
	public void releaseMEController(String acquirerID) throws RemoteException {
		meController.releaseMEController(acquirerID);
	}

	@Override
	public MEControllerState getState() throws RemoteException {
		return meController.getState();
	}

	@Override
	public int getQueueLength() throws RemoteException {
		return meController.getQueueLength();
	}

	@Override
	public List<StatusMessage> fetchStatusMessages() throws RemoteException {
		return meController.fetchStatusMessages();
	}

}
