package org.sopeco.persistence.dataset.util;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.sopeco.configuration.parameter.ParameterRole;
import org.sopeco.configuration.parameter.ParameterUsage;
import org.sopeco.persistence.dataset.AbstractDataSetColumn;
import org.sopeco.persistence.dataset.DataSetAggregated;
import org.sopeco.persistence.dataset.DataSetInputColumn;
import org.sopeco.persistence.dataset.DataSetObservationColumn;
import org.sopeco.persistence.dataset.ParameterValue;

public class DataSetModifier {
	private DataSetAggregated dataset;

	public DataSetModifier(DataSetAggregated dataset) {
		this.dataset = dataset;
	}

	public DataSetModifier() {
		this.dataset = new DataSetAggregated(
				new ArrayList<DataSetInputColumn>(),
				new ArrayList<DataSetObservationColumn>(), 0, UUID.randomUUID()
						.toString());
	}

	public AbstractDataSetColumn<?> addColumn(ParameterUsage p) {
		AbstractDataSetColumn col = null;
		if (p.getRole().equals(ParameterRole.INPUT)) {
			col = new DataSetInputColumn(p, new ArrayList());
		} else if (p.getRole().equals(ParameterRole.OBSERVATION) || p.getRole().equals(ParameterRole.OBSERVABLE_TIME_SERIES)) {
			col = new DataSetObservationColumn(p, new ArrayList());
		}
		dataset.addColumn(col);
		return col;
	}

//	public AbstractDataSetColumn<?> addColumn(ParameterUsage p,
//			List<Parameter> values) {
//		AbstractDataSetColumn col = null;
//		if (p.getRole().equals(ParameterRole.INPUT)) {
//			col = new DataSetInputColumn(p, values);
//		} else if (p.getRole().equals(ParameterRole.OBSERVATION)) {
//			if (!values.isEmpty()
//					&& (values.get(0) instanceof ParameterValueList)) {
//				col = new DataSetObservationColumn(p, values);
//			} else {
//				throw new IllegalArgumentException(
//						"For observation parameters a value list must comprise parameterValueLists!");
//			}
//
//		}
//		dataset.addColumn(col);
//		return col;
//	}

	// public DataSetInputColumn<?> addInputColumn(ParameterUsage p,
	// List<ParameterValue<?>> values) {
	// List<Object> valueList = new ArrayList<Object>();
	// for (ParameterValue<?> pv : values) {
	// valueList.add(pv.getValue());
	// }
	//
	// DataSetInputColumn col = new DataSetInputColumn(p, valueList);
	//
	// dataset.addColumn(col);
	// return col;
	// }
	//
	// public DataSetObservationColumn<?> addObservationColumn(ParameterUsage p,
	// List<ParameterValueList<?>> values) {
	// List<ParameterValueList<?>> valueList = new
	// ArrayList<ParameterValueList<?>>();
	// for (ParameterValueList<?> pvl : values) {
	// valueList.add(pvl);
	// }
	//
	// DataSetObservationColumn col = new DataSetObservationColumn(p,
	// valueList);
	//
	// dataset.addColumn(col);
	// return col;
	// }

//	public void addValue(ParameterUsage p, Object value) {
//		if (p.getRole().equals(ParameterRole.INPUT)) {
//			dataset.getInputColumn(p).addValue(value);
//
//		} else {
//			dataset.getObservationColumn(p).addValue(value);
//		}
//		int max = -1;
//
//		for (AbstractDataSetColumn<?> col : dataset.getColumns()) {
//			if (col.size() > max) {
//				max = col.size();
//			}
//		}
//
//		dataset.setSize(max);
//	}

	public AbstractDataSetColumn<?> getColumn(ParameterUsage p) {
		try {
			return dataset.getColumn(p);
		} catch (Exception e) {
			return null;
		}

	}

	/**
	 * Adds all columns of the given dataSet to the existing dataSet. The
	 * dataSet must not contain parameters of the existing dataSet.
	 * 
	 * @param newDataSet
	 *            - the dataSet that should be merged with the existing dataSet
	 */
	public void mergeDataSet(DataSetAggregated newDataSet) {
		for (AbstractDataSetColumn col : newDataSet.getColumns()) {
			if (dataset.contains(col.getParameter())) {
				throw new IllegalStateException(
						"Trying to merge two data sets that include "
								+ "the same parameter: "
								+ col.getParameter().getName());
			} else {
				dataset.addColumn(col);
			}
		}
	}

	public DataSetAggregated getDataSet() {
		return dataset;
	}
}
