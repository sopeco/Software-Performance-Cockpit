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
	@Column(name = "id")
	private String id;
	
	
	@Column(name = "dbName")
	private String dbName;
	
	@Column(name = "host")
	private String host;
	
	@Column(name = "port")
	private String port;
	
	

	
	@Column
	private boolean protectedByPassword;
	
	public DatabaseInstance() {
		id = "";
	}
	
	public DatabaseInstance(String dbName, String host, String port, boolean passwordSet) {
		setDbName(dbName);
		setHost(host);
		setPort(port);
		setProtectedByPassword(passwordSet);
	}

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
		updateId();
	}

	public String getUser(){
		return dbName;
	}
	


	public boolean isProtectedByPassword() {
		return protectedByPassword;
	}

	public void setProtectedByPassword(boolean protectedByPassword) {
		this.protectedByPassword = protectedByPassword;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
		updateId();
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
		updateId();
	}
	
	public String getId(){
		return id;
	}
	
	private void updateId(){
		id = host + ":" + port + "/" + dbName;
	}
}
