package org.sopeco.visualisation.eclipse.commands;

import java.io.IOException;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.core.runtime.CoreException;
import org.sopeco.visualisation.eclipse.navigation.PersistenceNavigation;

public class CommandHandler implements IHandler{
	/**
	 * singleton instance of the ViewsTreeView.
	 */
	private PersistenceNavigation navigationTreeView;

	/**
	 * This method serves as command dispatcher using unique command-IDs.
	 */
	@Override
	public Object execute(ExecutionEvent event){
		navigationTreeView = PersistenceNavigation.getInstance();

		if (navigationTreeView != null) {
			AbstractCommand command = AbstractCommand.getCommand(event
					.getCommand().getId(), navigationTreeView);
			if (command != null) {
				try {
					command.execute(event);
				} catch (CoreException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return null;
	}

	@Override
	public void addHandlerListener(IHandlerListener handlerListener) {
		// nothing to do
	}

	@Override
	public void dispose() {
		// nothing to do
	}

	@Override
	public void removeHandlerListener(IHandlerListener handlerListener) {
		// nothing to do
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public boolean isHandled() {
		return true;
	}
}
