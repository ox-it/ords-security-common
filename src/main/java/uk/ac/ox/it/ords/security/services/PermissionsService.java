package uk.ac.ox.it.ords.security.services;

import java.util.ServiceLoader;

import uk.ac.ox.it.ords.security.model.Permission;
import uk.ac.ox.it.ords.security.services.impl.hibernate.PermissionsServiceImpl;

public interface PermissionsService {
	
	public void createPermission(Permission permission) throws Exception;
	public void deletePermission(Permission permission) throws Exception;
	
	/**
	 * Factory for obtaining implementations
	 */
    public static class Factory {
		private static PermissionsService provider;
	    public static PermissionsService getInstance() {
	    	//
	    	// Use the service loader to load an implementation if one is available
	    	// Place a file called uk.ac.ox.oucs.ords.utilities.csv in src/main/resources/META-INF/services
	    	// containing the classname to load as the CsvService implementation. 
	    	// By default we load the Hibernate implementation.
	    	//
	    	if (provider == null){
	    		ServiceLoader<PermissionsService> ldr = ServiceLoader.load(PermissionsService.class);
	    		for (PermissionsService service : ldr) {
	    			// We are only expecting one
	    			provider = service;
	    		}
	    	}
	    	//
	    	// If no service provider is found, use the default
	    	//
	    	if (provider == null){
	    		provider = new PermissionsServiceImpl();
	    	}
	    	
	    	return provider;
	    }
	}

}
