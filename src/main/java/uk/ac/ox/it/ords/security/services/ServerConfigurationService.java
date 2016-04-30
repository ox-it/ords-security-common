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
package uk.ac.ox.it.ords.security.services;

import java.util.List;
import java.util.ServiceLoader;

import uk.ac.ox.it.ords.security.model.DatabaseServer;
import uk.ac.ox.it.ords.security.services.impl.ServerConfigurationServiceImpl;

/**
 *  Interface for obtaining credentials for database servers. The default implementation
 *  stores credentials in a configuration file on the server file system, but other 
 *  implementations could use a database table, registry, or external API
 */
public interface ServerConfigurationService {
	
	/**
	 * Gets the specified database server
	 * @param host the host
	 * @return a DatabaseServer instance
	 * @throws Exception if the database configuration file is corrupt
	 */
	public DatabaseServer getDatabaseServer(String host) throws Exception;	
	
	/**
	 * Gets the preferred database server to use for user data
	 * @return a DatabaseServer instance
	 * @throws Exception if the database configuration file is corrupt
	 */
	public DatabaseServer getDatabaseServer() throws Exception;
	
	/**
	 * Gets the preferred database server to use for internal metadata
	 * @return a DatabaseServer instance
	 * @throws Exception if the database configuration file is corrupt
	 */
	public DatabaseServer getOrdsDatabaseServer() throws Exception;
	
	/**
	 * Gets all configured database servers
	 * @return a List of DatabaseServers
	 * @throws Exception if the database configuration file is corrupt
	 */
	public List<DatabaseServer> getAllDatabaseServers() throws Exception;    
    
	/**
	 * Factory for obtaining implementations
	 */
    public static class Factory {
		private static ServerConfigurationService provider;
	    public static ServerConfigurationService getInstance() {
	    	//
	    	// Use the service loader to load an implementation if one is available
	    	// Place a file called uk.ac.ox.oucs.ords.utilities.csv in src/main/resources/META-INF/services
	    	// containing the classname to load as the CsvService implementation. 
	    	// By default we load the Hibernate implementation.
	    	//
	    	if (provider == null){
	    		ServiceLoader<ServerConfigurationService> ldr = ServiceLoader.load(ServerConfigurationService.class);
	    		for (ServerConfigurationService service : ldr) {
	    			// We are only expecting one
	    			provider = service;
	    		}
	    	}
	    	//
	    	// If no service provider is found, use the default
	    	//
	    	if (provider == null){
	    		provider = ServerConfigurationServiceImpl.getInstance();
	    	}
	    	
	    	return provider;
	    }
	}
	 

}
