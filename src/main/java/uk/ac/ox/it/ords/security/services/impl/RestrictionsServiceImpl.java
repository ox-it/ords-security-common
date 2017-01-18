package uk.ac.ox.it.ords.security.services.impl;

import org.apache.shiro.SecurityUtils;

import uk.ac.ox.it.ords.security.services.RestrictionsService;

public class RestrictionsServiceImpl implements RestrictionsService {

	@Override
	public int getMaximumUploadSize() {
		if (SecurityUtils.getSubject().hasRole("PremiumUser")) return 25000;
		if (SecurityUtils.getSubject().hasRole("StandardUser")) return 1000;
		if (SecurityUtils.getSubject().hasRole("BasicUser")) return 16;
		if (SecurityUtils.getSubject().hasRole("User")) return 1;
		return 0;
	}

	@Override
	public int getMaximumNumberOfLiveProjects() {
		if (SecurityUtils.getSubject().hasRole("PremiumUser")) return 9999;
		if (SecurityUtils.getSubject().hasRole("StandardUser")) return 9999;
		if (SecurityUtils.getSubject().hasRole("BasicUser")) return 3;
		if (SecurityUtils.getSubject().hasRole("User")) return 1;
		return 0;
	}

	@Override
	public int getMaximumDatabasesPerProject() {
		if (SecurityUtils.getSubject().hasRole("PremiumUser")) return 9999;
		if (SecurityUtils.getSubject().hasRole("StandardUser")) return 3;
		if (SecurityUtils.getSubject().hasRole("BasicUser")) return 3;
		if (SecurityUtils.getSubject().hasRole("User")) return 3;
		return 0;
	}

	@Override
	public int getMqxiumumDatasetsPerDatabase() {
		if (SecurityUtils.getSubject().hasRole("PremiumUser")) return 9999;
		if (SecurityUtils.getSubject().hasRole("StandardUser")) return 9999;
		if (SecurityUtils.getSubject().hasRole("BasicUser")) return 3;
		if (SecurityUtils.getSubject().hasRole("User")) return 3;
		return 0;
	}

}
