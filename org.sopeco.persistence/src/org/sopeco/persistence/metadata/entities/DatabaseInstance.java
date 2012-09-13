package org.sopeco.persistence.metadata.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@NamedQueries({ @NamedQuery(name = "findAllDataBaseInstances", query = "SELECT o FROM DatabaseInstance o") })
@Entity
public class DatabaseInstance {
	@Id
	@Column(name = "connectionUrl")
	private String connectionUrl;
	
	@Column(name = "dbName")
	private String dbName;
	
	@Column
	private String password;
	
	@Column
	private boolean protectedByPassword;
	
	public DatabaseInstance() {
	}
	
	public DatabaseInstance(String dbName, String connectionUrl) {
		setDbName(dbName);
		setConnectionUrl(connectionUrl);
	}

	public String getConnectionUrl() {
		return connectionUrl;
	}

	public void setConnectionUrl(String connectionUrl) {
		this.connectionUrl = connectionUrl;
	}

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public String getUser(){
		return dbName;
	}
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isProtectedByPassword() {
		return protectedByPassword;
	}

	public void setProtectedByPassword(boolean protectedByPassword) {
		this.protectedByPassword = protectedByPassword;
	}
}
