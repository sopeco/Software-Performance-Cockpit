package org.sopeco.visualisation.eclipse.navigation;

import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;

public class PNSelectionChangeListener implements ISelectionChangedListener {

	private final List<Object> selectedNodes;

	public PNSelectionChangeListener(List<Object> selectedNodes) {
		this.selectedNodes = selectedNodes;
	}

	@Override
	public void selectionChanged(SelectionChangedEvent event) {
		selectedNodes.clear();
		IStructuredSelection selection = (IStructuredSelection) event.getSelection();
		Iterator<?> iterator = selection.iterator();
		while (iterator.hasNext()) {
			Object next = iterator.next();
			selectedNodes.add(next);
		}

	}

}
