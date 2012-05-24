package org.sopeco.plugin.std.exploration.screening.adapter;

import org.sopeco.persistence.entities.definition.ExplorationStrategy;
import org.sopeco.persistence.entities.definition.ParameterDefinition;
import org.sopeco.plugin.std.exploration.screening.config.ScreeningConfiguration;
import org.sopeco.plugin.std.exploration.screening.container.ParameterRunLevels;
import org.sopeco.plugin.std.exploration.screening.util.RAdapter;


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
		RAdapter.getWrapper().executeRCommandString("library(FrF2);");	
	}

	@Override
	protected void getAllRunLevelsFromR() {
		int i = 0;
		for (ParameterDefinition param : expDesign.getParameters()) {
					
			StringBuilder cmdBuilder = new StringBuilder();
			cmdBuilder.append("desnum(curDesign)[," + (i + 1) + "]");

			double[] factorLevelsOfParam = RAdapter.getWrapper().executeRCommandDoubleArray(cmdBuilder.toString());
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
		String result = RAdapter.getWrapper().executeRCommandString(cmdBuilder.toString());
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
