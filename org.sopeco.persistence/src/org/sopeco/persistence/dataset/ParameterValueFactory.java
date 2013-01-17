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
package org.sopeco.persistence.dataset;

import org.sopeco.persistence.dataset.util.ParameterType;
import org.sopeco.persistence.dataset.util.ParameterUtil;
import org.sopeco.persistence.entities.definition.ParameterDefinition;

/**
 * Factory for creation of ParameterValues.
 * @author Alexander Wert
 *
 */
public final class ParameterValueFactory {
	
	private ParameterValueFactory() {
	}
	
	private static final double SMALL_NUMBER = 0.0000001;

	public static <T extends Object> ParameterValue<T> createParameterValue(ParameterDefinition parameter, T value) {
		Object convertedValue = convertValue(value, ParameterUtil.getTypeEnumeration(parameter.getType()));
		if (convertedValue instanceof Double) {
			return (ParameterValue<T>) new ParameterValue<Double>(parameter, (Double) convertedValue);
		} else if (convertedValue instanceof Integer) {
			return (ParameterValue<T>) new ParameterValue<Integer>(parameter, (Integer) convertedValue);
		} else if (convertedValue instanceof Boolean) {
			return (ParameterValue<T>) new ParameterValue<Boolean>(parameter, (Boolean) convertedValue);
		} else if (convertedValue instanceof String) {
			return (ParameterValue<T>) new ParameterValue<String>(parameter, (String) convertedValue);
		} else {
			throw new IllegalArgumentException("Invalud value type for the creation of parameter value!");
		}
	}

	public static Object convertValue(Object value, String typeName) {
		ParameterType type = ParameterUtil.getTypeEnumeration(typeName);
		return convertValue(value, type);
	}

	public static Object convertValue(Object value, ParameterType type) {
		switch (type) {
		case BOOLEAN:
			if (value instanceof Boolean) {
				return value;
			} else if (value instanceof Double) {
				return doubleToBoolean((Double) value);
			} else if (value instanceof Integer) {
				return integerToBoolean((Integer) value);
			} else if (value instanceof String) {
				return stringToBoolean((String) value);
			}
			break;
		case DOUBLE:
			if (value instanceof Double) {
				return value;
			} else if (value instanceof Boolean) {
				return booleanToDouble((Boolean) value);
			} else if (value instanceof Integer) {
				return ((Integer) value).doubleValue();
			} else if (value instanceof String) {
				return Double.parseDouble((String) value);
			}
			break;
		case INTEGER:
			if (value instanceof Integer) {
				return value;
			} else if (value instanceof Boolean) {
				return booleanToInteger((Boolean) value);
			} else if (value instanceof Double) {
				return ((Double) value).intValue();
			} else if (value instanceof String) {
				return Integer.parseInt((String) value);
			}
			break;
		case STRING:
			return (String) value;
		default:
			break;
		}
		throw new IllegalArgumentException("Cannot convert object '" + value + "' to the required value type!");
	}

	private static Boolean doubleToBoolean(Double d) {
		return d >= SMALL_NUMBER;

	}

	private static Boolean integerToBoolean(Integer i) {
		return i > 0;
	}

	private static Boolean stringToBoolean(String s) {
		if (s.equalsIgnoreCase("FALSE") || s.equalsIgnoreCase("0")) {
			return false;
		} else if (s.equalsIgnoreCase("TRUE") || s.equalsIgnoreCase("1")) {
			return true;
		} else {
			throw new IllegalArgumentException("Cannot convert string '" + s + "' to a boolean value!");
		}
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
