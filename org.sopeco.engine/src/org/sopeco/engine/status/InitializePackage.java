package org.sopeco.engine.status;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * 
 * @author Marius Oehler
 * 
 */
@XmlRootElement
public class InitializePackage implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String hostUrl;
	private String token;

	public String getHostUrl() {
		return hostUrl;
	}

	public String getToken() {
		return token;
	}

	public void setHostUrl(String pHostUrl) {
		hostUrl = pHostUrl;
	}

	public void setToken(String pToken) {
		token = pToken;
	}

}
