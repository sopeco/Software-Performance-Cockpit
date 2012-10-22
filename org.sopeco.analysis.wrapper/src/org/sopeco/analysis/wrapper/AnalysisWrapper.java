package org.sopeco.analysis.wrapper;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sopeco.config.Configuration;

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

	private static AnalysisWrapper instance;

	/**
	 * 
	 * @return Returns the default instance of the analysis wrapper.
	 */
	public static AnalysisWrapper getDefaultWrapper() {
		if (instance == null || !instance.isActive) {
			instance = new AnalysisWrapper();
		}
		return instance;
	}

	private Gson gson;
	private String baseUrl;
	private boolean isActive = false;

	/**
	 * Constructor for creating an AnalysisWrapper.
	 */
	public AnalysisWrapper() {
		gson = new Gson();
		baseUrl = getbaseUrl();
		String result = executePOSTRequest(null, baseUrl + INIT);
		boolean createdSuccessful = gson.fromJson(result, boolean.class);
		if (!createdSuccessful) {
			throw new RuntimeException("Failed to acquire analysisWrapper within given time frame!");
		}
		isActive = true;

	}

	/**
	 * For R commands with String return value. Executes the given command and
	 * returns a String value.
	 * 
	 * @param command
	 *            command string to be executed by the rEngine
	 * @return Returns the result of the given command as string
	 */
	public String executeCommandString(String command) {
		logger.debug(command);
		String jsonString = gson.toJson(command);

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
	 */
	public double executeCommandDouble(String command) {
		logger.debug(command);

		String jsonString = gson.toJson(command);

		String result = executePOSTRequest(jsonString, baseUrl + EXECUTE_COMMAND_DOUBLE);

		return gson.fromJson(result, double.class);
	}

	/**
	 * For R commands with a return value expected as string array. Executes the
	 * given command and returns a string array.
	 * 
	 * @param command
	 *            command string to be executed by the rEngine
	 * @return Returns the result of the given command as string array
	 */
	public String[] executeCommandStringArray(String command) {

		logger.debug(command);
		
		String jsonString = gson.toJson(command);

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
	 */
	public double[] executeCommandDoubleArray(String command) {
		logger.debug(command);
		
		String jsonString = gson.toJson(command);

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
	 */
	public void initVariables(String varPrefix, double[] variablesValue) {

		String varPrefixString = gson.toJson(varPrefix);
		String variablesValueString = gson.toJson(variablesValue);

		String[] stringVector = new String[2];
		stringVector[0] = varPrefixString;
		stringVector[1] = variablesValueString;
		String jsonString = gson.toJson(stringVector);
		executePOSTRequest(jsonString, baseUrl + INIT_VARIABLES);
	}

	/**
	 * Removes all objects from R's memory.
	 */
	public void deleteAllObjects() {
		executeCommandString("rm(list = ls())");
	}

	/**
	 * Shuts down R.
	 */
	public void shutdown() {
		isActive = false;
		executePOSTRequest(null, baseUrl + SHUTDOWN);
	}

	/**
	 * Executes a post request (in the sense of RESTful Services)
	 * 
	 * @param input
	 *            input to be send
	 * @param urlString
	 *            target URL
	 * @return returns output from the service as String
	 */
	private String executePOSTRequest(String input, String urlString) {

		try {
			boolean inputExists = false;
			if (input != null && !input.isEmpty()) {
				inputExists = true;
			}

			URL url = new URI(urlString).toURL();
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
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
			throw new RuntimeException("failed calling R service", e);
		}

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
