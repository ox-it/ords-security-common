package uk.ac.ox.it.ords.security.services;

import java.util.ServiceLoader;

import org.apache.shiro.session.Session;

import uk.ac.ox.it.ords.security.services.impl.hibernate.SessionStorageServiceImpl;

public interface SessionStorageService {

	public abstract Session readSession(String sessionId) throws Exception;
	public abstract void createSession(String sessionId, Session session) throws Exception;
	public abstract void updateSession(String sessionId, Session session) throws Exception;
	public abstract void deleteSession(String sessionId) throws Exception;
	
	/**
	 * Factory for obtaining implementations
	 */
    public static class Factory {
		private static SessionStorageService provider;
	    public static SessionStorageService getInstance() {
	    	//
	    	// Use the service loader to load an implementation if one is available
	    	// Place a file called uk.ac.ox.oucs.ords.utilities.csv in src/main/resources/META-INF/services
	    	// containing the classname to load as the CsvService implementation. 
	    	// By default we load the Hibernate implementation.
	    	//
	    	if (provider == null){
	    		ServiceLoader<SessionStorageService> ldr = ServiceLoader.load(SessionStorageService.class);
	    		for (SessionStorageService service : ldr) {
	    			// We are only expecting one
	    			provider = service;
	    		}
	    	}
	    	//
	    	// If no service provider is found, use the default
	    	//
	    	if (provider == null){
	    		provider = new SessionStorageServiceImpl();
	    	}
	    	
	    	return provider;
	    }
	}
}
