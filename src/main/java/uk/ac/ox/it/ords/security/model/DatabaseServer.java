/*
 * Copyright 2015 University of Oxford
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uk.ac.ox.it.ords.security.model;

/**
 * Configuration and credentials for a database server
 */
public class DatabaseServer {

	private String alias;
	private String host;
	private int port;
	private String username;
	private String password;
	
	/**
	 *  The database to connect to when no specific database is indicated; for on-premise this is typically "postgres"
	 *  but for cloud database servers is either a random GUID or the username
	 */
	private String masterDatabaseName;
	
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getMasterDatabaseName() {
		return masterDatabaseName;
	}
	public void setMasterDatabaseName(String masterDatabaseName) {
		this.masterDatabaseName = masterDatabaseName;
	}
	
	public String getUrl(){
		return "jdbc:postgresql://" + this.getHost() + ":" + this.getPort() + "/" + this.getMasterDatabaseName();
	}
	
	public String getUrl(String database){
		return "jdbc:postgresql://" + this.getHost() + ":" + this.getPort() + "/" + database;
	}

}
