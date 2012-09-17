package org.sopeco.persistence;

import java.util.List;

import org.sopeco.persistence.exceptions.DataNotFoundException;
import org.sopeco.persistence.metadata.entities.DatabaseInstance;

/**
 * Persistence provider for meta data. Meta data comprises information about
 * available databases used for storing measurement results and scenario
 * defenitions
 * 
 * @author Alexander Wert
 * 
 */
public interface IMetaDataPersistenceProvider {
	/**
	 * Stores an entry for a database instance.
	 * 
	 * @param dbInstance
	 *            database instance to be stored
	 */
	void store(DatabaseInstance dbInstance);

	/**
	 * Loads the database instance with the given name.
	 * 
	 * @param dbName
	 *            name of the database instance to be loaded
	 * @return Returns the database instance with the given name.
	 * @throws DataNotFoundException
	 *             This exception is thrown if no database instance with the
	 *             given name could be found.
	 */
	DatabaseInstance loadDatabaseInstance(String dbName) throws DataNotFoundException;

	/**
	 * Loads all known database instances.
	 * 
	 * @return
	 * @throws DataNotFoundException
	 */
	List<DatabaseInstance> loadAllDatabaseInstances() throws DataNotFoundException;

	/**
	 * Removes the meta-data entry for the given database instance.
	 * 
	 * @param databaseInstance
	 *            Database instance to be removed
	 * @throws DataNotFoundException
	 */
	void remove(DatabaseInstance databaseInstance) throws DataNotFoundException;
}
