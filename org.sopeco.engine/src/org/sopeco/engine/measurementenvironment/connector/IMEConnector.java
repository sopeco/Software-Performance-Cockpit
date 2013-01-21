package org.sopeco.engine.measurementenvironment.connector;

import java.net.URI;

import org.sopeco.engine.measurementenvironment.IMeasurementEnvironmentController;

/**
 * 
 * @author Marius Oehler
 * 
 */
public interface IMEConnector {
	IMeasurementEnvironmentController connectToMEController(URI meURI);
}
