package uk.ac.ox.it.ords.security.services.impl;

import org.apache.shiro.SecurityUtils;

import uk.ac.ox.it.ords.security.services.RestrictionsService;

public class RestrictionsServiceImpl implements RestrictionsService {

	@Override
	public int getMaximumUploadSize() {
		if (SecurityUtils.getSubject().hasRole("administrator")) return 25000;
		if (SecurityUtils.getSubject().hasRole("premiumuser")) return 25000;
		if (SecurityUtils.getSubject().hasRole("standarduser")) return 1000;
		if (SecurityUtils.getSubject().hasRole("basicuser")) return 16;
		if (SecurityUtils.getSubject().hasRole("user") || SecurityUtils.getSubject().hasRole("localuser")) return 1;
		return 0;
	}

	@Override
	public int getMaximumNumberOfLiveProjects() {
		if (SecurityUtils.getSubject().hasRole("administrator")) return 9999;
		if (SecurityUtils.getSubject().hasRole("premiumuser")) return 9999;
		if (SecurityUtils.getSubject().hasRole("standarduser")) return 9999;
		if (SecurityUtils.getSubject().hasRole("basicuser")) return 3;
		if (SecurityUtils.getSubject().hasRole("user") || SecurityUtils.getSubject().hasRole("localuser")) return 1;
		return 0;
	}

	@Override
	public int getMaximumDatabasesPerProject() {
		if (SecurityUtils.getSubject().hasRole("administrator")) return 9999;
		if (SecurityUtils.getSubject().hasRole("premiumuser")) return 9999;
		if (SecurityUtils.getSubject().hasRole("standarduser")) return 3;
		if (SecurityUtils.getSubject().hasRole("basicuser")) return 3;
		if (SecurityUtils.getSubject().hasRole("user") || SecurityUtils.getSubject().hasRole("localuser")) return 3;
		return 0;
	}

	@Override
	public int getMqxiumumDatasetsPerDatabase() {
		if (SecurityUtils.getSubject().hasRole("administrator")) return 9999;
		if (SecurityUtils.getSubject().hasRole("premiumuser")) return 9999;
		if (SecurityUtils.getSubject().hasRole("standarduser")) return 9999;
		if (SecurityUtils.getSubject().hasRole("basicuser")) return 3;
		if (SecurityUtils.getSubject().hasRole("user") || SecurityUtils.getSubject().hasRole("localuser")) return 3;
		return 0;
	}

}
