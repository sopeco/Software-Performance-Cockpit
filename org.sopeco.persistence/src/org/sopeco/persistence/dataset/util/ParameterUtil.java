package org.sopeco.persistence.dataset.util;

/**
 * This class holds utility methods for Parameters.
 * 
 * @author Dennis Westermann
 * 
 */
public final class ParameterUtil {
	private ParameterUtil() {
	}

	/**
	 * Converts a given type string in a corresponding enumeration object.
	 * 
	 * @param typeName
	 *            name of the type in a string representation
	 * @return an enumeration instance that corresponds to the given type string
	 */
	public static ParameterType getTypeEnumeration(String typeName) {
		if (typeName.equalsIgnoreCase(ParameterType.DOUBLE.name())) {
			return ParameterType.DOUBLE;
		} else if (typeName.equalsIgnoreCase(ParameterType.INTEGER.name())) {
			return ParameterType.INTEGER;
		} else if (typeName.equalsIgnoreCase(ParameterType.STRING.name())) {
			return ParameterType.STRING;
		} else if (typeName.equalsIgnoreCase(ParameterType.BOOLEAN.name())) {
			return ParameterType.BOOLEAN;
		} else {
			throw new IllegalArgumentException("Invalid parameter type: " + typeName);
		}

	}

}
