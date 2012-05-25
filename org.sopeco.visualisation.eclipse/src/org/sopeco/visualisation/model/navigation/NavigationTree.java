package org.sopeco.visualisation.model.navigation;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sopeco.persistence.IPersistenceProvider;
import org.sopeco.persistence.PersistenceProviderFactory;
import org.sopeco.persistence.entities.ExperimentSeries;
import org.sopeco.persistence.entities.ExperimentSeriesRun;
import org.sopeco.persistence.entities.ScenarioInstance;
import org.sopeco.persistence.exceptions.DataNotFoundException;

public class NavigationTree {
	private static NavigationTree instance;

	public static NavigationTree getInstance() {
		if (instance == null) {
			instance = new NavigationTree();
		}
		return instance;
	}

	private NavigationTree() {
		persistenceProvider = PersistenceProviderFactory.getPersistenceProvider();
	}

	private Logger logger = LoggerFactory.getLogger(NavigationTree.class);

	private IPersistenceProvider persistenceProvider;

	/**
	 * Returns all children of a parent element
	 */
	public List getChildren(Object parentElement) {
		if (parentElement instanceof ScenarioInstance) {
			return ((ScenarioInstance) parentElement).getExperimentSeriesList();
		} else if (parentElement instanceof ExperimentSeries) {
			return ((ExperimentSeries) parentElement).getExperimentSeriesRuns();
		}

		// default case: no children
		return new ArrayList();
	}

	/**
	 * Returns all root nodes
	 */
	public List getRootNodes() {
		try {
			return persistenceProvider.loadAllScenarioInstances();
		} catch (DataNotFoundException e) {
			logger.error("Could not load the root elements of Persistence Navigator. Cause: {}", e);
			throw new RuntimeException(e);
		}

	}

	/**
	 * Returns the parent of an element
	 */
	public Object getParent(Object element) {
		if (element instanceof ExperimentSeries) {
			return ((ExperimentSeries) element).getScenarioInstance();
		} else if (element instanceof ExperimentSeriesRun) {
			return ((ExperimentSeriesRun) element).getExperimentSeries();
		}

		return null;
	}

	/**
	 * checks whether the passed element has children
	 */
	public boolean hasChildren(Object parentElement) {
		return getChildren(parentElement).size() > 0;
	}
	
	public void deleteElement(Object element) throws DataNotFoundException{
		if (element instanceof ScenarioInstance) {
			persistenceProvider.remove((ScenarioInstance)element);
		} else if (element instanceof ExperimentSeries) {
			persistenceProvider.remove((ExperimentSeries)element);
		} else if (element instanceof ExperimentSeriesRun) {
			persistenceProvider.remove((ExperimentSeriesRun)element);
		}
	}
}
