package org.sopeco.visualisation.eclipse.commands;

import java.io.IOException;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.sopeco.persistence.exceptions.DataNotFoundException;
import org.sopeco.visualisation.eclipse.dialogs.Message;
import org.sopeco.visualisation.eclipse.navigation.PersistenceNavigation;
import org.sopeco.visualisation.model.navigation.NavigationTree;

public class DeleteCommand extends AbstractCommand {

	@Override
	public void execute(Object data) throws CoreException, IOException {
		PersistenceNavigation navigationTreeView = PersistenceNavigation.getInstance();

		List<Object> selectedNodes = navigationTreeView.getSelectedNodes();
		for (Object obj : selectedNodes) {
			try {
				NavigationTree.getInstance().deleteElement(obj);
				
			} catch (DataNotFoundException e) {
				Message.showError("Error", "Failed to delete selected tree nodes!", e);
			}
		}
		CommandExecuter.execute(CommandExecuter.REFRESH_NAVIGATION_VIEW_COMMAND, null, PersistenceNavigation.getInstance().getSite());

	}

}
