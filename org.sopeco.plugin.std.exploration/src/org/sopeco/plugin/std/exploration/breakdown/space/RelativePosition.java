package org.sopeco.plugin.std.exploration.breakdown.space;

import java.util.Map;
import java.util.TreeMap;

/**
 * Datatype to define a relative position.
 * <br>
 * A relative position must have a value v for each parameter for which
 * 0 &ge; v &le; 1 
 *<br>
 * String = Parameters id
 * <br>
 * Double = relative Value
 * 
 * @author Rouven Krebs
 *
 */

public class RelativePosition extends TreeMap<String, Double> 
{

	private static final long serialVersionUID = 1L;

	public RelativePosition() 
	{
		super();
	}

	public RelativePosition(Map<? extends String, ? extends Double> m) 
	{
		super(m);
	}

}
