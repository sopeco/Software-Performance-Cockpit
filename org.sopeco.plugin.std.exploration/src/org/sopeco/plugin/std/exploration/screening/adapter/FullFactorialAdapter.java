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
package org.sopeco.plugin.std.exploration.screening.adapter;

import org.sopeco.persistence.entities.definition.ExplorationStrategy;
import org.sopeco.persistence.entities.definition.ParameterDefinition;
import org.sopeco.plugin.std.exploration.screening.config.ScreeningConfiguration;
import org.sopeco.plugin.std.exploration.screening.container.ParameterRunLevels;


/**
 * Adapter used to generate a full factorial design with R.
 * 
 * @author Pascal Meier
 * @author Dennis Westermann
 * 
 */

public class FullFactorialAdapter extends AbstractScreeningAdapter {

	/**
	 * Constructor.
	 */
	public FullFactorialAdapter() {
		super();
	}	

	@Override
	protected void checkExplorationConfType(ExplorationStrategy expConf) {
		
		if (!(expConf.getName().equalsIgnoreCase(ScreeningConfiguration.FULL_FACTORIAL))) {
			throw new IllegalStateException("Configuration is not of type FullFactorial.");
		}	
	}
		
	/**
	 * Reads all run levels of all parameters from R and stores them in the design.
	 * Additionally center points have to be added manually if they are desired.
	 * 
	 */
	protected void getAllRunLevelsFromR() {
		int i = 0;
		for (ParameterDefinition param : expDesign.getParameters()) {
					
			StringBuilder cmdBuilder = new StringBuilder();
			cmdBuilder.append("desnum(curDesign)[," + (i + 1) + "]");

			double[] factorLevelsOfParam = analysisWrapper.executeCommandDoubleArray(cmdBuilder.toString());
			ParameterRunLevels runLevels = new ParameterRunLevels(param);
						 
			for (double value : factorLevelsOfParam) {
				runLevels.addRunLevel(((Double) value).intValue());
			}
			
			expDesign.addRunLevelsByParameter(param, runLevels);
			
			i++;
		}
	}

	
	@Override
	protected String buildRCommand() {
		
		StringBuilder cmdBuilder = new StringBuilder();
		cmdBuilder.append("curDesign <- fac.design(");
		
		cmdBuilder.append("nfactors=" + (expDesign.getNumberOfParameters()) + ", ");
		
		if (expDesign.isUseReplication()) {
			cmdBuilder.append("replications=" + expDesign.getNumberOfReplications() + ", ");
		} else {
			cmdBuilder.append("replications=1, ");
		}
		
		cmdBuilder.append("repeat.only=FALSE, ");
		
		if (expDesign.isRandomizeRuns()) {
			cmdBuilder.append("randomize=TRUE, ");
		} else {
			cmdBuilder.append("randomize=FALSE, ");
		}

		cmdBuilder.append("nlevels=c(");
	
		for (int i = 0; i < expDesign.getParameters().size(); i++) {
			
			if (i != 0) {
				cmdBuilder.append(", ");	
			}
			cmdBuilder.append("2");
		}				
		cmdBuilder.append("), ");
		
		cmdBuilder.append("factor.names=list(");
		int i = 0;
		for (ParameterDefinition param : expDesign.getParameters()) {
				
			if (i != 0) {
				cmdBuilder.append(", ");	
			}
			cmdBuilder.append("\"" + param.getFullName("_") + "\"=c(-1,1)");
			
			i++;
		}				
		cmdBuilder.append("));");
		
		return cmdBuilder.toString();
	}

	@Override
	protected void loadRLibraries() {
		analysisWrapper.executeCommandString("library(FrF2);");	
	}

	@Override
	protected void setSpecialExplorationConfParams(ExplorationStrategy expConf) {
		// nothing to to		
	}

	@Override
	protected int getDesignResolutionFromR() {
		return -1; // not applicable
	}
		
}
