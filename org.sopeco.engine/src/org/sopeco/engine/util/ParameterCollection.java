/**
 * 
 */
package org.sopeco.engine.util;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.sopeco.persistence.dataset.ParameterValue;
import org.sopeco.persistence.entities.definition.ParameterDefinition;

/**
 * A smart collection of {@link ParameterDefinition} or {@link ParameterValue} that 
 * acts as a map of parameter name to the values it holds.
 * 
 * @author Roozbeh Farahbod
 * @param <K> a type of either {@link ParameterDefinition} or {@link ParameterValue}
 *
 */
public class ParameterCollection<V> implements Set<V>, Serializable {
	
	/** Version ID for serialization. */
	private static final long serialVersionUID = 1L;
	
	/** Holds a map view to this collection. */
	private Map<String, V> map = new HashMap<String, V>();
	
	protected ParameterCollection() {
	}
	
	@Override
	public boolean add(V e) {
		final String name = getParamFullName(e);
		return map.put(name, e) == null;
	}

	@Override
	public boolean addAll(Collection<? extends V> collection) {
		boolean changed = false;
		boolean singleAdd = false;
		if(collection != null) {
			for (V e: collection) {
				singleAdd = add(e);
				changed = changed || singleAdd; 
			}
		}
		return changed;
	}


	@Override
	public void clear() {
		map.clear();
	}


	@Override
	public boolean contains(Object obj) {
		return map.values().contains(obj);
	}


	@Override
	public boolean containsAll(Collection<?> collection) {
		return map.values().containsAll(collection);
	}


	@Override
	public boolean isEmpty() {
		return map.isEmpty();
	}


	@Override
	public Iterator<V> iterator() {
		return map.values().iterator();
	}


	@Override
	public boolean remove(Object obj) {
		if (isValidObject(obj)) {
			final Object prev = map.remove(getParamFullName(obj));
			return prev != null;
		} else
			return false;
	}


	@Override
	public boolean removeAll(Collection<?> collection) {
		boolean changed = false;
		boolean singleChange = false;
		for (Object o: collection) {
			singleChange = remove(o);
			changed = changed || singleChange;
		}
		return changed;
	}


	@Override
	public boolean retainAll(Collection<?> arg0) {
		throw new UnsupportedOperationException("Method retainAll(collection) is not supported in " + this.getClass().getSimpleName() + ".");
	}


	@Override
	public int size() {
		return map.values().size();
	}


	@Override
	public Object[] toArray() {
		return map.values().toArray();
	}


	@Override
	public <T> T[] toArray(T[] arr) {
		return map.values().toArray(arr);
	}

	/**
	 * Returns an object from this collection with the given parameter name.
	 * @param parameterName full parameter name
	 * @return an instance of V
	 */
	public V get(String parameterName) {
		return map.get(parameterName);
	}

	/**
	 * Returns <code>true</code> if the given collection contains an object
	 * corresponding to the given parameter name.
	 * 
	 * @param parameterName the full parameter name
	 */
	public boolean containsParameter(String parameterName) {
		return get(parameterName) != null;
	}

	/**
	 * This is a helper method for getting one single object out of this collection. 
	 * 
	 * This is useful when the collection has only one object in it, or when the caller
	 * wants to perform a non-deterministic choose from this collection.
	 * 
	 * @return an object from this collection, if it is not empty, or <code>null</code> otherwise.
	 */
	public V getOne() {
		final Iterator<V> iterator = map.values().iterator();
		if (iterator.hasNext())
			return iterator.next();
		else
			return null;
	}
	
	/**
	 * Returns whether the given object is valid to be part of this collection.
	 */
	private boolean isValidObject(Object obj) {
		return (obj instanceof ParameterDefinition) || (obj instanceof ParameterValue<?>);
	}

	/**
	 * Returns the full parameter name of the given object.
	 */
	private String getParamFullName(Object obj) {
		final ParameterDefinition pDef = getParameterDefinition(obj);
		
		// unless something is wrong, pDef should not be null
		return pDef.getFullName();
	}

	/**
	 * Returns the parameter definition of the given object.
	 */
	private ParameterDefinition getParameterDefinition(Object obj) {
		ParameterDefinition pDef = null;
		if (obj instanceof ParameterDefinition) {
			pDef = (ParameterDefinition)obj;
		} else
			if (obj instanceof ParameterValue<?>) {
				pDef = ((ParameterValue<?>)obj).getParameter();
			}
		return pDef;
	}

}
