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
/**
 * 
 */
package org.sopeco.util;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An abstract class for concurrent processing of sequences of tasks.
 * 
 * In order to use this class for concurrent job processing, provide an implementation of 
 * the {@link ConcurrentJobProcessor#processJob(Object)} method and then follow the process below:
 * 
 * <ol>
 * <li> Create an instance of your implementation of the concurrent job processor.</li>
 * <li> Start the thread by calling the {@link #startProcessingThread()} method.</li>
 * <li> Add jobs using the {@link #addJob(Object)} method.</li>
 * <li> When there is no more job to be added, call the {@link #stopAndWaintUntilDone()} method to signal the list is done and the
 * concurrent thread should stop as soon as all the jobs are finished. If you don't want to wait for the jobs to be processed, 
 * simply call the {@link #stop()} method to signal that the there will be no jobs and the thread should 
 * end as soon as all inputs are processed. </li>
 * <li> Get the results by calling {@link #getResults()}. </li>
 * </ol>
 * 
 * @param <IN> the type of input jobs
 * @param <OUT> the type of output results
 * 
 * @author Roozbeh Farahbod
 *
 */
public abstract class ConcurrentJobProcessor<IN, OUT> implements Runnable {

	private static final int JOB_CHECK_CYCLE_DELAY = 50;

	private static final String DEFAULT_CONCURRENT_PROCESSOR_NAME = "Concurrent Processor";

	private static final String DEFAULT_RESULT_OBJECT_NAME = "result";

	public static final String DEFAULT_INPUT_OBJECT_NAME = "job";

	private static final Logger logger = LoggerFactory.getLogger(ConcurrentJobProcessor.class);
	
	/** Holds the name of input objects. Default value is {@value #DEFAULT_INPUT_OBJECT_NAME}. */
	protected String inputObjectName = DEFAULT_INPUT_OBJECT_NAME;
	
	/** Holds the name of output objects. {@value #DEFAULT_RESULT_OBJECT_NAME}. */
	protected String outputObjectName = DEFAULT_RESULT_OBJECT_NAME;
	
	/** Holds the name of input objects. Default value is {@value #DEFAULT_CONCURRENT_PROCESSOR_NAME}. */
	protected String processorName = DEFAULT_CONCURRENT_PROCESSOR_NAME;
	
	private int index = 0;
	private volatile boolean running = true;

	private ArrayList<IN> jobs = new ArrayList<IN>();
	
	private ArrayList<OUT> results = new ArrayList<OUT>();
	
	/**
	 * Starts this concurrent processor in a new thread. 
	 */
	public void startProcessingThread() {
		(new Thread(this, processorName)).start();
	}
	
	@Override
	public void run() {
		while (running || !isDone()) {
			if (!isDone()) {
				IN job = readNext();
				if (job != null) {
					final OUT result = processJob(job);
					results.add(result);
				}
			}
			
			try {
				Thread.sleep(JOB_CHECK_CYCLE_DELAY);
			} catch (InterruptedException e) {
				return;
			}
		}
	}
	
	/**
	 * Processes the given job and produces a result.
	 * 
	 * If there is any problem with the job, the method
	 * can return <code>null</code>. This method is overriden 
	 * by implementations of this class to provide the core processing
	 * behavior that is expected to be done in parallel.
	 * 
	 * @param job a job to be processed
	 * @return the result of processing the job or <code>null</code>.
	 */
	public abstract OUT processJob(IN job);

	/**
	 * Stops the plan processing.
	 */
	public void stop() {
		running = false;
	}
	
	/**
	 * Returns true if there are no more jobs to be done.
	 * 
	 * @return true if there are no more jobs to be done
	 */
	public boolean isDone() {
		return results.size() == jobs.size();
	}
	
	/**
	 * Adds a new job to the todo list.
	 * 
	 * @param job a new job to be processed
	 */
	public synchronized void addJob(IN job) {
		jobs.add(job);
	}
	
	/**
	 * Marks the end to the concurrent processing and waits until all jobs are done.
	 */
	public void stopAndWaintUntilDone() {
		stop();
		while (!isDone()) {
			try {
				Thread.sleep(JOB_CHECK_CYCLE_DELAY);
			} catch (InterruptedException e) {
				logger.error("Wait loop was interrupted. Error: {}", e.getMessage());
				return;
			}
		}
	}
	
	/**
	 * Returns a copy of the results list.
	 *  
	 * @return a copy of the results list
	 */
	public synchronized ArrayList<OUT> getResults() {
		return new ArrayList<OUT>(results);
	}
	
	/**
	 * Reads the next job from the input list and advances the index.
	 * 
	 * @return a job from the todo list
	 */
	protected synchronized IN readNext() {
		if (index < jobs.size()) {
			logger.debug("Reading the next {} in the queue...", inputObjectName);
			return jobs.get(index++);
		} else {
			return null;
		}
	}

	public String getInputObjectName() {
		return inputObjectName;
	}

	public void setInputObjectName(String inputObjectName) {
		this.inputObjectName = inputObjectName;
	}

	public String getOutputObjectName() {
		return outputObjectName;
	}

	public void setOutputObjectName(String outputObjectName) {
		this.outputObjectName = outputObjectName;
	}

	public String getProcessorName() {
		return processorName;
	}

	public void setProcessorName(String processorName) {
		this.processorName = processorName;
	}
}
