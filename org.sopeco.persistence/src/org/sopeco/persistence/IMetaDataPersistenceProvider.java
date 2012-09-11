package org.sopeco.persistence;

import java.util.List;

import org.sopeco.persistence.exceptions.DataNotFoundException;
import org.sopeco.persistence.metadata.entities.DatabaseInstance;

public interface IMetaDataPersistenceProvider {
	public void store(DatabaseInstance dbInstance);

	DatabaseInstance loadDatabaseInstance(String dbName) throws DataNotFoundException;

	List<DatabaseInstance> loadAllDatabaseInstances() throws DataNotFoundException;
}
