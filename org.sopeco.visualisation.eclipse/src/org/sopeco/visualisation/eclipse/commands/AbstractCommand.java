package org.sopeco.visualisation.eclipse.commands;

import java.io.IOException;

import org.eclipse.core.runtime.CoreException;
import org.sopeco.visualisation.eclipse.navigation.PersistenceNavigation;
import org.sopeco.visualisation.model.navigation.NavigationTree;

public abstract class AbstractCommand {

	/**
	 * Creates a command instance for the given command ID.
	 * 
	 * @param commandID
	 *            the id a command should be created for.
	 * @param dsTreeView
	 *            DataSetTreeView instance, needed to retrieve current selected
	 *            nodes
	 * @return
	 */
	public static AbstractCommand getCommand(String commandID,
			PersistenceNavigation navigationTreeView) {
		if (commandID.equals(CommandExecuter.OPEN_FUNCTION_VIEW_COMMAND)) {
			return new OpenFunctionViewCommand();
		} else if(commandID.equals(CommandExecuter.OPEN_BOXPLOT_VIEW_COMMAND)){
			return new OpenBoxPlotViewCommand();
		}else if(commandID.equals(CommandExecuter.OPEN_CORRELATION_VIEW_COMMAND)){
			return new OpenCorrelationViewCommand();
		}else if(commandID.equals(CommandExecuter.REFRESH_NAVIGATION_VIEW_COMMAND)){
			return new RefreshNavigationViewCommand();
		}else if(commandID.equals(CommandExecuter.DELETE_COMMAND)){
			return new DeleteCommand();
		}
		
		return null;
	}

	/**
	 * Executes the command
	 * 
	 * @param data
	 *            Any data that might be needed for the command execution
	 * @throws CoreException
	 * @throws IOException
	 * @throws FrameworkException
	 */
	abstract public void execute(Object data) throws CoreException,
			IOException;
}
