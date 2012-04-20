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
