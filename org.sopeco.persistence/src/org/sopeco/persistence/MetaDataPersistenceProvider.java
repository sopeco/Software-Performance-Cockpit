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
