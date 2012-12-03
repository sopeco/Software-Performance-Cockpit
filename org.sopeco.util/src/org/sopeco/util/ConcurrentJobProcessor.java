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
 * @author Roozbeh Farahbod
 *
 */
public abstract class ConcurrentJobProcessor<IN, OUT> implements Runnable {

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
				Thread.sleep(50);
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
				Thread.sleep(50);
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
		if (index  < jobs.size()) {
			logger.debug("Reading the next {} in the queue...", inputObjectName);
			return jobs.get(index++);
		} else
			return null;
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
