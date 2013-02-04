/**
 * Copyright (c) 2013 SAP
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the SAP nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL SAP BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.sopeco.engine.measurementenvironment.socket;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sopeco.engine.measurementenvironment.IMeasurementEnvironmentController;
import org.sopeco.engine.measurementenvironment.app.MECApplication;

public final class CallExecutor {

	private static final Logger LOGGER = LoggerFactory.getLogger(CallExecutor.class);

	private static CallExecutor singleton;

	public static void execute(final String controllerName, final String methodName, final Object[] parameter,
			final ObjectOutputStream ooStream) {

		if (singleton == null) {
			singleton = new CallExecutor();
		}

		Runnable run = new Runnable() {
			@Override
			public void run() {
				Method method = singleton.reflectMethod(controllerName, methodName, parameter);

				if (methodName.equals("runExperiment")) {					
					StatusPusher pusher = new StatusPusher(controllerName, ooStream);
					singleton.threadPool.execute(pusher);

					Object result = singleton.invokeMethod(controllerName, method, parameter);
					pusher.setExperimentResults(result);

				} else {
					Object result = singleton.invokeMethod(controllerName, method, parameter);

					try {
						ooStream.writeObject(result);
						ooStream.flush();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		};
		singleton.threadPool.execute(run);

	}

	public static final Class<?> checkPrimitive(Class<?> clazz) {
		if (clazz.equals(java.lang.Byte.class))
			return byte.class;
		if (clazz.equals(java.lang.Short.class))
			return short.class;
		if (clazz.equals(java.lang.Integer.class))
			return int.class;
		if (clazz.equals(java.lang.Long.class))
			return long.class;
		if (clazz.equals(java.lang.Character.class))
			return char.class;
		if (clazz.equals(java.lang.Float.class))
			return float.class;
		if (clazz.equals(java.lang.Double.class))
			return double.class;
		if (clazz.equals(java.lang.Boolean.class))
			return boolean.class;
		if (clazz.equals(java.lang.Void.class))
			return void.class;
		return clazz;
	}

	private ExecutorService threadPool = Executors.newCachedThreadPool();

	private CallExecutor() {
	}

	private Method reflectMethod(String controllerName, String methodName, Object[] parameter) {

		Class<?> targetClass;
		if (controllerName != null) {
			targetClass = IMeasurementEnvironmentController.class;
		} else {
			targetClass = this.getClass();
		}

		Class<?>[] clazzes = new Class<?>[parameter.length];
		for (int i = 0; i < parameter.length; i++) {
			clazzes[i] = checkPrimitive(parameter[i].getClass());
		}

		Method method;
		try {
			method = targetClass.getMethod(methodName, clazzes);
			return method;
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			for (Method m : targetClass.getMethods()) {
				if (m.getName().equals(methodName) && m.getParameterTypes().length == parameter.length) {
					boolean isOk = true;
					for (int i = 0; i < parameter.length; i++) {
						Class<?> clazz = m.getParameterTypes()[i];
						if (!clazz.isInstance(parameter[i])) {
							isOk = false;
						}
					}
					if (isOk) {
						return m;
					}
				}
			}

			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public Object invokeMethod(String controllerName, Method method, Object[] parameter) {
		try {

			if (controllerName != null) {
				LOGGER.debug("Call method {} on controller {}", method.getName(), controllerName);
				IMeasurementEnvironmentController mec = MECApplication.get().getMEController(controllerName);
				return method.invoke(mec, parameter);
			} else {
				LOGGER.debug("Call method {}", method.getName());
				return method.invoke(this, parameter);
			}

		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}

		return null;
	}

	public String[] availableController() {
		return MECApplication.get().getControllerList();
	}

}
