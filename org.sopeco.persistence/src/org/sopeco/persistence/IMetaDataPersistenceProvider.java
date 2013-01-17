/**
 * Copyright (c) 2013 SAP
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the SAP nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL SAP BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
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
