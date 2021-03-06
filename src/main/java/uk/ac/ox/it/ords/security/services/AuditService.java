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

import uk.ac.ox.it.ords.security.model.Audit;
import uk.ac.ox.it.ords.security.services.impl.hibernate.AuditServiceImpl;

public interface AuditService {
	
	/**
	 * getAuditListForProject
	 * @param projectId the project to get the audit list for
	 * @return List of audit objects associated with that project id
	 */
	abstract List<Audit>getAuditListForProject( int projectId );
	
	/**
	 * getAuditListForDatabase
	 * @param logicalDatabaseId the database to get the audit list for
	 * @return List of audit objects associated with that database id
	 */
	abstract List<Audit>getAuditListForDatabase( int logicalDatabaseId );
	
	/**
	 * getAuditListForUser
	 * @param userId the user to get the audit list for
	 * @return List of audit objects associated with the user id
	 */
	abstract List<Audit>getAuditListForUser( String userId ) ;
	
	/**
	 * createNewAudit
	 * @param audit: an audit object initialized with the values to save
	 */
	abstract void createNewAudit( Audit audit );
	
	/**
	 * Factory for obtaining implementations
	 */
    public static class Factory {
		private static AuditService provider;
	    public static AuditService getInstance() {
	    	//
	    	// Use the service loader to load an implementation if one is available
	    	// Place a file called uk.ac.ox.oucs.ords.utilities.csv in src/main/resources/META-INF/services
	    	// containing the classname to load as the CsvService implementation. 
	    	// By default we load the Hibernate implementation.
	    	//
	    	if (provider == null){
	    		ServiceLoader<AuditService> ldr = ServiceLoader.load(AuditService.class);
	    		for (AuditService service : ldr) {
	    			// We are only expecting one
	    			provider = service;
	    		}
	    	}
	    	//
	    	// If no service provider is found, use the default
	    	//
	    	if (provider == null){
	    		provider = new AuditServiceImpl();
	    	}
	    	
	    	return provider;
	    }
	}

}
