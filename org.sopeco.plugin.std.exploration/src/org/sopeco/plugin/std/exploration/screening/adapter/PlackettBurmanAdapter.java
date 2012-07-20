package org.sopeco.plugin.std.exploration.screening.adapter;

import org.sopeco.persistence.entities.definition.ExplorationStrategy;
import org.sopeco.persistence.entities.definition.ParameterDefinition;
import org.sopeco.plugin.std.exploration.screening.config.ScreeningConfiguration;
import org.sopeco.plugin.std.exploration.screening.container.ParameterRunLevels;
import org.sopeco.plugin.std.exploration.screening.util.RAdapter;

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
		RAdapter.getWrapper().executeCommandString("library(FrF2);");
		RAdapter.shutDown();
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
			
			double[] factorLevelsOfParam = RAdapter.getWrapper().executeCommandDoubleArray(cmdBuilder.toString());
			RAdapter.shutDown();
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
