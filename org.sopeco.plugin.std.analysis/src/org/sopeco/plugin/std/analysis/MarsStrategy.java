package org.sopeco.plugin.std.analysis;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sopeco.engine.analysis.IPredictionFunctionResult;
import org.sopeco.engine.analysis.IPredictionFunctionStrategy;
import org.sopeco.engine.analysis.PredictionFunctionResult;
import org.sopeco.engine.registry.ISoPeCoExtension;
import org.sopeco.persistence.dataset.DataSetAggregated;
import org.sopeco.persistence.dataset.ParameterValue;
import org.sopeco.persistence.dataset.SimpleDataSet;
import org.sopeco.persistence.dataset.SimpleDataSetRow;
import org.sopeco.persistence.dataset.SimpleDataSetRowBuilder;
import org.sopeco.persistence.entities.definition.AnalysisConfiguration;
import org.sopeco.plugin.std.analysis.common.AbstractAnalysisStrategy;
import org.sopeco.plugin.std.analysis.util.RAdapter;
import org.sopeco.util.Tools;

/**
 * This analysis strategy allows deriving Multivariate Adaptive Regression
 * Splines (MARS) in R.
 * 
 * @author Dennis Westermann, Jens Happe
 * 
 */
public class MarsStrategy extends AbstractAnalysisStrategy implements IPredictionFunctionStrategy {

	Logger logger = LoggerFactory.getLogger(MarsStrategy.class);

	/**
	 * Instantiate a new MARS Analysis for R.
	 */
	public MarsStrategy(ISoPeCoExtension<?> provider) {
		super(provider);
		requireLibrary("leaps");
		requireLibrary("earth");
		loadLibraries();
	}

	@Override
	public void analyse(DataSetAggregated dataset, AnalysisConfiguration config) {

		logger.debug("Starting MARS analysis.");

		deriveDependentAndIndependentParameters(dataset, config);

		DataSetAggregated analysisDataSet = extractAnalysisDataSet(dataset);
		
		loadDataSetInR(createValidSimpleDataSet(analysisDataSet));
		
		StringBuilder cmdBuilder = new StringBuilder();
		cmdBuilder.append(getId());
		cmdBuilder.append(" <- earth(");
		cmdBuilder.append(dependentParameterDefintion.getFullName("_"));
		cmdBuilder.append(" ~ . , data =");
		cmdBuilder.append(data.getId());
		cmdBuilder.append(", penalty=-1,  fast.k=0, degree=2)");
		logger.debug("Running R Command: {}", cmdBuilder.toString());
		RAdapter.getWrapper().executeRCommandString(cmdBuilder.toString());

	}

	@Override
	public IPredictionFunctionResult getPredictionFunctionResult() {
		return new PredictionFunctionResult(getFunctionAsString(), dependentParameterDefintion, config);
	}

	/**
	 * @return the analysis result as a function string that conforms to the
	 *         JavaScript syntax
	 */
	private String getFunctionAsString() {
		String fs = RAdapter.getWrapper().executeRCommandString("cat(format(" + getId() + ", style=\"max\"))");
		fs = fs.replace("\n ", "");
		fs = fs.replace("+  ", "+ ");
		fs = fs.replace("-  ", "- ");
		fs = fs.replace("max", "Math.max");
		fs = fs.replaceAll("\\s\\s+", " "); // remove double blanks
		return fs;
	}

	/**
	 * Ensures that the dataset contains at least 8 rows. This is the minimum
	 * number required by the MARS implementation. If the dataset contains less
	 * than 8 rows the existing rows are duplicated.
	 * 
	 * @param dataSet the dataset passed to the analysis strategy
	 * @return a {@link SimpleDataSet} that contains at least 8 rows
	 */
	private SimpleDataSet createValidSimpleDataSet(DataSetAggregated dataSet) {
		SimpleDataSet givenDataSet = dataSet.convertToSimpleDataSet();
		
		SimpleDataSetRowBuilder rb = new SimpleDataSetRowBuilder();
		while (rb.size() < 8) {
			
			for(SimpleDataSetRow row : givenDataSet.getRowList()){
				rb.startRow();
				for (ParameterValue<?> pv : row.getRowValues()){
					
					if(pv.getParameter().equals(dependentParameterDefintion)) {
						// scatter due to Mars Error "cannot scale y (values are all equal to ..)"
						Object newValue;
						switch(Tools.SupportedTypes.get(pv.getParameter().getType())){
						case Double:
							Random r = new Random();
							newValue = pv.getValueAsDouble() * (1.0001 + 0.0001 * r.nextDouble());
							break;
						case Integer:
							newValue = pv.getValueAsInteger() + 1;
							break;
						default:
							throw new IllegalArgumentException("Unsopported parameter type: " + pv.getParameter().getType());
						}
						pv.setValue(newValue);
					}
					
					rb.addParameterValue(pv.getParameter(), pv.getValue());
				}
				rb.finishRow(); 
			}
			
		}
		return rb.createDataSet();
	}
}
