package org.sopeco.engine.experiment.impl;

import java.net.MalformedURLException;
import java.net.URI;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sopeco.engine.measurementenvironment.IMeasurementEnvironmentController;

public class MEConnector {
	private static Logger logger = LoggerFactory.getLogger(MEConnector.class);
	
	public static IMeasurementEnvironmentController getMeasurementEnvironmentController(URI uri){

		// set path to java policy file TODO: Read path from Config-File
		System.setProperty("java.security.policy", "rsc/wideopen.policy");
	
		if (System.getSecurityManager() == null)
       		System.setSecurityManager ( new RMISecurityManager() );
    
		try {
			  	LocateRegistry.getRegistry("localhost", 1099);
				
				logger.debug("Looking up {}", uri.toString());
				
				IMeasurementEnvironmentController meController = 
				(IMeasurementEnvironmentController) Naming.lookup(uri.toString());
						
				logger.info("Received SatelliteController instance from {}", uri.toString());
				
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
