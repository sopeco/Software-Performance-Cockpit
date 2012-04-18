package org.sopeco.persistence.dataset.util;

import org.sopeco.configuration.parameter.ParameterDefinition;
import org.sopeco.configuration.parameter.ParameterRole;
import org.sopeco.configuration.parameter.ParameterType;
import org.sopeco.configuration.parameter.ParameterUsage;
import org.sopeco.configuration.parameter.impl.ParameterFactoryImpl;
import org.sopeco.configuration.parameter.impl.ParameterUsageImpl;

public class ParameterFactoryExtended extends ParameterFactoryImpl {



	public ParameterUsage createParameterUsage(String systemNodeId,
			String adapterInstanceId, String probeId, ParameterDefinition pDef) {
		ParameterUsage pu = super.createParameterUsage();
		((ParameterUsageImpl) pu).setId(createParameterUsageId(systemNodeId,
				adapterInstanceId, probeId, pDef.getID()));
		pu.setParameterDefinition(pDef);
		return pu;
	}



	public ParameterUsage createParameterUsage(String systemNodeId,
			String adapterInstanceId, String probeId, String name,
			ParameterType type, ParameterRole role) {
		ParameterDefinition pDef = createParameterDefinition(name, type, role);
		ParameterUsage pu = super.createParameterUsage();
		((ParameterUsageImpl) pu).setId(createParameterUsageId(systemNodeId,
				adapterInstanceId, probeId, pDef.getID()));
		pu.setParameterDefinition(pDef);
		return pu;
	}

	public ParameterDefinition createParameterDefinition(String name,
			ParameterType type, ParameterRole role) {
		ParameterDefinition pDef = super.createParameterDefinition();
		pDef.setName(name);
		pDef.setRole(role);
		pDef.setType(type);
		return pDef;
	}

	private String createParameterUsageId(String systemNodeId,
			String adapterInstanceId, String probeId, String parameterDefId) {
		return systemNodeId + ParameterUsageImpl.idSeparator
				+ adapterInstanceId + ParameterUsageImpl.idSeparator + probeId
				+ ParameterUsageImpl.idSeparator + parameterDefId;
	}
}
