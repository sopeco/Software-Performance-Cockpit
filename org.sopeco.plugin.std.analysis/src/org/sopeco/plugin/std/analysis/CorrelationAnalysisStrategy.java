package org.sopeco.plugin.std.analysis;

import java.util.HashMap;

import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sopeco.engine.analysis.CorrelationResult;
import org.sopeco.engine.analysis.ICorrelationResult;
import org.sopeco.engine.analysis.ICorrelationStrategy;
import org.sopeco.engine.analysis.ParameterCorrelation;
import org.sopeco.engine.registry.ISoPeCoExtension;
import org.sopeco.persistence.dataset.DataSetAggregated;
import org.sopeco.persistence.dataset.SimpleDataSet;
import org.sopeco.persistence.dataset.SimpleDataSetColumn;
import org.sopeco.persistence.entities.definition.AnalysisConfiguration;
import org.sopeco.persistence.entities.definition.ParameterDefinition;
import org.sopeco.plugin.std.analysis.common.AbstractAnalysisStrategy;
import org.sopeco.util.Tools;

/**
 * This strategy allows to execute a correlation analysis in order to
 * determine the influence of configuration parameters on an observation
 * parameter.
 * 
 * @author Dennis Westermann
 */

public class CorrelationAnalysisStrategy extends AbstractAnalysisStrategy implements ICorrelationStrategy {

	Logger logger = LoggerFactory.getLogger(CorrelationAnalysisStrategy.class);
	
	private CorrelationResult resultOfLastAnalysis;
	
	public CorrelationAnalysisStrategy(ISoPeCoExtension<?> provider){
		super(provider);
	}
	
	@Override
	public void analyse(DataSetAggregated dataset,
			AnalysisConfiguration strategyConfig) {

		logger.debug("Starting correlation analysis.");
		
		deriveDependentAndIndependentParameters(dataset, strategyConfig);
		
		SimpleDataSet analysisDataSet = extractAnalysisDataSet(dataset).convertToSimpleDataSet();
		
		
		
		SimpleDataSetColumn<?> depParamCol = analysisDataSet.getColumn(dependentParameterDefintion);
		double[] depParamValues = convertDataSetColumnToDoubleArray(depParamCol);
		for(ParameterDefinition indepParam : independentParameterDefinitions) {
			SimpleDataSetColumn<?> col = analysisDataSet.getColumn(indepParam);
			double[] indepParamValues = convertDataSetColumnToDoubleArray(col);
			
			double[][] testData = new double[2][];
			testData[0] = depParamValues;
			testData[1] = indepParamValues;
			
			RealMatrix m = new BlockRealMatrix(indepParamValues.length, 2);
			m.setColumn(0, depParamValues);
			m.setColumn(1,indepParamValues);
			
			PearsonsCorrelation corr = new PearsonsCorrelation(m);
			double p = corr.getCorrelationPValues().getEntry(0, 1);
			if(Double.isNaN(p)){
				p=1.0;
			}
			double r = corr.getCorrelationMatrix().getEntry(0, 1);
			if(Double.isNaN(r)){
				r=0.0;
			}
			
			resultOfLastAnalysis  = new CorrelationResult(config);
			resultOfLastAnalysis.addParameterCorrelation(new ParameterCorrelation(indepParam, dependentParameterDefintion, r, p));
		}
		
		
	}

	
	private static double[] convertDataSetColumnToDoubleArray(SimpleDataSetColumn<?> col){
		double[] resultArray = new double[col.size()];
		String type = col.getParameter().getType();
		switch(Tools.SupportedTypes.get(type)){
		case Integer:
			// same as case double
		case Double:  
			for(int i=0; i<col.size(); i++){
				resultArray[i] = col.getParameterValues().get(i).getValueAsDouble(); //+ new Random().nextDouble() * 0.0001;
			}
			return resultArray;
		default:
			HashMap<Object, Double> stringEncoding = new HashMap<Object, Double>();
			double encodingCounter = 0.0;
			for(int i=0; i<col.size(); i++){
				Object valObj = col.getParameterValues().get(i).getValue();
				if(stringEncoding.containsKey(valObj)) {
					resultArray[i] = stringEncoding.get(valObj);
				} else {
					resultArray[i] = encodingCounter;
					stringEncoding.put(valObj, encodingCounter);
					encodingCounter++;
				}
				
			}
			return resultArray;
		}
	}
	
	

	
	@Override
	public ICorrelationResult getCorrelationResult() {
		
		return this.resultOfLastAnalysis;
	}

}
