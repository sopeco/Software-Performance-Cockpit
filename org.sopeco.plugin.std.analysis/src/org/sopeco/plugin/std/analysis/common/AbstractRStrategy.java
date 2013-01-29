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
package org.sopeco.plugin.std.analysis.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.sopeco.analysis.wrapper.AnalysisWrapper;
import org.sopeco.engine.registry.AbstractSoPeCoExtensionArtifact;
import org.sopeco.engine.registry.ISoPeCoExtension;
import org.sopeco.persistence.dataset.AbstractDataSetColumn;
import org.sopeco.persistence.dataset.DataSetInputColumn;
import org.sopeco.persistence.dataset.SimpleDataSet;
import org.sopeco.persistence.entities.definition.AnalysisConfiguration;
import org.sopeco.persistence.entities.definition.ParameterDefinition;
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
				RAdapter.getWrapper().executeCommandString("library(" + library + ");");
			}
			RAdapter.shutDown();
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
	
	protected AnalysisConfiguration config;
	protected RDataSet data; 
	
	protected void loadDataSetInR(SimpleDataSet dataset){
		
		data = new RDataSet(dataset);
		data.loadDataSetInR();
	}
}
