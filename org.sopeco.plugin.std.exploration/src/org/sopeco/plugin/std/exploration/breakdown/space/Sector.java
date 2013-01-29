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
package org.sopeco.plugin.std.exploration.breakdown.space;

import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;

/**
 * A tuple consisting of two defined positions and an error value which 
 * is associated with the enclosed room of both positions.
 * 
 * @author Rouven Krebs
 */
public class Sector implements Comparable<Sector> {
	/**
	 * first Position describing the area under investigation.
	 */
	public RelativePosition position1;
	
	/**
	 * second Position describing the area under investigation.
	 */
	public RelativePosition position2;
	
	/**
	 * expected error in the area defined by {@link #position1} and {@link #position2} 
	 */
	public Double error = 0.0;
	
	/**
	 * this map contains parameter id's and a relative value. This is filled up with
	 * parameters identified as non influencing in the area under investigation.
	 */
	public Map<String, Double> parametersToIgnore = new TreeMap<String, Double>(); 
	
	/**
	 * 
	 */
	LinkedList<RelativePosition> sectorValidationPoints = new LinkedList<RelativePosition>();
	
	/**
	 * constructs a new ErrorTuple.
	 * @param position1 first Position describing the area under investigation.
	 * @param position2 second Position describing the area under investigation.
	 * @param error expected error in the area defined by {@link #position1} and {@link #position2} 
	 */
	Sector(RelativePosition position1, RelativePosition position2, Double error)
	{
		this.position1 = position1;
		this.position2 = position2;
		this.error = error;
	}
	
	public Sector()
	{
	}
	
	/**
	 * @return true if both positions are equal and the error is equal too.
	 */
	public boolean equals(Object o)
	{
		if(o instanceof Sector)
		{
			Sector ot = (Sector)o;
			return ot.position2.equals(this.position2)
					&& ot.position1.equals(this.position1)
					&& ot.error.equals(this.error);

		}	
		return false;
	}

	public int hashCode()
	{	
		return (this.position2 == null? 1 : this.position2.hashCode()) * 3 
			+ (this.position1 == null ? 1 : this.position1.hashCode()) * 5 
			+ (this.error == null ? 1 : this.error.hashCode()) * 7;		
	}

	/**
	 * ordered by the error.
	 * See: {@link Comparable}
	 */
	@Override
	public int compareTo(Sector o) 
	{
		return (o.error - this.error)<0?-1:(o.error - this.error)==0?0:1;
	}
	
	public void addSectorValidationPoint(RelativePosition validationPoint){
		sectorValidationPoints.add(validationPoint);
	}
	
	public LinkedList<RelativePosition> getSectorValidationPoints() {
		return sectorValidationPoints;
	}
}
