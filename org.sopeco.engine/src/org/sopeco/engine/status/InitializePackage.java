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

	/**
	 * Returns the URL where SoPeCo is running.
	 * 
	 * @return the URL
	 */
	public String getHostUrl() {
		return hostUrl;
	}

	/**
	 * Returns the token, which is related to this session.
	 * 
	 * @return the token
	 */
	public String getToken() {
		return token;
	}

	/**
	 * @param pHostUrl
	 *            the hostUrl to set
	 */
	public void setHostUrl(String pHostUrl) {
		this.hostUrl = pHostUrl;
	}

	/**
	 * @param pToken
	 *            the token to set
	 */
	public void setToken(String pToken) {
		this.token = pToken;
	}

}
