package org.sopeco.visualisation.eclipse.commands;

import java.io.IOException;

import org.eclipse.core.runtime.CoreException;
import org.sopeco.visualisation.eclipse.navigation.PersistenceNavigation;

public class RefreshNavigationViewCommand extends AbstractCommand{

	@Override
	public void execute(Object data) throws CoreException, IOException {
		PersistenceNavigation.getInstance().refresh();
	}

}
