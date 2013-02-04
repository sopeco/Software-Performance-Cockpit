package org.sopeco.engine.measurementenvironment.app;

import java.rmi.RemoteException;

import org.sopeco.engine.measurementenvironment.IMeasurementEnvironmentController;
import org.sopeco.engine.measurementenvironment.socket.SocketAcception;
import org.sopeco.engine.measurementenvironment.socket.SocketAppWrapper;
import org.sopeco.engine.measurementenvironment.socket.SocketManager;

public class TestSoPeCo {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SocketAcception.open();

		System.out.println(long.class);
		System.out.println(Long.class);
		
		try {
			Thread.sleep(4000);
			System.out.println("weiter");
		} catch (Exception e) {
		}

		SocketAppWrapper wrapper = SocketManager.getSocketApp("127.0.0.1");
		if (wrapper != null) {

			System.out.println("count: " + wrapper.getAvailableController().length);
			for (String s : wrapper.getAvailableController()) {
				System.out.println("\t" + s);
			}
		}

		try {
			Thread.sleep(4000);
			System.out.println("weiter");
		} catch (Exception e) {
		}

		IMeasurementEnvironmentController mec = wrapper.getMECWrapper(wrapper.getAvailableController()[0]);

		try {
			System.out.println(mec.getState());
			System.out.println("acquire");
			System.out.println(mec.acquireMEController("myID", 0));

			System.out.println("acquire");
			System.out.println(mec.acquireMEController("myID2", 0));

			mec.finalizeExperimentSeries("myID");
			
			System.out.println(mec.getState());
			
			System.out.println(mec.getQueueLength());
			
			System.out.println(mec.getMEDefinition());
			
			System.out.println("release");
			mec.releaseMEController("myID");
			System.out.println(mec.getState());
		} catch (RemoteException e) {
			e.printStackTrace();
		}

		System.out.println("End");
	}

}
