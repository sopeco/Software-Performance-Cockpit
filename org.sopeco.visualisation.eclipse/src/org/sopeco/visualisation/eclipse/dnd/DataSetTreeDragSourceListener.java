package org.sopeco.visualisation.eclipse.dnd;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.TextTransfer;
import org.sopeco.persistence.entities.ExperimentSeries;
import org.sopeco.persistence.entities.ScenarioInstance;

/**
 * This class represents the drag source for the DataSetTreeView.
 * 
 * @author D054009
 * 
 */
public class DataSetTreeDragSourceListener implements DragSourceListener {

	/**
	 * Separator for the payload
	 */
	protected static final String payloadSeparator = ";";

	protected static final String pathSeparator = "/";
	/**
	 * Singleton instance of DataSetTreeViewer from which the entities are
	 * dragged.
	 */
	private TreeViewer viewer;

	/**
	 * Constructor
	 * 
	 * @param viewer
	 */
	public DataSetTreeDragSourceListener(TreeViewer viewer) {
		this.viewer = viewer;
	}

	/**
	 * Start dragging. Check whether dragging is supported.
	 * Dragging supported only for ExperimentSeriesRuns
	 */
	@Override
	public void dragStart(DragSourceEvent event) {
		IStructuredSelection selection = (IStructuredSelection) viewer
				.getSelection();
		
		Iterator<?> iterator = selection.iterator();
		while (iterator.hasNext()) {
			Object next = iterator.next();
			if(next instanceof ScenarioInstance || next instanceof ExperimentSeries){
				event.doit = false;
				return;
			}
		}
		event.doit = true;

	}

	/**
	 * Set data to be dragged. In this case it is a string representation of an
	 * array of DataSetNodes. The payload is a string of DataSetNode names
	 * separated by ";".
	 */
	@Override
	public void dragSetData(DragSourceEvent event) {
		// TODO: check how to transfer!!!
		if (TextTransfer.getInstance().isSupportedType(event.dataType)) {
			IStructuredSelection selection = (IStructuredSelection) viewer
					.getSelection();
			Iterator<?> iterator = selection.iterator();
			
			List<Object> payload = new ArrayList<Object>();
			
			
			while (iterator.hasNext()) {
				Object selectedObject = iterator.next();
				payload.add(selectedObject);
			}
			
			event.data = payload;
		}
	}

	@Override
	public void dragFinished(DragSourceEvent event) {
		// no cleaning needed
	}

}
