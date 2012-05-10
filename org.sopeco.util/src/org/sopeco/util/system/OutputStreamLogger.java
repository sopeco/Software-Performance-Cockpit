/**
 * Project: SoPeCo
 * 
 * (c) 2011 Roozbeh Farahbod, roozbeh.farahbod@sap.com
 * 
 */
package org.sopeco.util.system;

import java.io.IOException;
import java.io.OutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A wrapper to log messages written to an output stream.
 * 
 * @author Roozbeh Farahbod
 *
 */
public class OutputStreamLogger extends OutputStream {

	public enum Level {TRACE, DEBUG, INFO, WARN, ERROR};
	
	private final OutputStream stream;
	private final Logger logger;
	
	public Level level = Level.TRACE;

	private StringBuffer buffer = new StringBuffer();
	
	/**
	 * Creates an output stream logger for the given class.
	 * 
	 * @param stream the target stream to forward the data 
	 * @param c the class responsible for the log messages
	 */
	public OutputStreamLogger(OutputStream stream, Class c) {
		this(stream, LoggerFactory.getLogger(c));
	}
	
	/**
	 * Creates an output stream logger with the given logger.
	 * 
	 * @param stream the target stream to forward the data
	 * @param logger a logger instance
	 */
	public OutputStreamLogger(OutputStream stream, Logger logger) {
		this.stream = stream;
		this.logger = logger;
	}
	
	@Override
	public void write(int d) throws IOException {
		if (d == '\n') {
			log(buffer.toString());
			buffer = new StringBuffer();
		} else { 
			buffer.append((char)d);
		}
	}

	/*
	 * Logs the message to the logger.
	 */
	private void log(String msg) {
		switch (level) {
		case TRACE:
			logger.trace(msg);
			break;
			
		case DEBUG:
			logger.debug(msg);
			break;
			
		case INFO:
			logger.info(msg);
			break;
			
		case WARN:
			logger.warn(msg);
			break;
			
		case ERROR:
			logger.error(msg);
			break;
		}
	}

}
