package org.sopeco.engine.measurementenvironment;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MEConnector {
	private static Logger logger = LoggerFactory.getLogger(MEConnector.class);
	
	/**
	 * Connects to a remote measurement environment controller (via RMI) identified by the given URI and
	 * returns a local instance. 
	 * 
	 * @param uri the URI of the RMI service
	 * @return a local instance of the controller
	 */
	public static IMeasurementEnvironmentController getMeasurementEnvironmentController(String uri){

		// set path to java policy file TODO: Read path from Config-File
		System.setProperty("java.security.policy", "rsc/wideopen.policy");
	
		if (System.getSecurityManager() == null)
       		System.setSecurityManager ( new RMISecurityManager() );
    
		try {
			  	LocateRegistry.getRegistry("localhost", 1099);
				
				logger.debug("Looking up {}", uri);
				
				IMeasurementEnvironmentController meController = 
				(IMeasurementEnvironmentController) Naming.lookup(uri);
						
				logger.info("Received SatelliteController instance from {}", uri);
				
				return meController;
		    
	    } catch (RemoteException e) {
	    	
			throw new IllegalStateException("Cannot access remote controller.", e);
		} catch (MalformedURLException e) {
			
			throw new IllegalStateException("Malformed URL.", e);
		} catch (NotBoundException e) {
		
			throw new IllegalStateException("NotBoundException: ", e);
		} 
		
	}
}
