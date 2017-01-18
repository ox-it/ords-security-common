package uk.ac.ox.it.ords.security.services;

public interface RestrictionsService {
	
	public abstract int getMaximumUploadSize();
	
	public abstract int getMaximumNumberOfLiveProjects();
	
	public abstract int getMaximumDatabasesPerProject();
	
	public abstract int getMqxiumumDatasetsPerDatabase();
	
}
