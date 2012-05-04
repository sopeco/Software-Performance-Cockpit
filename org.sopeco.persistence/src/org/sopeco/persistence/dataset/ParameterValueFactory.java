package org.sopeco.persistence.dataset;

import org.sopeco.persistence.dataset.util.ParameterType;
import org.sopeco.persistence.dataset.util.ParameterUtil;
import org.sopeco.persistence.entities.definition.ParameterDefinition;

public class ParameterValueFactory {
	private static final double SMALL_NUMBER = 0.0000001;

	public static ParameterValue<?> createParameterValue(
			ParameterDefinition parameter, Object value) {
		Object convertedValue = convertValue(value, ParameterUtil.getTypeEnumeration(parameter.getType()));
		if (convertedValue instanceof Double) {
			return new ParameterValue<Double>(parameter,
					(Double) convertedValue);
		} else if (convertedValue instanceof Integer) {
			return new ParameterValue<Integer>(parameter,
					(Integer) convertedValue);
		} else if (convertedValue instanceof Boolean) {
			return new ParameterValue<Boolean>(parameter,
					(Boolean) convertedValue);
		} else if (convertedValue instanceof String) {
			return new ParameterValue<String>(parameter,
					(String) convertedValue);
		} else {
			throw new IllegalArgumentException(
					"Invalud value type for the creation of parameter value!");
		}
	}
	
	public static Object convertValue(Object value, String typeName ){
		ParameterType type = ParameterUtil.getTypeEnumeration(typeName);
		return convertValue(value, type);
	}

	public static Object convertValue(Object value, ParameterType type) {
		switch (type) {
		case BOOLEAN:
			if (value instanceof Boolean)
				return value;
			else if (value instanceof Double)
				return doubleToBoolean((Double) value);
			else if (value instanceof Integer)
				return integerToBoolean((Integer) value);
			else if (value instanceof String)
				return stringToBoolean((String) value);
			break;
		case DOUBLE:
			if (value instanceof Double)
				return value;
			else if (value instanceof Boolean)
				return booleanToDouble((Boolean) value);
			else if (value instanceof Integer)
				return ((Integer) value).doubleValue();
			else if (value instanceof String)
				return Double.parseDouble((String) value);
			break;
		case INTEGER:
			if (value instanceof Integer)
				return value;
			else if (value instanceof Boolean)
				return booleanToInteger((Boolean) value);
			else if (value instanceof Double)
				return ((Double) value).intValue();
			else if (value instanceof String)
				return Integer.parseInt((String) value);
			break;
		case STRING:
			return value.toString();
		default:
			break;
		}
		throw new IllegalArgumentException("Cannot convert object '" + value
				+ "' to the required value type!");
	}

	private static Boolean doubleToBoolean(Double d) {
		if (d < SMALL_NUMBER)
			return false;
		else
			return true;
	}

	private static Boolean integerToBoolean(Integer i) {
		if (i <= 0)
			return false;
		else
			return true;
	}

	private static Boolean stringToBoolean(String s) {
		if (s.equalsIgnoreCase("FALSE") || s.equalsIgnoreCase("0"))
			return false;
		else if (s.equalsIgnoreCase("TRUE") || s.equalsIgnoreCase("1"))
			return true;
		else
			throw new IllegalArgumentException("Cannot convert string '" + s
					+ "' to a boolean value!");
	}

	private static Double booleanToDouble(Boolean b) {
		if (b) {
			return 1.0;
		} else {
			return 0.0;
		}
	}

	private static Integer booleanToInteger(Boolean b) {
		if (b) {
			return 1;
		} else {
			return 0;
		}
	}

}
