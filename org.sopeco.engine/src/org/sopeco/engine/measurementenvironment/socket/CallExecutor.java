package org.sopeco.engine.measurementenvironment.socket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.sopeco.engine.measurementenvironment.app.MECApplication;

public final class CallExecutor {

	private static CallExecutor singleton;

	public static void execute(CallType call, ObjectInputStream oiStream,
			ObjectOutputStream ooStream) {

		if (singleton == null) {
			singleton = new CallExecutor();
		}

		singleton.executeInThread(call, oiStream, ooStream);
	}

	private ExecutorService threadPool = Executors.newCachedThreadPool();

	private CallExecutor() {
	}

	public void executeInThread(final CallType call,
			final ObjectInputStream oiStream, final ObjectOutputStream ooStream) {
		Runnable run = new Runnable() {
			@Override
			public void run() {
				try {
					switch (call) {
					case AVAILABLE_CONTROLLER:
						singleton.availableController(oiStream, ooStream);
						break;
					default:
						throw new IllegalStateException(call
								+ " is not a valid CallType!");
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		threadPool.execute(run);
	}

	private void availableController(ObjectInputStream in,
			ObjectOutputStream out) throws IOException {
		out.writeObject(MECApplication.get().getControllerList());
		out.flush();
	}
}
