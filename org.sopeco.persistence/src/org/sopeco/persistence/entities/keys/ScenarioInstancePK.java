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
package org.sopeco.persistence.entities.keys;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.sopeco.persistence.entities.ScenarioInstance;

/**
 * Class that represents the primary key of the entity {@link ScenarioInstance}
 * 
 * @author Dennis Westermann
 */
@Embeddable
public class ScenarioInstancePK implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "name")
	private String name;
	
	@Column(name = "measurementEnvironmentUrl")
	private String measurementEnvironmentUrl;
	
	public ScenarioInstancePK(){
		
	}
	public ScenarioInstancePK(String name, String measurementEnvironmentUrl) {
		super();
		this.name = name;
		this.measurementEnvironmentUrl = measurementEnvironmentUrl;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMeasurementEnvironmentUrl() {
		return measurementEnvironmentUrl;
	}
	public void setMeasurementEnvironmentUrl(String measurementEnvironmentUrl) {
		this.measurementEnvironmentUrl = measurementEnvironmentUrl;
	}
	
	@Override
	public boolean equals(Object o) {

		 if (this == o) return true;
		 if (o == null || getClass() != o.getClass()) return false;

		 ScenarioInstancePK obj = (ScenarioInstancePK) o;
		 if (name == null || measurementEnvironmentUrl == null || obj.name == null || obj.measurementEnvironmentUrl == null) return false;
		 if(!name.equals(obj.name) || !measurementEnvironmentUrl.equals(obj.measurementEnvironmentUrl)) return false;

		 return true;

	}

	@Override
	public int hashCode() {
		if(name!=null && measurementEnvironmentUrl!=null){
			String hashString = name + measurementEnvironmentUrl;
			return hashString.hashCode();
		} else {
			return 0;
		}
	}

	@Override
    public String toString() {

       return "ScenarioInstancePK{" +
	                 "name='" + name + '\'' +
	                 "measurementEnvironmentUrl='" + measurementEnvironmentUrl + '\'' +'}';
    }
	
}
