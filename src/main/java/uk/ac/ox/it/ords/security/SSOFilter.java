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

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;

public class SSOFilter extends AuthenticatingFilter {

	@Override
	protected AuthenticationToken createToken(ServletRequest request,
			ServletResponse response) throws Exception {	
		String user;
		try {
			user = ((HttpServletRequest)request).getRemoteUser();
		} catch (Exception e) {
			//
			// No current HTTPSession
			//
			user = null;
		}
		String affiliation = (String)request.getAttribute("affiliation");
		if (affiliation == null) affiliation = "";
		RemoteUserToken token = new RemoteUserToken(user, affiliation);
		return token;
	}
	
	@Override
	protected boolean isAccessAllowed(ServletRequest request,
			ServletResponse response, Object mappedValue) {
		
		Subject subject = getSubject(request, response);

		try {
			//
			// We have to logout first to ensure stateless operation, as 
			// even with session persistence off we still retain the 
			// same principal if we call login(). We do this
			// with the anon user as they MUST be stateless.
			//
			if (subject.getPrincipal()!=null && subject.getPrincipal().equals("anonymous")){
				subject.logout();
			}
		} catch (Exception e1) {
			//
			// This shouldn't happen ... 
			// TODO enable proper logging
			//
			e1.printStackTrace();
		}
		
		//
		// Login with current REMOTE_USER
		//
		try {
			AuthenticationToken token = createToken(request, response);
			subject.login(token);
			if (subject.isAuthenticated()){
				return true;
			};
		} catch (AuthenticationException e) {
			//
			// In Permissive mode, create an anonymous subject
			//
			// We have to "login" with anonymous to allow anonymous role permission
			// checking
			//
			if (isPermissive(mappedValue)){
				RemoteUserToken token = new RemoteUserToken("anonymous", "");
				subject.login(token);
				return true;
			}			
		} catch (Exception e) {
			//
			// Something went wrong, so lets assume no login is possible at
			// the moment.
			// TODO enable proper logging
			e.printStackTrace();
			return false;
		}
		return false;
	}

	@Override
	protected boolean onAccessDenied(ServletRequest request,
			ServletResponse response) throws Exception {
		((HttpServletResponse)response).sendError(HttpServletResponse.SC_FORBIDDEN);
		return false;
	}
	
	


}
