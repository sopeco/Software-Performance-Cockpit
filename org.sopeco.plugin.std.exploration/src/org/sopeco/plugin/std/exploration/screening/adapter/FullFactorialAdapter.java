package org.sopeco.plugin.std.exploration.screening.adapter;

import org.sopeco.persistence.entities.definition.ExplorationStrategy;
import org.sopeco.persistence.entities.definition.ParameterDefinition;
import org.sopeco.plugin.std.exploration.screening.config.ScreeningConfiguration;
import org.sopeco.plugin.std.exploration.screening.container.ParameterRunLevels;
import org.sopeco.plugin.std.exploration.screening.util.RAdapter;


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

			double[] factorLevelsOfParam = RAdapter.getWrapper().executeCommandDoubleArray(cmdBuilder.toString());
			RAdapter.shutDown();
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
		RAdapter.getWrapper().executeCommandString("library(FrF2);");	
		RAdapter.shutDown();
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
