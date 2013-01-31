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
 * Adapter used to generate a PlackettBurman screening design with R.
 * 
 * @author Pascal Meier
 * @author Dennis Westermann
 */
public class PlackettBurmanAdapter extends AbstractScreeningAdapter {

	/**
	 * Constructor.
	 */
	public PlackettBurmanAdapter() {
		super();
	}
	
	@Override
	protected void loadRLibraries() {
		analysisWrapper.executeCommandString("library(FrF2);");

	}
	
	/**
	 * Calculates the number of runs needed by a PlackettBurman design.
	 */
	private int calculateNumberOfRunsByNumberOfParameters(int numberOfParameters) {
		// Determine the number of runs needed. Minimum is 8.
		// -> see doc of R-package
		// Has to be a multiple of 4.
		int numberOfRuns = 8;
		if (numberOfParameters > 7) {
			// Calculate multiple of 4 greater than number of parameters
			numberOfRuns = 4 * ((Double)Math.ceil((((Integer)numberOfParameters).doubleValue()/4.0))).intValue();
			if(numberOfRuns == numberOfParameters) {
				numberOfRuns += 4;
			}
		}	
		
		return numberOfRuns;
	}

	@Override
	protected void getAllRunLevelsFromR() {
		int i = 1;
		for (ParameterDefinition param : expDesign.getParameters()) {
					
			StringBuilder cmdBuilder = new StringBuilder();
			cmdBuilder.append("desnum(curDesign)[," + i + "]");
			i++;
			
			double[] factorLevelsOfParam = analysisWrapper.executeCommandDoubleArray(cmdBuilder.toString());

			ParameterRunLevels runLevels = new ParameterRunLevels(param);
			for (double value : factorLevelsOfParam) {
				runLevels.addRunLevel(((Double) value).intValue());
			}
			expDesign.addRunLevelsByParameter(param, runLevels);
		}
	}

	@Override
	protected String buildRCommand() {
		StringBuilder cmdBuilder = new StringBuilder();
		cmdBuilder.append(
				"curDesign <- pb(nruns=" + 
				calculateNumberOfRunsByNumberOfParameters(expDesign.getNumberOfParameters())
			  );
	//	cmdBuilder.append(", nfactors=" + (expDesign.getNumberOfParameters()));
		cmdBuilder.append(", nfactors=" + (calculateNumberOfRunsByNumberOfParameters(expDesign.getNumberOfParameters()) - 1));
		
		cmdBuilder.append(", ncenter=0, ");
		
		if (expDesign.isUseReplication()) {
			cmdBuilder.append("replications=" + expDesign.getNumberOfReplications() + ", ");
		} else {
			cmdBuilder.append("replications=1, ");
		}
		
		cmdBuilder.append("repeat.only=FALSE, ");
		
		if (expDesign.isRandomizeRuns()) {
			cmdBuilder.append("randomize=TRUE");
		} else {
			cmdBuilder.append("randomize=FALSE");
		}
		
		cmdBuilder.append(", factor.names=list(");
	
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
	protected void checkExplorationConfType(ExplorationStrategy expConf) {
		if (!(expConf.getName().equalsIgnoreCase(ScreeningConfiguration.PLACKETT_BURMAN)) ) {
			throw new IllegalStateException("Configuration is not of type PlackettBurman.");
		}		
	}

	@Override
	protected void setSpecialExplorationConfParams(ExplorationStrategy expConf) {
		// nothing to do	
	}
	
	@Override
	protected int getDesignResolutionFromR() {
		return -1; // not applicable
	}
	
}
