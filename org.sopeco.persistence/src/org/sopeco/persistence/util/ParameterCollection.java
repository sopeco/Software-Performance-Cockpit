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
 * 
 */
package org.sopeco.persistence.util;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
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

	public String toString() {
		StringBuffer result = new StringBuffer();
		for (Entry<String, V> e: map.entrySet()) {
			result.append(", " + e.getKey() + "->" + e.getValue());
		}
		if (result.length() == 0) {
			return "{}";
		} else {
			return "{" + result.substring(2) + "}";
		}
	}
}
