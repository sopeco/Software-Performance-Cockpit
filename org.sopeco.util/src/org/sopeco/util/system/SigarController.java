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
 * Project: UI-PriceTags
 * 
 * (c) 2011 Roozbeh Farahbod, roozbeh.farahbod@sap.com
 * 
 */
package org.sopeco.util.system;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.hyperic.sigar.ProcUtil;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Interface to the Sigar monitoring tool. 
 * 
 * @author Roozbeh Farahbod
 *
 */
public class SigarController {

	/** Thread local copies of {@link Sigar} instances */
	private static ThreadLocal<SigarController> controllerInstances = new ThreadLocal<SigarController>() {

		@Override
		protected SigarController initialValue() {
			return new SigarController();
		}

	};
	
	/** Local instance of {@link Sigar} */
	public final Sigar sigar;
	
	private static Logger logger = LoggerFactory.getLogger(SigarController.class);
	
	private SigarController() {
		this.sigar = new Sigar();
	}
	
	/** 
	 * @return The sigar controller instance associated with the calling thread.
	 */
	public static SigarController getThreadInstance() {
		return controllerInstances.get();
	}
	
	/**
	 * Gets the current aggregated CPU time of the process identified by 
	 * <code>processName</code>. 
	 * 
	 * @param processName the name of the process of interest
	 * @return the CPU time in milliseconds if there is such a process; returns -1 otherwise.
	 * @throws ProcessInfoException 
	 */
	public long getProcessCPUTime(String processName) throws ProcessInfoException {
		long result = 0;
		
		for (Long pid: getProcessPIds(processName)) {
			result += getProcessCPUTime(pid);
		}
		
		return result;
	}
	
	/**
	 * Gets the current aggregated CPU time of the process identified by the given id. 
	 * 
	 * @param pid the id of the process of interest
	 * @return the CPU time in milliseconds if there is such a process; returns -1 otherwise.
	 * @throws ProcessInfoException 
	 */
	public long getProcessCPUTime(long pid) throws ProcessInfoException {
		try {
			return sigar.getProcCpu(pid).getTotal();
		} catch (SigarException e) {
			throw new ProcessInfoException(e);
		}
	}
	
	/**
	 * Returns the process id of the process with the given name.
	 * 
	 * @param processName process name
	 * @return the process id, if such a process exists.
	 * @throws ProcessInfoException if there is a problem with looking 
	 * 								up the process name or the process cannot be found.
	 */
	public long getProcessPId(String processName) throws ProcessInfoException {
		final Set<Long> pids = getProcessPIds(processName);
		if (pids.size() > 0)
			return pids.iterator().next();
		else
			throw new ProcessNotFoundException("Process name '" + processName + "' cannot be found.", logger);
	}
	
	/**
	 * Returns a set of process ids for the given process name.
	 * 
	 * @param processName process name
	 * @return the set of process ids; empty if there is no such process.
	 * @throws ProcessInfoException if there is a problem with looking up the process name.
	 */
	public Set<Long> getProcessPIds(String processName) throws ProcessInfoException {
		long[] pids;
		try {
			pids = sigar.getProcList();
		} catch (SigarException e1) {
			throw new ProcessInfoException(e1);
		}
		
		processName = processName.toLowerCase();
		
		Set<Long> result = new HashSet<Long>();
		for (long pid: pids) {
			try {
				String pName = sigar.getProcExe(pid).getName().toLowerCase();
 				if (pName.indexOf(processName) >= 0) {
					result.add(pid);
				}
			} catch (SigarException e) {
//				logger.debug("Process (pid:{}) is not available anymore.", pid);
				// silently ignore, it's no big deal. :)
			}
		}

		if (result.size() == 0) {
			logger.debug("No '{}' process running.", processName);
			return Collections.emptySet();
		} else {
			logger.debug("Process ids for '{}' are {}.", processName, result);
			return result;
		}
	}
	
	/**
	 * Kills the given process with the given signal. 
	 * @param pid process id
	 * @param signal the signal sent to the process
	 * @throws SystemToolsException if it fails.
	 * @see {@link Sigar#kill(long, int)}
	 */
	public void killProcess(long pid, int signal) throws SystemToolsException {
		try {
			sigar.kill(pid, signal);
		} catch (SigarException e) {
			throw new SystemToolsException("Cannot kill process (pid: " + pid + ").", e, logger);
		}
	}
	
}
