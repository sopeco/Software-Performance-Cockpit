package org.sopeco.model.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.eclipse.emf.compare.util.ModelUtils;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.sopeco.model.configuration.ConfigurationPackage;

public class EMFUtil {

	public static String saveToString(final EObject objectToSave) throws IOException {

		String str = ModelUtils.serialize(objectToSave);
		
		return str;
	}
	
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
}
