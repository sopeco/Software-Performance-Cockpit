package org.sopeco.analysis.wrapper;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

import org.sopeco.config.Configuration;

import com.google.gson.Gson;

public class AnalysisWrapper {
	private static String BASE_URL_PROPERTY = "sopeco.analysis.wrapper.url";
	private static String DEFAULT_BASE_URL = "http://localhost:8080/org.analysis.engine.server/rest/rwrapper/";
	private static String EXECUTE_COMMAND_STRING = "executeCommandString";
	private static String EXECUTE_COMMAND_DOUBLE = "executeCommandDouble";
	private static String EXECUTE_COMMAND_STRING_ARRAY = "executeCommandStringArray";
	private static String EXECUTE_COMMAND_DOUBLE_ARRAY = "executeCommandDoubleArray";
	private static String INIT_VARIABLES = "initVariables";
	private static String INIT = "init";
	private static String SHUTDOWN = "shutdown";

	private static AnalysisWrapper instance;

	public static AnalysisWrapper getDefaultWrapper() {
		if (instance == null) {
			instance = new AnalysisWrapper();
		}
		return instance;
	}

	private Gson gson;
	private String baseUrl;

	public AnalysisWrapper() {
		gson = new Gson();
		baseUrl = getbaseUrl();
		String result = executePOSTRequest(null, baseUrl + INIT);
		boolean createdSuccessful = gson.fromJson(result, boolean.class);
		if (!createdSuccessful) {
			throw new RuntimeException("Failed to acquire analysisWrapper within given time frame!");
		}

	}

	/**
	 * For R commands with String return value.
	 * 
	 * @param rCommand
	 * @return
	 */
	public String executeCommandString(String command) {
		String jsonString = gson.toJson(command);

		String result = executePOSTRequest(jsonString, baseUrl + EXECUTE_COMMAND_STRING);

		return gson.fromJson(result, String.class);
	}

	/**
	 * For R commands with double/real return value.
	 * 
	 * @param rCommand
	 * @return
	 */
	public double executeCommandDouble(String command) {

		String jsonString = gson.toJson(command);

		String result = executePOSTRequest(jsonString, baseUrl + EXECUTE_COMMAND_DOUBLE);

		return gson.fromJson(result, double.class);
	}

	/**
	 * For R commands with String array return value.
	 * 
	 * @param rCommand
	 * @return
	 */
	public String[] executeCommandStringArray(String command) {

		String jsonString = gson.toJson(command);

		String result = executePOSTRequest(jsonString, baseUrl + EXECUTE_COMMAND_STRING_ARRAY);

		return gson.fromJson(result, String[].class);
	}

	/**
	 * For R commands with double/real array return value.
	 * 
	 * @param rCommand
	 * @return
	 */
	public double[] executeCommandDoubleArray(String command) {
		String jsonString = gson.toJson(command);

		String result = executePOSTRequest(jsonString, baseUrl + EXECUTE_COMMAND_DOUBLE_ARRAY);

		return gson.fromJson(result, double[].class);
	}

	/**
	 * Initialize Variables
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
		instance = null;
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
		String url = Configuration.getSingleton().getPropertyAsStr(BASE_URL_PROPERTY);
		return url == null ? DEFAULT_BASE_URL : url;
	}
}
