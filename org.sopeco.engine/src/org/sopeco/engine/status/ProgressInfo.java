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
package org.sopeco.engine.status;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class ProgressInfo implements IStatusInfo {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String experimentSeriesName;
	private int numberOfRepetition;
	private int repetition;

	/**
	 * Constructor.
	 */
	public ProgressInfo() {
		this(null, -1, -1);
	}

	/**
	 * Constructor.
	 */
	public ProgressInfo(String pExperimentSeriesName) {
		this(pExperimentSeriesName, -1, -1);
	}

	/**
	 * Constructor.
	 */
	public ProgressInfo(String pExperimentSeriesName, int pRepetition) {
		this(pExperimentSeriesName, pRepetition, -1);
	}

	/**
	 * Constructor.
	 */
	public ProgressInfo(String pExperimentSeriesName, int pRepetition, int pNumberOfRepetition) {
		experimentSeriesName = pExperimentSeriesName;
		repetition = pRepetition;
		numberOfRepetition = pNumberOfRepetition;
	}

	/**
	 * @return the experimentSeriesName
	 */
	public String getExperimentSeriesName() {
		return experimentSeriesName;
	}

	/**
	 * @return the numberOfRepetition
	 */
	public int getNumberOfRepetition() {
		return numberOfRepetition;
	}

	/**
	 * @return the repetition
	 */
	public int getRepetition() {
		return repetition;
	}

	/**
	 * @param pExperimentSeriesName
	 *            the experimentSeriesName to set
	 */
	public void setExperimentSeriesName(String pExperimentSeriesName) {
		experimentSeriesName = pExperimentSeriesName;
	}

	/**
	 * @param pNumberOfRepetition
	 *            the numberOfRepetition to set
	 */
	public void setNumberOfRepetition(int pNumberOfRepetition) {
		numberOfRepetition = pNumberOfRepetition;
	}

	/**
	 * @param pRepetition
	 *            the repetition to set
	 */
	public void setRepetition(int pRepetition) {
		repetition = pRepetition;
	}

}
