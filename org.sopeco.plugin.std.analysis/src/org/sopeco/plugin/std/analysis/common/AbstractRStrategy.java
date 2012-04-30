package org.sopeco.plugin.std.analysis.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.sopeco.engine.registry.AbstractSoPeCoExtensionArtifact;
import org.sopeco.engine.registry.ISoPeCoExtension;
import org.sopeco.model.configuration.environment.ParameterDefinition;
import org.sopeco.persistence.dataset.AbstractDataSetColumn;
import org.sopeco.persistence.dataset.DataSetInputColumn;
import org.sopeco.plugin.std.analysis.util.RAdapter;

/**
 * Implementation of the R specific features that have to be implemented by all
 * strategies used by the R-Adapter.
 * 
 * @author Jens Happe
 * 
 */
public abstract class AbstractRStrategy extends AbstractSoPeCoExtensionArtifact {

	
	
	public AbstractRStrategy(ISoPeCoExtension<?> provider) {
		super(provider);
		rId = RIdentifiableObject.getNextId();
	}

	private String rId;
	
	protected String getId(){
		return rId;
	}
	
	protected static final String R_DELETE_OBJECTS = 
		"if(length(ls(pattern=\"object_*\"))>10) for(i in 1:(length(ls(pattern=\"object_*\"))-10)) rm(list=(lsl<-ls(pattern=\"object_*\"))[1])";
	
	/**
	 * List of libraries that shall be loaded into the R environment before
	 * executing the strategy.
	 */
	private List<String> requiredLibraries = new ArrayList<String>();

	/**
	 * Backward reference to the R Adapter.
	 */
	private RAdapter rAdapter = null;
	
	public void loadLibraries() {
		    
			for (String library : requiredLibraries) {
				RAdapter.getWrapper().executeRCommandString("library(" + library + ");");
			}
			requiredLibraries.clear();
		
	
	}
	
	/**
	 * Sub-classes can use this method to add required libraries to the list of
	 * required libraries. These are automatically loaded before a strategy is
	 * executed.
	 * 
	 * @param libraryName
	 *            Name of the library to be loaded.
	 */
	protected void requireLibrary(String libraryName) {
		requiredLibraries.add(libraryName);
	}

	/**
	 * @return Returns the R-Adapter using this strategy.
	 */
	protected RAdapter getAdapter() {
		return rAdapter;
	}
	
	@SuppressWarnings("rawtypes")
	protected List<ParameterDefinition> getParameterDefintions(Collection<DataSetInputColumn> inputColumns) {
		ArrayList<ParameterDefinition> resultList = new ArrayList<ParameterDefinition>();
		
		for (AbstractDataSetColumn<?> col : inputColumns) {
				resultList.add(col.getParameter());
		}
		return resultList;
	}
	
}
