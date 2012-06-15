package org.sopeco.persistence.util;

import java.io.FileOutputStream;
import java.io.IOException;

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
	protected boolean hasColumnNames;

	/**
	 * Strategy for storing CSV files.
	 */
	protected CSVStrategy strategy;

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
	public DataSetCsvHandler(char valueSeparator, char commentSeparator, boolean hasColumnNames) {
		strategy = new CSVStrategy(valueSeparator, '%', commentSeparator);
		this.hasColumnNames = hasColumnNames;
	}

	// /**
	// * Load a given CSV file as DataSet. Please note that parameters used in
	// the
	// * file must be registered at the ParameterRegistry beforehand.
	// *
	// * @param fileName
	// * Name of the file to be loaded.
	// * @param id
	// * id of the DataSet to be loaded
	// * @return DataSet containing the CSV's data.
	// *
	// * @throws IOException
	// * If the file cannot be found or loaded.
	// */
	// public SimpleDataSet load(String fileName, String id) throws IOException
	// {
	// SimpleDataSetColumnBuilder builder = new SimpleDataSetColumnBuilder();
	// String[][] dataRecords = readCsvFile(fileName);
	// int numColumns = getNumColumns(dataRecords);
	//
	// for (int column = 0; column < numColumns; column++) {
	// ParameterDefinition parameter = getParameter(dataRecords, column);
	// builder.startColumn(parameter);
	// builder.addValueList(getValueList(dataRecords, column));
	// builder.finishColumn();
	// }
	//
	// return builder.createDataSet(id);
	// }
	//
	// /**
	// * Load a given CSV file as DataSet. Please note that parameters used in
	// the
	// * file must be registered at the ParameterRegistry beforehand. The ID of
	// * the DataSet is assumed to be the name of the file.
	// *
	// * @param fileName
	// * Name of the file to be loaded.
	// * @return DataSet containing the CSV's data.
	// *
	// * @throws IOException
	// * If the file cannot be found or loaded.
	// */
	// public SimpleDataSet load(String fileName) throws IOException {
	// String id = UUID.randomUUID().toString();
	// return load(fileName, id);
	// }
	//
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
	public void store(SimpleDataSet dataset, String fileName) throws IOException {
		FileOutputStream fo = new FileOutputStream(fileName);
		CSVPrinter printer = new CSVPrinter(fo);
		printer.setStrategy(strategy);

		if (hasColumnNames) {
			for (SimpleDataSetColumn column : dataset.getColumns()) {
				printer.print(column.getParameter().getName());
			}
			printer.println();
		}

		for (SimpleDataSetRow row : dataset) {
			for (ParameterValue pv : row.getRowValues()) {
				printer.print(pv.getValue().toString());
			}
			printer.println();
		}
		fo.close();
	}
	//
	// /**
	// * Get the values of a particular column from a dataRecord.
	// *
	// * @param dataRecord
	// * Data of the file loaded in String representation.
	// * @param column
	// * Column whose data is to be loaded.
	// * @return List of values of the column as type defined by the parameter.
	// */
	// @SuppressWarnings("unchecked")
	// protected List getValueList(String[][] dataRecord, int column) {
	// List valueList = new ArrayList();
	// int start = hasColumnNames ? 1 : 0;
	// ParameterUsage p = getParameter(dataRecord, column);
	//
	// for (int row = start; row < dataRecord.length; row++) {
	// valueList.add(getValue(dataRecord, row, column, p.getType()));
	// }
	// return valueList;
	// }
	//
	// /**
	// * Retrieve a particular value from a dataRecord with the correct type.
	// *
	// * @param dataRecord
	// * Data of the file loaded in String representation.
	// * @param row
	// * Row of interest.
	// * @param column
	// * Column of interest.
	// * @param type
	// * Type that is expected for the identified value.
	// * @return An instance of the value in the expected type.
	// */
	// protected Object getValue(String[][] dataRecord, int row, int column,
	// ParameterType type) {
	// String str = dataRecord[row][column];
	// switch (type) {
	// case DOUBLE:
	// return str == null || "".equals(str.trim()) ? new Double(0.0) :
	// Double.parseDouble(str);
	// case INTEGER:
	// return str == null || "".equals(str.trim()) ? new Integer(0) :
	// Integer.parseInt(str);
	// case BOOLEAN:
	// return str == null || "".equals(str.trim()) ? new Boolean(false) :
	// Boolean.parseBoolean(str);
	// case STRING:
	// return str == null ? "" : str;
	// default:
	// throw new IllegalArgumentException("Unknown ParameterType: " + type);
	// }
	// }
	//
	// /**
	// * Get the parameter that is associated with a particular column.
	// *
	// * @param dataRecords
	// * Data of the file loaded in String representation.
	// * @param column
	// * Column of interest.
	// *
	// * @return Parameter of the given column.
	// */
	// protected ParameterDefinition getParameter(String[][] dataRecords, int
	// column) {
	//
	// String name = hasColumnNames ? dataRecords[0][column] : "Column_" +
	// column;
	//
	// return findParameter(name);
	// }
	//
	// /**
	// * Determine the number of columns of the given data.
	// *
	// * @param dataRecords
	// * Data of the file loaded in String representation.
	// * @return Number of columns
	// */
	// protected int getNumColumns(String[][] dataRecords) {
	// if (dataRecords.length > 0) {
	// return dataRecords[0].length;
	// }
	// return 0;
	// }
	//
	// /**
	// * Read the complete CSV file into memory.
	// *
	// * @param fileName
	// * Name of the file.
	// * @return String representation of all values in the following
	// * representation: [rows][columns]
	// * @throws IOException
	// * Thrown if the specified file cannot be found.
	// *
	// */
	// protected String[][] readCsvFile(String fileName) throws IOException {
	// Reader reader = new FileReader(fileName);
	// return (new CSVParser(reader, strategy)).getAllValues();
	// }
	//
	// protected boolean isInteger(String value) {
	// try {
	// Integer.parseInt(value);
	// return true;
	// } catch (Exception e) {
	// return false;
	// }
	// }
	//
	// protected boolean isDouble(String value) {
	// try {
	// Double.parseDouble(value);
	// return true;
	// } catch (Exception e) {
	// return false;
	// }
	// }
	//
	// protected ParameterDefinition findParameter(String name) {
	// for (ParameterDefinition pd : scenario.getAllParameterUsages()) {
	// if (pd.getName().equals(name)) {
	// return pd;
	// }
	// }
	// return null;
	// }
}
