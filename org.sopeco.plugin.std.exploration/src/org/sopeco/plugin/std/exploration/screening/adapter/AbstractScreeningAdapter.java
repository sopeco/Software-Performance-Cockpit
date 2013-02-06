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

import java.util.ArrayList;
import java.util.List;

import org.sopeco.analysis.wrapper.AnalysisWrapper;
import org.sopeco.analysis.wrapper.exception.AnalysisWrapperException;
import org.sopeco.engine.experimentseries.IParameterVariation;
import org.sopeco.persistence.dataset.ParameterValue;
import org.sopeco.persistence.entities.definition.ExplorationStrategy;
import org.sopeco.plugin.std.exploration.screening.config.ScreeningConfiguration;
import org.sopeco.plugin.std.exploration.screening.container.ExpDesign;
import org.sopeco.plugin.std.exploration.screening.container.ExpDesignType;
import org.sopeco.plugin.std.exploration.screening.container.ParameterFactorValues;

/**
 * Abstract adapter used to provide a basic structure for different type of
 * screening adapters.
 * 
 * @author Pascal Meier
 * @author Dennis Westermann
 */
public abstract class AbstractScreeningAdapter implements IScreeningAdapter {

	/**
	 * The generated screening design.
	 */
	protected ExpDesign expDesign = null;
	/**
	 * List of ParameterValues for parameters that won't be explored by the
	 * design but are necessary for executing measurements.
	 */
	protected List<ParameterValue<?>> valuesOfConstantParams;

	/**
	 * Analysis wrapper used for statistical operations.
	 */
	protected AnalysisWrapper analysisWrapper;

	/**
	 * Constructor.
	 */
	public AbstractScreeningAdapter() {
		expDesign = new ExpDesign();
		valuesOfConstantParams = new ArrayList<ParameterValue<?>>();

		try {
			analysisWrapper = new AnalysisWrapper();
		} catch (AnalysisWrapperException e) {
			throw new RuntimeException(e);
		}

	}

	@Override
	public final void setParameterVariations(List<IParameterVariation> variationImplementations) {
		valuesOfConstantParams.clear();

		for (IParameterVariation iter : variationImplementations) {
			if (iter.size() > 1) {
				ParameterFactorValues factorValues = new ParameterFactorValues(iter.get(0), iter.get(iter.size() - 1),
						iter.getParameter());
				expDesign.addParameter(iter.getParameter());
				expDesign.addFactorValuesByParameter(iter.getParameter(), factorValues);
			} else if (iter.size() == 1) {
				valuesOfConstantParams.add(iter.get(0));
			} else {
				throw new IllegalStateException(
						"Screening explorations require parameter variations with a size of at least 1.");
			}
		}
	}

	@Override
	public final void setExplorationConf(ExplorationStrategy expConf) {
		checkExplorationConfType(expConf);
		expDesign.setUseReplication(ScreeningConfiguration.useReplication(expConf));
		expDesign.setNumberOfReplications(ScreeningConfiguration.getNumberOfReplications(expConf));
		expDesign.setRandomizeRuns(ScreeningConfiguration.randomizeRuns(expConf));
		if (expDesign.isUseReplication() && expDesign.getNumberOfReplications() <= 1) {
			expDesign.setUseReplication(false);
			throw new IllegalStateException("numberOfReplications has to be larger than 1."
					+ "No replications will be executed!");
		}
		setSpecialExplorationConfParams(expConf);
	}

	/**
	 * Used to set adapter-dependent additional configuration parameters that
	 * define how the adapter generates the design.
	 * 
	 * @param expConf
	 *            Screening configuration
	 */
	protected abstract void setSpecialExplorationConfParams(ExplorationStrategy expConf);

	/**
	 * Used to test if the provided exploration configuration matches the used
	 * implementation of the ScreeningAdapter.
	 * 
	 * @param expConf
	 */
	protected abstract void checkExplorationConfType(ExplorationStrategy expConf);

	@Override
	public void generateDesign() {
		try {
			loadRLibraries();
			executeRCommandAndGetDesign();
			expDesign.setResolution(getDesignResolutionFromR());
		} catch (AnalysisWrapperException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Loads the R libraries needed to generate the design.
	 * @throws AnalysisWrapperException 
	 */
	protected abstract void loadRLibraries() throws AnalysisWrapperException;

	/**
	 * Reads the resolution of the generated design from R.
	 * @throws AnalysisWrapperException 
	 */
	protected abstract int getDesignResolutionFromR() throws AnalysisWrapperException;

	/**
	 * Executes the RCommand to generate a design and retrieves it from R.
	 * 
	 * @throws AnalysisWrapperException
	 * 
	 */
	protected final void executeRCommandAndGetDesign() throws AnalysisWrapperException {

		String rCommand = buildRCommand();
		analysisWrapper.executeCommandString(rCommand);
		getAllRunLevelsFromR();
		expDesign.setType(getDesignTypeFromR());
		expDesign.updateNumberOfRuns();

	}

	/**
	 * Reads the type of the generated design from R. Depending on the
	 * configuration provided by the analyst, sometimes a full factorial design
	 * is generated instead of a fractional factorial design. This is the case,
	 * if the number of parameters is small compared to the number of allowed
	 * measurements or if the resolution is very high compared to the number of
	 * parameters. In the second case, all possible interactions are not
	 * confounded/aliased and to achieve this a full factorial design is
	 * generated.
	 * 
	 * @return type of the generated design
	 * @throws AnalysisWrapperException
	 */
	private ExpDesignType getDesignTypeFromR() throws AnalysisWrapperException {
		String rCommand = "design.info(curDesign)$type";
		String type = analysisWrapper.executeCommandString(rCommand);
		if (type.contains("FrF2")) {
			return ExpDesignType.FRACTIONAL;
		} else if (type.contains("full factorial")) {
			return ExpDesignType.FULL;
		} else if (type.contains("pb")) { // should not occur
			return ExpDesignType.PLACKETTB;
		} else {
			throw new IllegalStateException("Unknow design type.");
		}
	}

	/**
	 * Reads all run levels of all parameters from R and stores them in the
	 * design.
	 * @throws AnalysisWrapperException 
	 * 
	 * @throws FrameworkException
	 */
	protected abstract void getAllRunLevelsFromR() throws AnalysisWrapperException;

	/**
	 * Builds the RCommand used to create the screening design with R.
	 * 
	 * @return generated RCommand
	 */
	protected abstract String buildRCommand();

	@Override
	public final ExpDesign getExpDesign() {
		return expDesign;
	}

	@Override
	public List<ParameterValue<?>> getConstantParameterValues() {
		return valuesOfConstantParams;
	}

	@Override
	public void releaseAnalysisResources() {
		try {
			if (analysisWrapper != null) {
				analysisWrapper.shutdown();
				analysisWrapper = null;
			}
		} catch (AnalysisWrapperException e) {
			throw new RuntimeException(e);
		}
	}

}
