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
package org.sopeco.analysis.wrapper;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URI;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sopeco.analysis.wrapper.exception.AnalysisWrapperException;
import org.sopeco.config.Configuration;
import org.sopeco.config.IConfiguration;

import com.google.gson.Gson;

/**
 * The Analysis wrapper is used to communicate with an external R-Server.
 * 
 * @author Alexander Wert
 * 
 */
public class AnalysisWrapper {
	private static final String BASE_URL_PROPERTY = "sopeco.analysis.wrapper.url";
	private static final String DEFAULT_BASE_URL = "http://localhost:8080/org.analysis.engine.server/rest/rwrapper/";
	private static final String EXECUTE_COMMAND_STRING = "executeCommandString";
	private static final String EXECUTE_COMMAND_DOUBLE = "executeCommandDouble";
	private static final String EXECUTE_COMMAND_STRING_ARRAY = "executeCommandStringArray";
	private static final String EXECUTE_COMMAND_DOUBLE_ARRAY = "executeCommandDoubleArray";
	private static final String INIT_VARIABLES = "initVariables";
	private static final String INIT = "init";
	private static final String SHUTDOWN = "shutdown";

	private static final Logger logger = LoggerFactory.getLogger(AnalysisWrapper.class);

	private Gson gson;
	private String baseUrl;
	private final String wrapperId;

	/**
	 * Constructor for creating an AnalysisWrapper.
	 * 
	 * @throws AnalysisWrapperException
	 */
	public AnalysisWrapper() throws AnalysisWrapperException {
		gson = new Gson();
		baseUrl = getbaseUrl();
		String result = executePOSTRequest(null, baseUrl + INIT);
		wrapperId = gson.fromJson(result, String.class);

	}

	/**
	 * For R commands with String return value. Executes the given command and
	 * returns a String value.
	 * 
	 * @param command
	 *            command string to be executed by the rEngine
	 * @return Returns the result of the given command as string
	 * @throws AnalysisWrapperException
	 */
	public String executeCommandString(String command) throws AnalysisWrapperException {
		logger.debug(command);
		String[] stringVector = new String[2];
		stringVector[0] = gson.toJson(wrapperId);
		stringVector[1] = gson.toJson(command);
		String jsonString = gson.toJson(stringVector);

		String result = executePOSTRequest(jsonString, baseUrl + EXECUTE_COMMAND_STRING);

		return gson.fromJson(result, String.class);
	}

	/**
	 * For R commands with double return value. Executes the given command and
	 * returns a double value.
	 * 
	 * @param command
	 *            command string to be executed by the rEngine
	 * @return Returns the result of the given command as double
	 * @throws AnalysisWrapperException
	 */
	public double executeCommandDouble(String command) throws AnalysisWrapperException {
		logger.debug(command);

		String[] stringVector = new String[2];
		stringVector[0] = gson.toJson(wrapperId);
		stringVector[1] = gson.toJson(command);
		String jsonString = gson.toJson(stringVector);

		String result = executePOSTRequest(jsonString, baseUrl + EXECUTE_COMMAND_DOUBLE);
		String stringResult = gson.fromJson(result, String.class);
		return Double.parseDouble(stringResult);
	}

	/**
	 * For R commands with a return value expected as string array. Executes the
	 * given command and returns a string array.
	 * 
	 * @param command
	 *            command string to be executed by the rEngine
	 * @return Returns the result of the given command as string array
	 * @throws AnalysisWrapperException
	 */
	public String[] executeCommandStringArray(String command) throws AnalysisWrapperException {

		logger.debug(command);

		String[] stringVector = new String[2];
		stringVector[0] = gson.toJson(wrapperId);
		stringVector[1] = gson.toJson(command);
		String jsonString = gson.toJson(stringVector);

		String result = executePOSTRequest(jsonString, baseUrl + EXECUTE_COMMAND_STRING_ARRAY);

		return gson.fromJson(result, String[].class);
	}

	/**
	 * For R commands with a return value expected as double array. Executes the
	 * given command and returns a double array.
	 * 
	 * @param command
	 *            command string to be executed by the rEngine
	 * @return Returns the result of the given command as double array
	 * @throws AnalysisWrapperException
	 */
	public double[] executeCommandDoubleArray(String command) throws AnalysisWrapperException {
		logger.debug(command);

		String[] stringVector = new String[2];
		stringVector[0] = gson.toJson(wrapperId);
		stringVector[1] = gson.toJson(command);
		String jsonString = gson.toJson(stringVector);
		String result = executePOSTRequest(jsonString, baseUrl + EXECUTE_COMMAND_DOUBLE_ARRAY);

		return gson.fromJson(result, double[].class);
	}

	/**
	 * Initialize Variables.
	 * 
	 * @param varPrefix
	 *            prefix of each variable; e.g. 'X'
	 * @param variablesValue
	 *            The value of the variable
	 * @throws AnalysisWrapperException
	 */
	public void initVariables(String varPrefix, double[] variablesValue) throws AnalysisWrapperException {

		String[] stringVector = new String[3];
		stringVector[0] = gson.toJson(wrapperId);
		stringVector[1] = gson.toJson(varPrefix);
		stringVector[2] = gson.toJson(variablesValue);
		String jsonString = gson.toJson(stringVector);
		executePOSTRequest(jsonString, baseUrl + INIT_VARIABLES);
	}

	/**
	 * Removes all objects from R's memory.
	 * 
	 * @throws AnalysisWrapperException
	 */
	public void deleteAllObjects() throws AnalysisWrapperException {
		executeCommandString("rm(list = ls())");
	}

	/**
	 * Shuts down R.
	 * 
	 * @throws AnalysisWrapperException
	 */
	public void shutdown() throws AnalysisWrapperException {
		executePOSTRequest(gson.toJson(wrapperId), baseUrl + SHUTDOWN);
	}

	/**
	 * Executes a post request (in the sense of RESTful Services)
	 * 
	 * @param input
	 *            input to be send
	 * @param urlString
	 *            target URL
	 * @return returns output from the service as String
	 * @throws AnalysisWrapperException
	 */
	private String executePOSTRequest(String input, String urlString) throws AnalysisWrapperException {

		try {
			boolean inputExists = false;
			if (input != null && !input.isEmpty()) {
				inputExists = true;
			}

			Proxy proxy = getProxy();

			URL url = new URI(urlString).toURL();
			HttpURLConnection connection = null;
			if (proxy == null) {
				connection = (HttpURLConnection) url.openConnection();
			} else {
				connection = (HttpURLConnection) url.openConnection(proxy);
			}

			connection.setRequestMethod("POST");
			if (inputExists) {
				connection.setRequestProperty("Content-Type", "text/plain");
				connection.setRequestProperty("Content-Length", "" + Integer.toString(input.getBytes().length));
				connection.setRequestProperty("Content-Language", "en-US");
			}

			connection.setUseCaches(false);

			connection.setDoOutput(true);
			if (inputExists) {
				connection.setDoInput(true);
				DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
				wr.writeBytes(input);
				wr.flush();
				wr.close();
			}

			String result = inputStreamToString(connection.getInputStream());
			connection.disconnect();
			return result;
		} catch (Exception e) {
			throw new AnalysisWrapperException("failed calling R service", e);
		}

	}

	private Proxy getProxy() {
		IConfiguration config = Configuration.getSessionSingleton(Configuration.getGlobalSessionId());
		String proxyHost = config.getPropertyAsStr(IConfiguration.CONF_HTTP_PROXY_HOST);
		int proxyPort = config.getPropertyAsInteger(IConfiguration.CONF_HTTP_PROXY_PORT, -1);
		if (proxyHost == null || proxyPort == -1) {
			return null;
		}
		Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, proxyPort));
		return proxy;
	}

	private String inputStreamToString(InputStream inStream) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(inStream));

		StringBuilder sb = new StringBuilder();

		String line;
		while ((line = br.readLine()) != null) {
			sb.append(line);
		}

		String result = sb.toString();

		br.close();
		return result;

	}

	private String getbaseUrl() {
		String url = Configuration.getSessionUnrelatedSingleton(this.getClass()).getPropertyAsStr(BASE_URL_PROPERTY);
		return url == null ? DEFAULT_BASE_URL : url;
	}
}
