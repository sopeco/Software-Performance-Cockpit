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
package org.sopeco.plugin.std.exploration.screening.container;

import java.util.ArrayList;
import java.util.List;

import org.sopeco.persistence.entities.definition.ParameterDefinition;

/**
 * Container used to store the levels of a parameter for every run of a design.
 * 
 * @author Pascal Meier
 */
public class ParameterRunLevels {

	/**
	 * Parameter of which the run levels are stored
	 */
	private ParameterDefinition parameter;
	/**
	 * List of run levels (1, -1 or 0)
	 */
	private List<Integer> runLevels;
	
	/**
	 * Constructor.
	 * @param parameter parameter of which the run levels are stored
	 */
	public ParameterRunLevels(ParameterDefinition parameter) {
		super();
		this.parameter = parameter;
		runLevels = new ArrayList<Integer>();
	}

	public ParameterDefinition getParameter() {
		return parameter;
	}
	
	/**
	 * Adds a run level to the container. Only levels -1, 1 or 0 are allowed.
	 * @param level value of the level
	 * @throws FrameworkException
	 */
	public void addRunLevel(int level) {
		if (!(level == 1 || level == -1 || level == 0)) {
			throw new IllegalStateException("Unknown level specified.");
		}
		runLevels.add(level);
	}
	
	/**
	 * Inserts a run level at the specified position.
	 * @param position position where the run-level will be inserted
	 * @param level the value of the level that will be inserted. has to be 1, -1 or 0
	 * @throws Exception
	 */
	public void insertRunLevelAtPosition(int position, int level) {
		if (!(level == 1 || level == -1 || level == 0)) {
			throw new IllegalStateException("Unknown level specified.");
		}
		runLevels.add(position, level);
	}
	
	public int getLevelByRunNumber(int runNumber) {
		return runLevels.get(runNumber);
	}
	
	public List<Integer> getAllRunLevels() {
		return runLevels;
	}
	
	public int getNumberOfRunLevels() {
		return runLevels.size();
	}
}
