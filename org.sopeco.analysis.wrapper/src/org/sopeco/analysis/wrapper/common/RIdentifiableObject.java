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
package org.sopeco.analysis.wrapper.common;

/**
 * This class provides the simple infrastructure to create objects with unique
 * IDs in R. Classes that shall be accessible in R should extend this class and
 * retrieve their unique identifier by calling getId();
 * 
 * @author Jens Happe
 * 
 */
public class RIdentifiableObject {

	/**
	 * Static ID counter.
	 */
	private static int id_generator = 0;

	/**
	 * ID of this object.
	 */
	private String id = null;

	/**
	 * Computes the next valid R ID.
	 * 
	 * @return Next R ID.
	 */
	public static String getNextId() {
		return "object_" + id_generator++;
	}

	/**
	 * Lazy initialization.
	 * 
	 * @return Returns the object's R ID. The ID is defined during the first
	 *         access of this method.
	 */
	public String getId() {
		if (id == null) {
			id = getNextId();
		}
		return id;
	}


}
