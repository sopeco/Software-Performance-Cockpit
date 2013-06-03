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
package org.sopeco.persistence.util;

import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import org.sopeco.persistence.dataset.AbstractDataSetColumn;
import org.sopeco.persistence.dataset.DataSetAggregated;
import org.sopeco.persistence.dataset.DataSetRow;
import org.sopeco.persistence.dataset.ParameterValue;
import org.sopeco.persistence.dataset.ParameterValueList;
import org.sopeco.persistence.dataset.SimpleDataSet;
import org.sopeco.persistence.dataset.SimpleDataSetColumn;
import org.sopeco.persistence.dataset.SimpleDataSetRow;
import org.supercsv.io.CsvListWriter;
import org.supercsv.prefs.CsvPreference;

/**
 * 
 * The CsvHandler manages the storage of DataSets as CSV files.
 * 
 * @author Jens Happe
 * 
 */
public class DataSetCsvHandler {

	private static final char DEFAULT_DECIMAL_DELIMITER = '.';

	private static final String DEFAULT_CSV_EOL = "\r\n";

	/**
	 * Column names are used in the CSV files.
	 */
	private boolean hasColumnNames;

	/**
	 * The used decimal delimiter.
	 */
	private char decimalDelimiter;

	private char valueSeparator;
	private char quoteChar;

	/**
	 * Constructor.
	 * 
	 * @param valueSeparator
	 *            Character to be used for separation in the file.
	 * @param quoteChar
	 *            Character to be used to quote values in the file.
	 * @param hasColumnNames
	 *            Column names are to be stored in the file.
	 */
	public DataSetCsvHandler(char valueSeparator, char quoteChar, boolean hasColumnNames) {
		this(valueSeparator, quoteChar, DEFAULT_DECIMAL_DELIMITER, hasColumnNames);
	}

	/**
	 * Constructor.
	 * 
	 * @param valueSeparator
	 *            Character to be used for separation in the file.
	 * @param commentSeparator
	 *            Character to be used to mark comments in the file.
	 * @param decimalDelimiter
	 *            Character to be used to separate the whole part from the
	 *            fractional part of a number.
	 * @param hasColumnNames
	 *            Column names are to be stored in the file.
	 */
	public DataSetCsvHandler(char valueSeparator, char quoteChar, char decimalDelimiter, boolean hasColumnNames) {
		this.valueSeparator = valueSeparator;
		this.quoteChar = quoteChar;
		this.hasColumnNames = hasColumnNames;
		this.decimalDelimiter = decimalDelimiter;
	}

	/**
	 * Store the content of a DataSet as CSV file.
	 * 
	 * @param dataset
	 *            DataSet to be stored.
	 * @param fileName
	 *            Name of the CSV file.
	 * @throws IOException
	 *             Thrown if the file cannot be written.
	 */
	public void store(SimpleDataSet dataset, String fileName) throws IOException {
		FileWriter fileWriter = new FileWriter(fileName);
		writeCSV(dataset, fileWriter);
		fileWriter.close();
	}

	/**
	 * Converts the passed {@link SimpleDataSet} to a CSV String.
	 * 
	 * @param dataset
	 *            dataset to be converted
	 * @return Returns a CSV string representation of the passed dataset
	 * @throws IOException
	 *             If writing to the string fails
	 */
	public String convertToCSVString(SimpleDataSet dataset) throws IOException {
		StringWriter stringWriter = new StringWriter();
		writeCSV(dataset, stringWriter);
		String csvString = stringWriter.toString();
		stringWriter.close();
		return csvString;
	}

	/**
	 * Store the content of a DataSet as CSV file.
	 * 
	 * @param dataset
	 *            DataSet to be stored.
	 * @param fileName
	 *            Name of the CSV file.
	 * @throws IOException
	 *             Thrown if the file cannot be written.
	 */
	public void store(DataSetAggregated dataset, String fileName) throws IOException {
		FileWriter fileWriter = new FileWriter(fileName);
		writeCSV(dataset, fileWriter);
		fileWriter.close();
	}

	/**
	 * Converts the passed {@link SimpleDataSet} to a CSV String.
	 * 
	 * @param dataset
	 *            dataset to be converted
	 * @return Returns a CSV string representation of the passed dataset
	 * @throws IOException
	 *             If writing to the string fails
	 */
	public String convertToCSVString(DataSetAggregated dataset) throws IOException {
		StringWriter stringWriter = new StringWriter();
		writeCSV(dataset, stringWriter);
		String csvString = stringWriter.toString();
		stringWriter.close();
		return csvString;
	}

	/**
	 * Converts the the passed {@link SimpleDataSet} to a CSV stream and writes
	 * the stream to the passed writer.
	 */
	private void writeCSV(SimpleDataSet dataset, Writer writer) throws IOException {
		CsvPreference csvPreference = new CsvPreference.Builder(quoteChar, valueSeparator, DEFAULT_CSV_EOL).build();
		CsvListWriter csvWriter = new CsvListWriter(writer, csvPreference);

		if (hasColumnNames) {

			List<String> headers = new ArrayList<String>();
			for (SimpleDataSetColumn column : dataset.getColumns()) {
				headers.add(column.getParameter().getName());
			}

			csvWriter.writeHeader(headers.toArray(new String[0]));
		}

		List<String> values = new ArrayList<String>();
		for (SimpleDataSetRow row : dataset) {

			for (ParameterValue pv : row.getRowValues()) {
				addValueToList(values, pv);
			}
			csvWriter.write(values);
			values.clear();
		}

		csvWriter.close();
	}

	/**
	 * Converts the the passed {@link SimpleDataSet} to a CSV stream and writes
	 * the stream to the passed writer.
	 */
	private void writeCSV(DataSetAggregated dataset, Writer writer) throws IOException {
		CsvPreference csvPreference = new CsvPreference.Builder(quoteChar, valueSeparator, DEFAULT_CSV_EOL).build();
		CsvListWriter csvWriter = new CsvListWriter(writer, csvPreference);

		if (hasColumnNames) {

			List<String> headers = new ArrayList<String>();
			for (AbstractDataSetColumn column : dataset.getColumns()) {
				headers.add(column.getParameter().getFullName());
			}

			csvWriter.writeHeader(headers.toArray(new String[0]));
		}

		List<String> values = new ArrayList<String>();
		for (DataSetRow row : dataset) {
			int maxRowSize = row.getMaxSize();
			for (int i = 0; i < maxRowSize; i++) {
				// input parameters
				for (ParameterValue pv : row.getInputRowValues()) {
					addValueToList(values, pv);
				}

				// observation parameters
				for (ParameterValueList pvl : row.getObservableRowValues()) {
					if (i >= pvl.getSize()) {
						values.add("");
					} else {
						Object value = pvl.getValues().get(i);
						if (decimalDelimiter != '.' && value instanceof Double) {
							values.add(value.toString().replace('.', decimalDelimiter));

						} else {
							values.add(value.toString());
						}
					}
				}

				csvWriter.write(values.toArray(new String[0]));
				values.clear();
			}
		}

		csvWriter.close();
	}

	private void addValueToList(List<String> values, ParameterValue pv) {
		if (decimalDelimiter != '.' && pv.getValue() instanceof Double) {
			values.add(pv.getValue().toString().replace('.', decimalDelimiter));

		} else {
			values.add(pv.getValue().toString());
		}
	}
}
