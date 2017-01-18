package uk.ac.ox.it.ords.security.services;

import uk.ac.ox.it.ords.security.services.impl.RestrictionsServiceImpl;

public interface RestrictionsService {
	
	public abstract int getMaximumUploadSize();
	
	public abstract int getMaximumNumberOfLiveProjects();
	
	public abstract int getMaximumDatabasesPerProject();
	
	public abstract int getMqxiumumDatasetsPerDatabase();
	
	public static class Factory{
		
		static RestrictionsService provider;
		
		public static RestrictionsService getInstance(){
			if (provider == null) provider = new RestrictionsServiceImpl();
			return provider;
	}
		
	}
	
}
