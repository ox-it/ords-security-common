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

import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;

public class SSOFilter extends AuthenticatingFilter {

	@Override
	protected AuthenticationToken createToken(ServletRequest request,
			ServletResponse response) throws Exception {
				
		String user = ((HttpServletRequest)request).getRemoteUser();			
		String affiliation = (String)request.getAttribute("affiliation");
		if (affiliation == null) affiliation = "";
		RemoteUserToken token = new RemoteUserToken(user, affiliation);

		return token;
	}
	
	@Override
	protected boolean isAccessAllowed(ServletRequest request,
			ServletResponse response, Object mappedValue) {
		try {
			AuthenticationToken token = createToken(request, response);
			Subject subject = getSubject(request, response);
			subject.login(token);
			return subject.isAuthenticated();
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	protected boolean onAccessDenied(ServletRequest request,
			ServletResponse response) throws Exception {
		((HttpServletResponse)response).sendError(HttpServletResponse.SC_FORBIDDEN);
		return false;
	}





}
