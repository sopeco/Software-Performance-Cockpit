package org.sopeco.engine.measurementenvironment.app;

import org.sopeco.engine.measurementenvironment.socket.SocketAcception;
import org.sopeco.engine.measurementenvironment.socket.SocketAppWrapper;
import org.sopeco.engine.measurementenvironment.socket.SocketManager;

public class TestSoPeCo {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SocketAcception.open();

		try {
			Thread.sleep(4000);
			System.out.println("weiter");
		} catch (Exception e) {
		}

		SocketAppWrapper wrapper = SocketManager.getSocketApp("127.0.0.1");
		if (wrapper != null) {
			String[] controller = wrapper.getAvailableMEController();

			System.out.println("count: " + controller.length);
			for (String s : controller) {
				System.out.println("\t"+s);
			}
		}

		System.out.println("End");
	}

}
