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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.util.Factory;
import org.junit.BeforeClass;
import org.junit.Test;

public class SSOFilterTest extends AbstractShiroTest{

	@BeforeClass
	public static void setup() {
		Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro.ini");
		SecurityManager securityManager = factory.getInstance();
		SecurityUtils.setSecurityManager(securityManager);
	}

	@Test
	public void createToken() throws Exception{
		SSOFilter filter = new SSOFilter();
		HttpServletRequest request = mock(HttpServletRequest.class);
		HttpServletResponse response = mock(HttpServletResponse.class);
		when(request.getRemoteUser()).thenReturn("pingu");
		when(request.getAttribute("affiliation")).thenReturn("antarctica");
		RemoteUserToken token = (RemoteUserToken) filter.createToken(request, response);
		assertEquals("pingu", token.getPrincipal().toString());
		assertEquals("antarctica", token.getAffiliation());
	}

	@Test
	public void checkAccessNoHeaders(){
		SSOFilter filter = new SSOFilter();
		HttpServletRequest request = mock(HttpServletRequest.class);
		HttpServletResponse response = mock(HttpServletResponse.class);
		assertFalse(filter.isAccessAllowed(request, response, null));
	}

	@Test
	public void checkAccessGoodHeaders() throws Exception{
		SSOFilter filter = new SSOFilter();
		HttpServletRequest request = mock(HttpServletRequest.class);
		HttpServletResponse response = mock(HttpServletResponse.class);
		when(request.getRemoteUser()).thenReturn("pingu");
		when(request.getAttribute("affiliation")).thenReturn("antarctica");
		assertTrue(filter.isAccessAllowed(request, response, null));
	}

	@Test
	public void checkAccessEmptyHeaders() throws Exception{
		SSOFilter filter = new SSOFilter();
		HttpServletRequest request = mock(HttpServletRequest.class);
		HttpServletResponse response = mock(HttpServletResponse.class);
		when(request.getRemoteUser()).thenReturn("");
		when(request.getAttribute("affiliation")).thenReturn("");
		assertFalse(filter.isAccessAllowed(request, response, null));
	}

	@Test
	public void checkAccessNullHeaders() throws Exception{
		SSOFilter filter = new SSOFilter();
		HttpServletRequest request = mock(HttpServletRequest.class);
		HttpServletResponse response = mock(HttpServletResponse.class);
		when(request.getRemoteUser()).thenReturn(null);
		when(request.getAttribute("affiliation")).thenReturn(null);
		assertFalse(filter.isAccessAllowed(request, response, null));
	}

	@Test
	public void checkOnAccessDenied() throws Exception{
		SSOFilter filter = new SSOFilter();
		HttpServletRequest request = mock(HttpServletRequest.class);
		HttpServletResponse response = mock(HttpServletResponse.class);
		when(request.getRemoteUser()).thenReturn(null);
		when(request.getAttribute("affiliation")).thenReturn(null);
		assertFalse(filter.isAccessAllowed(request, response, null));
		filter.onAccessDenied(request, response);
		verify(response).sendError(403);
	}
	
	@Test
	public void checkPermissive() throws Exception{
		SSOFilter filter = new SSOFilter();
		HttpServletRequest request = mock(HttpServletRequest.class);
		HttpServletResponse response = mock(HttpServletResponse.class);
		when(request.getRemoteUser()).thenReturn(null);
		when(request.getAttribute("affiliation")).thenReturn(null);
		String[] config = new String[1];
		config[0] = "permissive";
		assertTrue(filter.isAccessAllowed(request, response, config));
	}

}
