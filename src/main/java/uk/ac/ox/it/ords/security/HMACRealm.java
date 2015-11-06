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
package uk.ac.ox.it.ords.security;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.realm.AuthenticatingRealm;

/**
 * Very basic realm that just uses the data 
 * provided from a RemoteUserToken; this gets the
 * principal from the SecurityContext set by the
 * SSO provider e.g. WebAuth or Shibboleth
 */
public class HMACRealm extends AuthenticatingRealm {

	public HMACRealm() {
	}

	public HMACRealm(CacheManager cacheManager) {
		super(cacheManager);
	}

	public HMACRealm(CredentialsMatcher matcher) {
		super(matcher);
	}

	public HMACRealm(CacheManager cacheManager, CredentialsMatcher matcher) {
		super(cacheManager, matcher);
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(
			AuthenticationToken token) throws AuthenticationException {
		if (token == null || token.getPrincipal() == null) throw new AuthenticationException();
		if (((String)token.getPrincipal()).trim().isEmpty()) throw new AuthenticationException();
		return new SimpleAuthenticationInfo(token.getPrincipal(), token.getCredentials(), "HMACRealm");
	}

	@Override
	public boolean supports(AuthenticationToken token) {
		return token instanceof HMACToken;
	}
	
	

}
