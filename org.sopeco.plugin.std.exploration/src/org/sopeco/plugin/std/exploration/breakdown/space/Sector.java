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
