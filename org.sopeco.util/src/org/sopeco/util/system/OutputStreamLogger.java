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
