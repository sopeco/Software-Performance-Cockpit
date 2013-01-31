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
 * Adapter used to generate a fractional factorial design with R.
 * 
 * @author Pascal Meier
 * @author Dennis Westermann
 */
public class FractionalFactorialAdapter extends AbstractScreeningAdapter {

	/**
	 * Indicates if the number of runs or the resolution (both specified in the 
	 * exploration configuration) is used to create the design.
	 */
	private boolean useNumRunsInsteadRes;
	/**
	 * Number of runs the design will have.
	 */
	private int numberOfDesiredRuns;
	/**
	 * Resolution that the design should have according to the configuration.
	 */
	private int desiredResolution;
	
	/**
	 * Constructor.
	 */
	public FractionalFactorialAdapter() {
		super();
	}
	
	@Override
	protected void checkExplorationConfType(ExplorationStrategy expConf) {
		
		if (!(expConf.getName().equalsIgnoreCase(ScreeningConfiguration.FRACTIONAL_FACTORIAL))) {
			throw new IllegalStateException("Configuration is not of type FractionalFactorial.");
		}	
		
	}

	@Override
	protected void setSpecialExplorationConfParams(ExplorationStrategy expConf) {
	
		if (ScreeningConfiguration.useNumRunsInsteadOfResolution(expConf)) {
			useNumRunsInsteadRes = true;	
			
			int numTemp = ScreeningConfiguration.getNumberOfRuns(expConf);
			while ((numTemp != 1) && (numTemp % 2 == 0)) {
				numTemp = numTemp / 2;
			}
			if (numTemp != 1) {
				throw new IllegalArgumentException("Number of runs has to be a power of 2.");
			}			
			
			if (ScreeningConfiguration.getNumberOfRuns(expConf) <= expDesign.getNumberOfParameters()) {
				throw new IllegalArgumentException("The number of runs has to be larger than the number of parameters.");
			}
			numberOfDesiredRuns = ScreeningConfiguration.getNumberOfRuns(expConf);
		} else {
			useNumRunsInsteadRes = false;
			if (ScreeningConfiguration.getResolution(expConf) < 3) {
				throw new IllegalArgumentException("The resolution of a experimental design has to be III or higher.");
			}
			desiredResolution = ScreeningConfiguration.getResolution(expConf);
		}
	}

	@Override
	protected void loadRLibraries() {
		analysisWrapper.executeCommandString("library(FrF2);");	
	}

	@Override
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
		cmdBuilder.append("curDesign <- FrF2(");
		if (useNumRunsInsteadRes) {
			cmdBuilder.append("nruns=" + numberOfDesiredRuns);
		} else {
			cmdBuilder.append("resolution=" + desiredResolution);
		}
		
		cmdBuilder.append(", nfactors=" + expDesign.getNumberOfParameters());
		
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
	protected int getDesignResolutionFromR() {
		StringBuilder cmdBuilder = new StringBuilder();
		cmdBuilder.append("print(as.character(design.info(curDesign)$catlg.entry))");
		String result = analysisWrapper.executeCommandString(cmdBuilder.toString());
		if (result.contains("STRING \"list(res = ")) {
			result = result.substring(result.indexOf("STRING \"list(res = ") + 19);
			result = result.substring(0, result.indexOf(","));
			int resolution = Integer.parseInt(result);
			if (resolution >= 3 && resolution <= 13) {
				return resolution;
			} else {
				return -1; // unknown
			}
		} else {
			return -1;	// unknown
		}
	}
	
}
