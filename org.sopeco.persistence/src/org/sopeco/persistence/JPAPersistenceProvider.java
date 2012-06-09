package org.sopeco.persistence;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sopeco.persistence.dataset.DataSetAggregated;
import org.sopeco.persistence.entities.ExperimentSeries;
import org.sopeco.persistence.entities.ExperimentSeriesRun;
import org.sopeco.persistence.entities.ScenarioInstance;
import org.sopeco.persistence.entities.analysis.AnalysisResultStorageContainer;
import org.sopeco.persistence.entities.analysis.IStorableAnalysisResult;
import org.sopeco.persistence.entities.keys.ExperimentSeriesPK;
import org.sopeco.persistence.entities.keys.ScenarioInstancePK;
import org.sopeco.persistence.exceptions.DataNotFoundException;

/**
 * This class implements the SoPeCo persistence interface based on the Java
 * Peristence API.
 * 
 * @author Dennis Westermann
 * 
 */
public class JPAPersistenceProvider implements IPersistenceProvider {

	private EntityManagerFactory emf;

	private static Logger logger = LoggerFactory.getLogger(JPAPersistenceProvider.class);

	/**
	 * This constructor creates an instance of {@link JPAPersistenceProvider}
	 * and creates an {@link EntityManagerFactory} for the given persistence
	 * unit.
	 * 
	 * @param peristenceUnit
	 *            - the name of the persistence unit that should be used by the
	 *            provider
	 */
	protected JPAPersistenceProvider(EntityManagerFactory factory) {

		emf = factory;

	}

	@Override
	public void store(ExperimentSeriesRun experimentSeriesRun) {

		experimentSeriesRun.storeDataSets(); // required due to decoupling of
												// data
												// sets from entity structure

		EntityManager em = emf.createEntityManager();
		try {
			// experimentSeriesRun.increaseVersion();
			em.getTransaction().begin();
			experimentSeriesRun = em.merge(experimentSeriesRun);
			em.getTransaction().commit();

		} finally {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			em.close();
		}

	}

	@Override
	public void store(DataSetAggregated dataSet) {

		EntityManager em = emf.createEntityManager();
		try {
			em.getTransaction().begin();
			dataSet = em.merge(dataSet);
			em.getTransaction().commit();

		} finally {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			em.close();
		}

	}

	@Override
	public void store(ExperimentSeries experimentSeries) {

		experimentSeries.storeDataSets(); // required due to decoupling of data
											// sets from entity structure

		EntityManager em = emf.createEntityManager();
		try {

			em.getTransaction().begin();
			em.merge(experimentSeries);
			em.getTransaction().commit();

		} finally {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			em.close();
		}

	}

	@Override
	public void store(ScenarioInstance scenarioInstance) {

		scenarioInstance.storeDataSets(); // required due to decoupling of data
											// sets from entity structure

		EntityManager em = emf.createEntityManager();
		try {

			em.getTransaction().begin();
			em.merge(scenarioInstance);
			em.getTransaction().commit();

		} finally {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			em.close();
		}

	}

	@Override
	public void store(String resultId, IStorableAnalysisResult analysisResult) {
		analysisResult.setId(resultId);
		AnalysisResultStorageContainer containerEntity = EntityFactory.createAnalysisResultStorageContainer(resultId,
				analysisResult);

		EntityManager em = emf.createEntityManager();
		try {

			em.getTransaction().begin();
			em.merge(containerEntity);
			em.getTransaction().commit();

		} finally {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			em.close();
		}
	}

	/* default */int getSize(final Class<?> entityType) {
		int result;
		EntityManager em = emf.createEntityManager();
		try {
			Query query = em.createQuery("SELECT es FROM " + entityType.getSimpleName() + " es");
			@SuppressWarnings("unchecked")
			List<ExperimentSeries> series = query.getResultList();
			result = series.size();
			return result;
		} finally {
			em.close();
		}

	}

	/* default */void disposeAll() {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		try {
			em.createQuery("DELETE FROM " + ExperimentSeriesRun.class.getSimpleName()).executeUpdate();
			em.createQuery("DELETE FROM " + ExperimentSeries.class.getSimpleName()).executeUpdate();
			em.createQuery("DELETE FROM " + ScenarioInstance.class.getSimpleName()).executeUpdate();
			em.createQuery("DELETE FROM " + DataSetAggregated.class.getSimpleName()).executeUpdate();
			em.getTransaction().commit();
		} finally {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			em.close();
		}
	}

	@Override
	public ExperimentSeries loadExperimentSeries(String experimentSeriesName, Long version,
			String scenarioInstanceName, String measurementEnvironmentUrl) throws DataNotFoundException {
		ExperimentSeries experimentSeries = null;
		String errorMsg = "Could not find experiment series for scenario instance { " + scenarioInstanceName + ", "
				+ measurementEnvironmentUrl + " series name " + experimentSeriesName + " and series version " + version
				+ ".";

		EntityManager em = emf.createEntityManager();
		try {
			// em.getTransaction().begin();

			experimentSeries = em.find(ExperimentSeries.class, new ExperimentSeriesPK(experimentSeriesName, version,
					scenarioInstanceName, measurementEnvironmentUrl));
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
		if (experimentSeries != null) {
			return experimentSeries;
		} else {
			logger.debug(errorMsg);
			throw new DataNotFoundException(errorMsg);
		}

	}

	@Override
	public ExperimentSeries loadExperimentSeries(String experimentSeriesName, String scenarioInstanceName,
			String measurementEnvironmentUrl) throws DataNotFoundException {
		List<ExperimentSeries> experimentSeriesWithEqualName = this.loadAllExperimentSeries(experimentSeriesName,
				scenarioInstanceName, measurementEnvironmentUrl);

		String errorMsg = "Could not find an experiment series with name " + experimentSeriesName
				+ " in scenario instance " + scenarioInstanceName + ".";

		ExperimentSeries resultSeries = null;
		if (experimentSeriesWithEqualName != null) {
			for (ExperimentSeries series : experimentSeriesWithEqualName) {
				if (resultSeries == null) {
					resultSeries = series;
				} else if (resultSeries.getVersion() < series.getVersion()) {
					resultSeries = series;
				}
			}
			return resultSeries;
		}

		logger.debug(errorMsg);
		throw new DataNotFoundException(errorMsg);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ExperimentSeries> loadAllExperimentSeries(String experimentSeriesName, String scenarioInstanceName,
			String measurementEnvironmentUrl) throws DataNotFoundException {

		List<ExperimentSeries> experimentSeriesWithEqualName;
		String errorMsg = "Could not find an experiment series with name " + experimentSeriesName
				+ " in scenario instance " + scenarioInstanceName + ".";

		EntityManager em = emf.createEntityManager();

		try {
			Query query = em.createNamedQuery("findExperimentSeriesByName");
			query.setParameter("name", experimentSeriesName);
			query.setParameter("scenarioInstanceName", scenarioInstanceName);
			query.setParameter("measurementEnvironmentUrl", measurementEnvironmentUrl);
			experimentSeriesWithEqualName = query.getResultList();

		} catch (Exception e) {

			logger.error(errorMsg);
			throw new DataNotFoundException(errorMsg, e);
		} finally {
			em.close();
		}

		// check if query was successful
		if (experimentSeriesWithEqualName != null) {
			return experimentSeriesWithEqualName;
		} else {
			logger.debug(errorMsg);
			throw new DataNotFoundException(errorMsg);
		}

	}

	@Override
	public ExperimentSeries loadExperimentSeries(ExperimentSeriesPK primaryKey) throws DataNotFoundException {
		return loadExperimentSeries(primaryKey.getName(), primaryKey.getScenarioInstanceName(),
				primaryKey.getMeasurementEnvironmentUrl());
	}

	@Override
	public ExperimentSeriesRun loadExperimentSeriesRun(Long timestamp) throws DataNotFoundException {
		ExperimentSeriesRun experimentSeriesRun;
		EntityManager em = emf.createEntityManager();
		String errorMsg = "Could not find experiment series run with id " + timestamp + " .";
		try {
			// em.getTransaction().begin();
			experimentSeriesRun = em.find(ExperimentSeriesRun.class, timestamp);
			// em.getTransaction().commit();
		} catch (Exception e) {
			logger.error(errorMsg);
			throw new DataNotFoundException(errorMsg);
		} finally {
			// if (em.getTransaction().isActive()) {
			// em.getTransaction().rollback();
			// }
			em.close();
		}

		// check if query was successful
		if (experimentSeriesRun != null) {
			return experimentSeriesRun;
		} else {
			logger.debug(errorMsg);
			throw new DataNotFoundException(errorMsg);
		}
	}

	@Override
	public DataSetAggregated loadDataSet(String dataSetId) throws DataNotFoundException {
		DataSetAggregated dataSet;
		EntityManager em = emf.createEntityManager();
		String errorMsg = "Could not find data set with id " + dataSetId + " .";
		try {
			dataSet = em.find(DataSetAggregated.class, dataSetId);
		} catch (Exception e) {
			logger.error(errorMsg);
			throw new DataNotFoundException(errorMsg);
		} finally {

			em.close();
		}

		// check if query was successful
		if (dataSet != null) {
			return dataSet;
		} else {
			logger.debug(errorMsg);
			throw new DataNotFoundException(errorMsg);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ScenarioInstance> loadScenarioInstances(String scenarioName) throws DataNotFoundException {
		List<ScenarioInstance> scenarioInstances;
		String errorMsg = "Could not find a scenario instance for scenario " + scenarioName + ".";

		EntityManager em = emf.createEntityManager();

		try {
			// em.getTransaction().begin();
			// em.getEntityManagerFactory().getCache().evictAll(); // clear
			// cache

			Query query = em.createNamedQuery("findScenarioInstancesByName");
			query.setParameter("name", scenarioName);
			scenarioInstances = query.getResultList();

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
		if (scenarioInstances != null) {
			return scenarioInstances;
		} else {
			logger.debug(errorMsg);
			throw new DataNotFoundException(errorMsg);
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ScenarioInstance> loadAllScenarioInstances() throws DataNotFoundException {
		List<ScenarioInstance> scenarioInstances;
		String errorMsg = "Could not find a scenario instance in the database.";

		EntityManager em = emf.createEntityManager();

		try {

			Query query = em.createNamedQuery("findAllScenarioInstances");
			scenarioInstances = query.getResultList();

		} catch (Exception e) {

			logger.error(errorMsg);
			throw new DataNotFoundException(errorMsg, e);
		} finally {
			em.close();
		}

		// check if query was successful
		if (scenarioInstances != null) {
			return scenarioInstances;
		} else {
			logger.debug(errorMsg);
			throw new DataNotFoundException(errorMsg);
		}
	}

	@Override
	public ScenarioInstance loadScenarioInstance(ScenarioInstancePK primaryKey) throws DataNotFoundException {
		return loadScenarioInstance(primaryKey.getName(), primaryKey.getMeasurementEnvironmentUrl());
	}

	@Override
	public ScenarioInstance loadScenarioInstance(String scenarioName, String measurementEnvironmentUrl)
			throws DataNotFoundException {

		ScenarioInstance scenarioInstance;
		String errorMsg = "Could not find scenario instance for scenario " + scenarioName
				+ " and measurement environment URL" + measurementEnvironmentUrl + " .";

		EntityManager em = emf.createEntityManager();
		try {
			// em.getTransaction().begin();

			scenarioInstance = em.find(ScenarioInstance.class, new ScenarioInstancePK(scenarioName,
					measurementEnvironmentUrl));

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
		if (scenarioInstance != null) {
			return scenarioInstance;
		} else {
			logger.debug(errorMsg);
			throw new DataNotFoundException(errorMsg);
		}

	}

	public IStorableAnalysisResult loadAnalysisResult(String resultId) throws DataNotFoundException {
		AnalysisResultStorageContainer container;
		IStorableAnalysisResult resultObject;
		String errorMsg = "Could not find analysis result with id " + resultId + " .";

		EntityManager em = emf.createEntityManager();
		try {

			container = em.find(AnalysisResultStorageContainer.class, resultId);
			resultObject = container.getResultObject();
		} catch (Exception e) {

			logger.error(errorMsg);
			throw new DataNotFoundException(errorMsg, e);
		} finally {

			em.close();
		}

		// check if query was successful
		if (resultObject != null) {
			return resultObject;
		} else {
			logger.debug(errorMsg);
			throw new DataNotFoundException(errorMsg);
		}
	}

	@Override
	public void remove(ExperimentSeriesRun experimentSeriesRun) throws DataNotFoundException {

		String errorMsg = "Could not remove experiment series run " + experimentSeriesRun.toString();

		experimentSeriesRun.removeDataSets(); // required due to decoupling of
												// data
												// sets from entity structure

		EntityManager em = emf.createEntityManager();
		try {

			em.getTransaction().begin();

			// load entity to make it "managed"
			experimentSeriesRun = em.find(ExperimentSeriesRun.class, experimentSeriesRun.getPrimaryKey());

			em.remove(experimentSeriesRun);

			em.getTransaction().commit();

		} catch (Exception e) {

			logger.error(errorMsg);
			throw new DataNotFoundException(errorMsg, e);

		} finally {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			em.close();
		}

	}

	@Override
	public void remove(ExperimentSeries experimentSeries) throws DataNotFoundException {
		String errorMsg = "Could not remove experiment series " + experimentSeries.toString();

		experimentSeries.removeDataSets(); // required due to decoupling of data
											// sets from entity structure

		EntityManager em = emf.createEntityManager();
		try {

			em.getTransaction().begin();

			// load entity to make it "managed"
			experimentSeries = em.find(ExperimentSeries.class, experimentSeries.getPrimaryKey());

			em.remove(experimentSeries);
			em.getTransaction().commit();
		} catch (Exception e) {

			logger.error(errorMsg);
			throw new DataNotFoundException(errorMsg, e);

		} finally {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			em.close();
		}

	}

	@Override
	public void remove(ScenarioInstance scenarioInstance) throws DataNotFoundException {

		scenarioInstance.removeDataSets(); // required due to decoupling of data
											// sets from entity structure

		String errorMsg = "Could not remove scenario instance " + scenarioInstance.toString();

		EntityManager em = emf.createEntityManager();
		try {

			em.getTransaction().begin();

			// load entity to make it "managed"
			scenarioInstance = em.find(ScenarioInstance.class, scenarioInstance.getPrimaryKey());

			em.remove(scenarioInstance);

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

	public void remove(String analysisResultId) throws DataNotFoundException {
		final String errorMsg = "Could not remove analysis result with id " + analysisResultId;

		EntityManager em = emf.createEntityManager();
		try {

			em.getTransaction().begin();

			// load entity to make it "managed"
			AnalysisResultStorageContainer container = em.find(AnalysisResultStorageContainer.class, analysisResultId);

			em.remove(container);

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

	@Override
	public void remove(DataSetAggregated dataSet) throws DataNotFoundException {
		final String errorMsg = "Could not remove DataSet with id " + dataSet.getID();

		EntityManager em = emf.createEntityManager();
		try {

			em.getTransaction().begin();

			// load entity to make it "managed"
			DataSetAggregated managedDataSet = em.find(DataSetAggregated.class, dataSet.getID());

			em.remove(managedDataSet);

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
