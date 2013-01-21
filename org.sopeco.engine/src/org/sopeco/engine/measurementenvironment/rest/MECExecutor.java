package org.sopeco.engine.measurementenvironment.rest;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sopeco.engine.measurementenvironment.app.MECApplication;
import org.sopeco.engine.measurementenvironment.rest.TransferPackage.State;
import org.sopeco.persistence.dataset.ParameterValue;
import org.sopeco.persistence.dataset.ParameterValueList;
import org.sopeco.persistence.util.ParameterCollection;

/**
 * 
 * @author Marius Oehler
 * 
 */
public final class MECExecutor {

	private static final ExecutorService EXECUTOR = Executors.newCachedThreadPool();

	private static final Logger LOGGER = LoggerFactory.getLogger(MECExecutor.class);

	private static Map<String, TransferPackage> resultMap = new HashMap<String, TransferPackage>();

	/**
	 * 
	 * @param controllerName
	 * @param acquirerID
	 * @param timeout
	 */
	public static void acquire(final String controllerName, final String acquirerID, final long timeout) {
		EXECUTOR.execute(new Runnable() {
			@Override
			public void run() {

				boolean result;
				resultMap.put(acquirerID, new TransferPackage());
				resultMap.get(acquirerID).setAttachmentA(State.WAITING);

				try {
					result = MECApplication.get().getMEController(controllerName)
							.acquireMEController(acquirerID, timeout);
				} catch (RemoteException e) {
					result = false;
					e.printStackTrace();
				}

				if (result) {
					LOGGER.debug("Acquiring controller '{}' with id '{}' was successful.", controllerName, acquirerID);
				} else {
					LOGGER.debug("Acquiring controller '{}' with id '{}' failed.", controllerName, acquirerID);
				}

				resultMap.get(acquirerID).setAttachmentC(result);
				resultMap.get(acquirerID).setAttachmentA(State.ACQUIRED);
			}
		});
	}

	/**
	 * 
	 * @param key
	 */
	public static void cleanResult(String key) {
		resultMap.remove(key);
	}

	/**
	 * 
	 * @param key
	 * @return
	 */
	public static TransferPackage getResult(String key) {
		if (!resultMap.containsKey(key)) {
			return null;
		}
		return resultMap.get(key);
	}

	/**
	 * 
	 * @param controllerName
	 * @param acquirerID
	 */
	public static void release(final String controllerName, final String acquirerID) {
		EXECUTOR.execute(new Runnable() {
			@Override
			public void run() {

				boolean result;
				resultMap.put(acquirerID, new TransferPackage());
				resultMap.get(acquirerID).setAttachmentA(State.WAITING);

				try {

					MECApplication.get().getMEController(controllerName).releaseMEController(acquirerID);
					result = true;
					LOGGER.debug("Releasing controller '{}' with id '{}' was successful.", controllerName, acquirerID);

				} catch (Exception e) {

					result = false;
					LOGGER.debug("Releasing controller '{}' with id '{}' failed.", controllerName, acquirerID);
					e.printStackTrace();

				}

				resultMap.get(acquirerID).setAttachmentC(result);
				resultMap.get(acquirerID).setAttachmentA(State.RELEASED);
			}
		});
	}

	/**
	 * 
	 * @param controllerName
	 * @param acquirerID
	 * @param inputPVs
	 */
	public static void runExperiment(final String controllerName, final String acquirerID,
			final ParameterCollection<ParameterValue<?>> inputPVs) {
		EXECUTOR.execute(new Runnable() {
			@Override
			public void run() {

				Collection<ParameterValueList<?>> result;
				resultMap.put(acquirerID, new TransferPackage());
				resultMap.get(acquirerID).setAttachmentA(State.WAITING);

				try {

					result = MECApplication.get().getMEController(controllerName).runExperiment(acquirerID, inputPVs);

					LOGGER.debug("RunExperiment on controller '{}' with id '{}' was successful.", controllerName,
							acquirerID);

				} catch (Exception e) {

					result = null;
					LOGGER.error("RunExperiment on controller '{}' with id '{}' failed.", controllerName, acquirerID);
					e.printStackTrace();

					resultMap.get(acquirerID).setAttachmentC(e);
				}

				if (result != null) {
					resultMap.get(acquirerID).setAttachmentC(result);
				}
				resultMap.get(acquirerID).setAttachmentA(State.RUNFINISHED);
			}
		});
	}

	/**
	 * Constructor.
	 */
	private MECExecutor() {
	}
}
