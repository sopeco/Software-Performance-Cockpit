package org.sopeco.persistence.dataset.util;

public class ParameterUtil {

	
	public static ParameterType getTypeEnumeration(String typeName){
		if (typeName.equalsIgnoreCase(ParameterType.DOUBLE.getName())){
			return ParameterType.DOUBLE;
		} else if (typeName.equalsIgnoreCase(ParameterType.INTEGER.getName())){
			return ParameterType.INTEGER;
		} else if (typeName.equalsIgnoreCase(ParameterType.STRING.getName())){
			return ParameterType.STRING;
		} else if (typeName.equalsIgnoreCase(ParameterType.BOOLEAN.getName())){
			return ParameterType.BOOLEAN;
		} else {
			throw new IllegalArgumentException(
					"Invalid parameter type: " + typeName);
		}
		
	}
	
	public static ParameterRole getRoleEnumeration(String roleName){
		if (roleName.equalsIgnoreCase(ParameterType.DOUBLE.getName())){
			return ParameterRole.INPUT;
		} else if (roleName.equalsIgnoreCase(ParameterRole.INPUT.getName())){
			return ParameterRole.OBSERVATION;
		} else if (roleName.equalsIgnoreCase(ParameterRole.OBSERVATION.getName())){
			return ParameterRole.OBSERVABLE_TIME_SERIES;
		} else {
			throw new IllegalArgumentException(
					"Invalid parameter role/direction: " + roleName);
		}
		
	}
}
