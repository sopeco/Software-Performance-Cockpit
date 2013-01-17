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

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sopeco.persistence.exceptions.DataNotFoundException;
import org.sopeco.persistence.metadata.entities.DatabaseInstance;
import org.sopeco.util.session.SessionAwareObject;

/**
 * Peresistence provider for meta data, such as available database instances.
 * 
 * @author Alexander Wert
 * 
 */
public class MetaDataPersistenceProvider extends SessionAwareObject implements IMetaDataPersistenceProvider {
	private EntityManagerFactory emf;
	private static Logger logger = LoggerFactory.getLogger(MetaDataPersistenceProvider.class);

	/**
	 * This constructor creates an instance of {@link JPAPersistenceProvider}
	 * and creates an {@link EntityManagerFactory} for the given persistence
	 * unit.
	 * 
	 * @param peristenceUnit
	 *            - the name of the persistence unit that should be used by the
	 *            provider
	 */
	protected MetaDataPersistenceProvider(String sessionId, EntityManagerFactory factory) {
		super(sessionId);
		emf = factory;

	}

	@Override
	public void store(DatabaseInstance dbInstance) {
		EntityManager em = emf.createEntityManager();
		try {

			em.getTransaction().begin();
			em.merge(dbInstance);
			em.getTransaction().commit();

		} finally {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			em.close();
		}

	}

	@Override
	public DatabaseInstance loadDatabaseInstance(String dbName) throws DataNotFoundException {
		DatabaseInstance databaseInstance;
		String errorMsg = "Could not find database instance with name " + dbName + " .";

		EntityManager em = emf.createEntityManager();
		try {
			// em.getTransaction().begin();

			databaseInstance = em.find(DatabaseInstance.class, dbName);

			// em.getTransaction().commit();

		} catch (Exception e) {

			logger.error(errorMsg);
			throw new DataNotFoundException(errorMsg, e);
		} finally {
			// if (em.getTransaction().isActive()) {
			// em.getTransaction().rollback();
			// }
			em.close();
		}

		// check if query was successful
		if (databaseInstance != null) {
			return databaseInstance;
		} else {
			logger.debug(errorMsg);
			throw new DataNotFoundException(errorMsg);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DatabaseInstance> loadAllDatabaseInstances() throws DataNotFoundException {
		List<DatabaseInstance> databaseInstances;
		String errorMsg = "Could not find a database instance in the database.";

		EntityManager em = emf.createEntityManager();

		try {

			Query query = em.createNamedQuery("findAllDataBaseInstances");
			databaseInstances = query.getResultList();

		} catch (Exception e) {

			logger.error(errorMsg);
			throw new DataNotFoundException(errorMsg, e);
		} finally {
			em.close();
		}

		// check if query was successful
		if (databaseInstances != null) {
			return databaseInstances;
		} else {
			logger.debug(errorMsg);
			throw new DataNotFoundException(errorMsg);
		}
	}

	@Override
	public void remove(DatabaseInstance databaseInstance) throws DataNotFoundException {
		String errorMsg = "Could not remove database instance " + databaseInstance.getId().toString();

		EntityManager em = emf.createEntityManager();
		try {

			em.getTransaction().begin();

			// load entity to make it "managed"
			databaseInstance = em.find(DatabaseInstance.class, databaseInstance.getId());

			em.remove(databaseInstance);

			em.getTransaction().commit();
		} catch (Exception e) {

			throw new DataNotFoundException(errorMsg, e);

		} finally {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			em.close();
		}

	}
}
