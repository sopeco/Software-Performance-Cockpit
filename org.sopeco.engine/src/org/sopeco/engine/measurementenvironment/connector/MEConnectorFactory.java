package org.sopeco.engine.measurementenvironment.connector;

import java.net.ConnectException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Set;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sopeco.engine.measurementenvironment.IMeasurementEnvironmentController;
import org.sopeco.engine.status.ErrorInfo;
import org.sopeco.engine.status.EventType;
import org.sopeco.engine.status.StatusBroker;
import org.sopeco.persistence.dataset.ParameterValue;
import org.sopeco.persistence.entities.definition.ExperimentTerminationCondition;
import org.sopeco.persistence.util.ParameterCollection;
import org.sopeco.persistence.util.ParameterCollectionFactory;

/**
 * 
 * @author Marius Oehler
 * 
 */
public final class MEConnectorFactory {
	private static final Logger LOGGER = LoggerFactory.getLogger(MEConnectorFactory.class);

	private MEConnectorFactory() {
	}

	public static IMeasurementEnvironmentController connectTo(URI uri) {
		if (uri.getScheme().toLowerCase().equals("rmi")) {
			StatusBroker.getManager(uri.toString()).newStatus(EventType.CONNECT_TO_MEC);
			LOGGER.debug("Creating RMI MEConnector.");
			try {
				return new RmiMEConnector().connectToMEController(uri);
			} catch (RuntimeException e) {
				ErrorInfo info = new ErrorInfo();
				info.setThrowable(e);
				StatusBroker.getManager(uri.toString()).newStatus(EventType.ERROR, e);
				throw e;
			}
		} else if (uri.getScheme().toLowerCase().equals("http")) {
			StatusBroker.getManager(uri.toString()).newStatus(EventType.CONNECT_TO_MEC);
			LOGGER.debug("Creating REST MEConnector.");
			try {
				return new RestMEConnector().connectToMEController(uri);
			} catch (RuntimeException e) {
				ErrorInfo info = new ErrorInfo();
				info.setThrowable(e);
				StatusBroker.getManager(uri.toString()).newStatus(EventType.ERROR, e);
				throw e;
			}
		} else {
			IllegalStateException exception = new IllegalStateException("Only 'rmi://' and 'http://' are supported.");

			ErrorInfo info = new ErrorInfo();
			info.setThrowable(exception);

			StatusBroker.getManager(uri.toString()).newStatus(EventType.ERROR, exception);
			throw exception;
		}

	}
}
