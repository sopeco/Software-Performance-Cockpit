package org.sopeco.visualisation.eclipse.commands;

import org.eclipse.swt.widgets.Event;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.handlers.IHandlerService;

public class CommandExecuter {
	/**
	 * Executes the command specified by the passed commandId
	 * 
	 * @param commandId
	 *            CommandID specifies the command
	 * @param data
	 *            Data to be transferred to the command
	 * @param site
	 *            The site, which executes the command
	 */
	public static void execute(String commandId, Object data,
			IWorkbenchPartSite site) {

		IHandlerService handlerService = (IHandlerService) site
				.getService(IHandlerService.class);
		Event event = new Event();
		event.data = data;
		try {
			handlerService.executeCommand(commandId, event);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public static final String OPEN_FUNCTION_VIEW_COMMAND = "org.sopeco.visualisation.openFunctionView";
	public static final String OPEN_BOXPLOT_VIEW_COMMAND = "org.sopeco.visualisation.openBoxPlotView";
	public static final String REFRESH_NAVIGATION_VIEW_COMMAND ="org.sopeco.visualisation.refreshNavigation";
	public static final String DELETE_COMMAND ="org.sopeco.visualisation.delete";
	
}
