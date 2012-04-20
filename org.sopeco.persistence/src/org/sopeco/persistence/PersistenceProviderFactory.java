package org.sopeco.persistence;

/**
 * Factory class that allows access to a persistence provider ({@link IPersistenceProvider}). 
 * The persistence provider is a singleton.
 * Currently, we have only one type of persistence provider (JPA persistence). 
 * 
 * @author Dennis Westermann
 *
 */
public class PersistenceProviderFactory {

	
	private static IPersistenceProvider persistenceProviderInstance = null;

    
	public static IPersistenceProvider getPersistenceProvider(){
		
		if(persistenceProviderInstance == null){
			//TODO: get info about which provider to load from config registry
			persistenceProviderInstance = new JPAPersistenceProvider("sopeco");
		}   
		
		return persistenceProviderInstance;
	}
	
}
