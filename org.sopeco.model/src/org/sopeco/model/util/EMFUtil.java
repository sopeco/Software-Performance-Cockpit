package org.sopeco.model.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import org.eclipse.emf.compare.util.ModelUtils;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.sopeco.model.configuration.ConfigurationPackage;
import org.sopeco.model.configuration.ScenarioDefinition;

public class EMFUtil {

	/**
	 * Saves an arbitrary {@link EObject} into an XMI representation. 
	 * 
	 * @param objectToSave the EObject instance
	 * @return a String representation
	 * @throws IOException 
	 */
	public static String saveToString(final EObject objectToSave) throws IOException {

		String str = ModelUtils.serialize(objectToSave);
		
		return str;
	}
	
	/**
	 * Loads a SoPeCo configuration {@link EObject} from an XMI representation.
	 * 
	 * @param str the XMI representation 
	 * @return an {@link EObject} instance
	 * @throws IOException
	 */
	public static EObject loadFromSting(String str) throws IOException {

		// convert String into InputStream
		InputStream is = new ByteArrayInputStream(str.getBytes());
		
		ResourceSet resourceSet = new ResourceSetImpl();
		
		// Register the appropriate resource factory to handle all file extensions.
		//
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put
			(Resource.Factory.Registry.DEFAULT_EXTENSION, 
			 new XMIResourceFactoryImpl());

		// Register the package to ensure it is available during loading.
		//
		resourceSet.getPackageRegistry().put
			(ConfigurationPackage.eNS_URI, 
			 ConfigurationPackage.eINSTANCE);
		
		EObject eObject = ModelUtils.load(is, "tmp", resourceSet);
		//return EcoreUtil.getRootContainer(eObject);
		return eObject;
	}

	
	/**
	 * Loads a SoPeCo scenario definition from the given URI.
	 *  
	 * @param uri URI of the scenario configuration
	 * @return an instance of Scenario Definition 
	 * @see {@link ScenarioDefinition}
	 */
	public static ScenarioDefinition loadFromURI(URI uri) {
		//TODO Implement!
		return null;
	}
}
