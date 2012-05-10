package org.sopeco.engine.measurementenvironment;

import java.net.MalformedURLException;
import java.net.URI;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sopeco.config.Configuration;

public class MEConnector {
	private static Logger logger = LoggerFactory.getLogger(MEConnector.class);
	
	/**
	 * Connects to a remote measurement environment controller (via RMI) identified by the given URI and
	 * returns a local instance. 
	 * 
	 * @param meURI the URI of the RMI service
	 * @return a local instance of the controller
	 */
	public static IMeasurementEnvironmentController getMeasurementEnvironmentController(URI meURI){

		String securityPolicyFile = Configuration.getSingleton().getPropertyAsStr("java.security.policy");
		if (securityPolicyFile != null){
			System.setProperty("java.security.policy", securityPolicyFile);
		}
		
		
		
		if (System.getSecurityManager() == null)
       		System.setSecurityManager ( new RMISecurityManager() );
    
		try {
			  	LocateRegistry.getRegistry(meURI.getHost(), meURI.getPort());
				
				logger.debug("Looking up {}", meURI);
				
				IMeasurementEnvironmentController meController = 
				(IMeasurementEnvironmentController) Naming.lookup(meURI.toString());
						
				logger.info("Received SatelliteController instance from {}", meURI);
				
				return meController;
		    
	    } catch (RemoteException e) {
	    	
			throw new IllegalStateException("Cannot access remote controller.", e);
		} catch (MalformedURLException e) {
			
			throw new IllegalStateException("Malformed URI.", e);
		} catch (NotBoundException e) {
		
			throw new IllegalStateException("NotBoundException: ", e);
		} 
		
	}
}
