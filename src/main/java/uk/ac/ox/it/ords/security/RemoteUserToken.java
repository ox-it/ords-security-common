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

import org.apache.shiro.authc.AuthenticationToken;

/**
 * A token representing a user authenticated
 * using an SSO or federated identity solution such
 * as Stanford WebAuth or Shibboleth.
 */
public class RemoteUserToken implements AuthenticationToken {
	
	private static final long serialVersionUID = -8635250055907545843L;
	
	private String principal;
	private String affiliation;

	public RemoteUserToken(String principal, String affiliation) {
		this.principal = principal;
		this.affiliation = affiliation;
	}

	public Object getPrincipal() {
		return principal;
	}
	
	public String getAffiliation() {
		return affiliation;
	}

	public Object getCredentials() {
		return affiliation;
	}

}
