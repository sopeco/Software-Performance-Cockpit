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

import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVStrategy;
import org.sopeco.persistence.dataset.ParameterValue;
import org.sopeco.persistence.dataset.SimpleDataSet;
import org.sopeco.persistence.dataset.SimpleDataSetColumn;
import org.sopeco.persistence.dataset.SimpleDataSetRow;

/**
 * 
 * The CsvHandler manages the storage of DataSets as CSV files.
 * 
 * @author Jens Happe
 * 
 */
public class DataSetCsvHandler {

	/**
	 * Column names are used in the CSV files.
	 */
	private boolean hasColumnNames;

	/**
	 * The used decimal delimiter.
	 */
	private char decimalDelimiter;

	/**
	 * Strategy for storing CSV files.
	 */
	private CSVStrategy strategy;

	/**
	 * Constructor.
	 * 
	 * @param valueSeparator
	 *            Character to be used for separation in the file.
	 * @param commentSeparator
	 *            Character to be used to mark comments in the file.
	 * @param hasColumnNames
	 *            Column names are to be stored in the file.
	 */
	public DataSetCsvHandler(char valueSeparator, char commentSeparator,
			boolean hasColumnNames) {
		this(valueSeparator, commentSeparator, '.', hasColumnNames);
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
	public DataSetCsvHandler(char valueSeparator, char commentSeparator,
			char decimalDelimiter, boolean hasColumnNames) {
		strategy = new CSVStrategy(valueSeparator, '%', commentSeparator);
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
	@SuppressWarnings("unchecked")
	public void store(SimpleDataSet dataset, String fileName)
			throws IOException {
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
	 * Converts the the passed {@link SimpleDataSet} to a CSV stream and writes
	 * the stream to the passed writer.
	 */
	private void writeCSV(SimpleDataSet dataset, Writer writer)
			throws IOException {
		CSVPrinter printer = new CSVPrinter(writer);
		printer.setStrategy(strategy);

		if (hasColumnNames) {
			for (SimpleDataSetColumn column : dataset.getColumns()) {
				printer.print(column.getParameter().getName());
			}
			printer.println();
		}

		for (SimpleDataSetRow row : dataset) {
			for (ParameterValue pv : row.getRowValues()) {
				if (decimalDelimiter != '.' && pv.getValue() instanceof Double) {
					printer.print(pv.getValue().toString()
							.replace('.', decimalDelimiter));
				} else {
					printer.print(pv.getValue().toString());
				}
			}
			printer.println();
		}

	}
}
