package org.sopeco.visualisation.eclipse.navigation;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sopeco.persistence.entities.ExperimentSeries;
import org.sopeco.persistence.entities.ExperimentSeriesRun;
import org.sopeco.persistence.entities.ScenarioInstance;
import org.sopeco.visualisation.model.navigation.NavigationTree;

public class PersistenceNavigationContentProvider implements ITreeContentProvider {
	private Logger logger = LoggerFactory.getLogger(PersistenceNavigationContentProvider.class);
	private NavigationTree navigationTree;

	@Override
	public void dispose() {

	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		dispose();
		if (newInput instanceof NavigationTree) {
			navigationTree = (NavigationTree) newInput;
		}

	}

	/**
	 * Returns all children of a parent element
	 */
	@Override
	public Object[] getChildren(Object parentElement) {
		return navigationTree.getChildren(parentElement).toArray();
		
	}

	/**
	 * Returns all root nodes
	 */
	@Override
	public Object[] getElements(Object arg0) {
		Object[] allScenarios = navigationTree.getRootNodes().toArray();
		return allScenarios;

	}

	/**
	 * Returns the parent of an element
	 */
	@Override
	public Object getParent(Object element) {
		return navigationTree.getChildren(element);
	}

	/**
	 * checks whether the passed element has children
	 */
	@Override
	public boolean hasChildren(Object parentElement) {
		return navigationTree.hasChildren(parentElement);
	}

}
