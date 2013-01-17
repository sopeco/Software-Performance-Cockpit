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
package org.sopeco.plugin.std.exploration.breakdown.util;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.sopeco.plugin.std.exploration.breakdown.space.RelativePosition;

/**
 * A structure to hold two {@link RelativePosition}s where one is the original one,
 * and one a projection of the original.
 *
 */
public class Projection {
	
	/**
	 * the original Position.
	 */
	public RelativePosition original = new RelativePosition();
	
	/**
	 * a projection where at least one parametersValue is different from the original.
	 */
	public RelativePosition projected = new RelativePosition();
	 
	
	/**
	 * calculates a list of projection lists where every projection list contains all the 
	 * projections where the projected positions are the same.
	 * 
	 * @param projections a list of all projections.
	 * @return the list of projection lists where every projection list contains all the 
	 * projections where the projected positions are the same.
	 */
	public static List<List<Projection>> getEqualProjections(List<Projection> projections) 
	{
		//the following lines creating a tree where every node != leaf contains the value of one
		//of the parameters value. This works because the RelativePosition is always in the same order.
		
		//the root
		Map<Double, Object> root = new TreeMap<Double, Object>(new DoubleComperator());
		
		//the list to be returned
		List<List<Projection>> returnList = new ArrayList<List<Projection>>();
		
		//we iterate all projections and search for those which are equal.
		for (Projection projection : projections) {
			//to find the equal ones we create/traverse a tree.
			Map<Double, Object> currentMap = root;
			for (Double value : projection.projected.values()) {
				if (currentMap.containsKey(value)) {
					currentMap = (Map<Double, Object>) currentMap.get(value);
				} else {
					currentMap.put(value, new TreeMap<Double, Object>(new DoubleComperator()));
					currentMap = (Map<Double, Object>) currentMap.get(value);			
				}
				
			}
			
			//currentMap is now the leaf of the edge describing the whole path to the position.
			//and we know, at there is only one value at the leaf.
			if (currentMap.values().isEmpty()) {
				ArrayList<Projection> newList = new ArrayList<Projection>();
				currentMap.put(1d, newList);
				returnList.add(newList);
			}
			
			Iterator<Object> iterator = currentMap.values().iterator();
			((ArrayList<Projection>) iterator.next()).add(projection);
			
		}
		
		return returnList;
	}
	
	/**
	 * just for help.
	 */
	private static class DoubleComperator implements Comparator<Double> {


		@Override
		public int compare(Double arg0, Double arg1) {
			if (Math.abs(arg0-arg1) < 0.000000000000000000001) {
				return 0;
			} else {
				return arg0.compareTo(arg1);
			}
		}
	
	}
}
