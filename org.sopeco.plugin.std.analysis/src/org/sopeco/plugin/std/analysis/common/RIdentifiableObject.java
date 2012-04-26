package org.sopeco.plugin.std.analysis.common;

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
