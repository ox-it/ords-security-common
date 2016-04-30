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
package uk.ac.ox.it.ords.security.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.configuration.XMLConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.ox.it.ords.security.configuration.MetaConfiguration;
import uk.ac.ox.it.ords.security.model.DatabaseServer;
import uk.ac.ox.it.ords.security.services.ServerConfigurationService;

/**
 * Default implementation of the database credentials interface that stores
 * credentials in a file on the server.
 */
public class ServerConfigurationServiceImpl implements ServerConfigurationService {

	public static Logger log = LoggerFactory.getLogger(ServerConfigurationServiceImpl.class);

	/**
	 * The default configuration location; this can be overridden in config.xml to point
	 * to another file.
	 */
	public static final String DEFAULT_SERVER_CONFIG_LOCATION = "databaseservers.xml";
	
	/**
	 * Singleton instance
	 */
	private static ServerConfigurationServiceImpl instance;
	
	/**
	 * Private constructor for singleton
	 */
	private ServerConfigurationServiceImpl(){
		try {
			load();
		} catch (Exception e) {
			log.error("Couldn't load database server credentials");
		}
	}
	
	/**
	 * Get the service instance
	 * @return the service instance
	 */
	public static ServerConfigurationServiceImpl getInstance(){
		if (instance == null) instance = new ServerConfigurationServiceImpl();
		
		return instance;
	}
	
	/**
	 * The servers defined in the configuration file
	 */
	private List<DatabaseServer> servers;
	
	/**
	 * The server for the internal metadata
	 */
	private DatabaseServer metadataServer;

	/**
	 * Loads the configuration file
	 * @return
	 * @throws Exception
	 */
	protected void load()  throws Exception {
		
		String serverConfigurationLocation = DEFAULT_SERVER_CONFIG_LOCATION;
		
		//
		// Check if there is a different load location from the default
		//
		try {
			serverConfigurationLocation = MetaConfiguration.getConfiguration().getString("ords.server.configuration");
			if (serverConfigurationLocation == null){
				log.debug("No server configuration location set; using defaults");
				serverConfigurationLocation = DEFAULT_SERVER_CONFIG_LOCATION;
			}
		} catch (Exception e) {
			log.debug("No server configuration location set; using defaults");
			serverConfigurationLocation = DEFAULT_SERVER_CONFIG_LOCATION;
		}

		//
		// Load the Server Configuration file
		//
		XMLConfiguration xmlServerConfiguration  = new XMLConfiguration();
		try {
			xmlServerConfiguration.setFileName(serverConfigurationLocation);
			xmlServerConfiguration.load();
		} catch (Exception e1) {
			log.error("Cannot read server configuration at " + serverConfigurationLocation);
			throw new Exception("Cannot read server configuration");
		}

		//
		// Read the server list
		//
		int serverArray = xmlServerConfiguration.getStringArray("server[@host]").length;
		
		servers = new ArrayList<DatabaseServer>();

		for (int i = 0; i < serverArray; i++){
			DatabaseServer databaseServer = new DatabaseServer();
			
			databaseServer.setHost(xmlServerConfiguration.getString("server("+i+")[@host]"));
			databaseServer.setPort(xmlServerConfiguration.getInt("server("+i+")[@port]"));
			databaseServer.setUsername(xmlServerConfiguration.getString("server("+i+")[@username]"));
			databaseServer.setPassword(xmlServerConfiguration.getString("server("+i+")[@password]"));
			databaseServer.setMasterDatabaseName(xmlServerConfiguration.getString("server("+i+")[@database]"));

			servers.add(databaseServer);
		}
		
		metadataServer = new DatabaseServer();
		metadataServer.setHost(xmlServerConfiguration.getString("metadata(0)[@host]"));
		metadataServer.setPort(xmlServerConfiguration.getInt("metadata(0)[@port]"));
		metadataServer.setUsername(xmlServerConfiguration.getString("metadata(0)[@username]"));
		metadataServer.setPassword(xmlServerConfiguration.getString("metadata(0)[@password]"));
		metadataServer.setMasterDatabaseName(xmlServerConfiguration.getString("metadata(0)[@database]"));
		
	}

	@Override
	public DatabaseServer getDatabaseServer() throws Exception {
		return servers.get(0);
	}

	@Override
	public List<DatabaseServer> getAllDatabaseServers() throws Exception {
		return servers;
	}

	@Override
	public DatabaseServer getDatabaseServer(String host) throws Exception {
		for (DatabaseServer databaseServer : servers){
			if (databaseServer.getHost().equalsIgnoreCase(host)){
				return databaseServer;
			}
		}
		return null;
	}

	@Override
	public DatabaseServer getOrdsDatabaseServer() throws Exception {
		return metadataServer;
	} 
	
	
}
