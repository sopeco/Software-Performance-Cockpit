package org.sopeco.visualisation.eclipse.navigation;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.part.ViewPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sopeco.visualisation.eclipse.commands.CommandExecuter;
import org.sopeco.visualisation.eclipse.dnd.DataSetTreeDragSourceListener;
import org.sopeco.visualisation.model.navigation.NavigationTree;

public class PersistenceNavigation extends ViewPart {
	private Logger logger = LoggerFactory.getLogger(PersistenceNavigation.class);

	/**
	 * View ID of this view
	 */
	public static final String viewID = "org.sopeco.visualisation.PersistenceNavigator";

	/**
	 * instance of the DataSetTreeView. As this editor part is allowed to have
	 * only one instance this instance should be a singleton instance
	 */
	private static PersistenceNavigation instance = null;

	/**
	 * Returns the instance of DataSetTreeView
	 * 
	 * @return
	 */
	public static PersistenceNavigation getInstance() {
		return instance;
	}

	/**
	 * TreeViewer instance
	 */
	private TreeViewer viewer;

	/**
	 * List of abstract dataset nodes representing the selection in the tree
	 */
	private final List<Object> selectedNodes;

	/**
	 * Constructor
	 */
	public PersistenceNavigation() {
		selectedNodes = new ArrayList<Object>();
		instance = this;
	}

	@Override
	public void createPartControl(Composite parent) {
		viewer = createTreeViewer(parent);
		initializeDragAndDrop();
		viewer.addSelectionChangedListener(new PNSelectionChangeListener(getSelectedNodes()));
		
		createDoubleClickListener();
		createContextMenu();

	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	/**
	 * Creates and configures tree viewer
	 * 
	 * @param parent
	 * @return
	 */
	private TreeViewer createTreeViewer(Composite parent) {
		TreeViewer viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		viewer.setContentProvider(new PersistenceNavigationContentProvider());
		viewer.setLabelProvider(new PersistenceNavigationLabelProvider());
		
		
		viewer.setInput(NavigationTree.getInstance());

		return viewer;
	}

	/**
	 * Initialization of drag and drop support
	 */
	private void initializeDragAndDrop() {
		// TODO: check how to transfer!
		int ops = DND.DROP_COPY | DND.DROP_MOVE;
		Transfer[] transfers = new Transfer[] { TextTransfer.getInstance() };
		viewer.addDragSupport(ops, transfers, new DataSetTreeDragSourceListener(viewer));
	}

	/**
	 * Creates listener for doubleClick events. Open function view on double
	 * click.
	 */
	private void createDoubleClickListener() {
		viewer.addDoubleClickListener(new IDoubleClickListener() {
			@Override
			public void doubleClick(DoubleClickEvent event) {
				if (getSelectedNodes().size() == 1) {
					CommandExecuter.execute(CommandExecuter.OPEN_FUNCTION_VIEW_COMMAND, null, getSite());
				}
			}
		});
	}

	/**
	 * Creates context menu for the DataSetTreenavigator
	 */
	private void createContextMenu() {
		MenuManager manager = new MenuManager("#PopupMenu");
		manager.setRemoveAllWhenShown(true);
		Menu menu = manager.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);
		getSite().registerContextMenu(manager, viewer);
	}

	public List<Object> getSelectedNodes() {
		return selectedNodes;
	}

	public void refresh() {
		viewer.setSelection(null);
		viewer.refresh();
		
	}

}
