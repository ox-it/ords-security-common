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
package uk.ac.ox.it.ords.security.services.impl.hibernate;

import static org.junit.Assert.*;

import org.apache.shiro.session.mgt.SimpleSession;
import org.junit.Test;

import uk.ac.ox.it.ords.security.services.SessionStorageService;

public class SessionStorageServiceImplTest {
	
	@Test
	public void sessionLifecycleTest() throws Exception{
		
		SimpleSession session = new SimpleSession();
		session.setAttribute("test", "test");
		session.setId("1");
		
		SessionStorageService.Factory.getInstance().createSession("1", session);
		
		SimpleSession result = (SimpleSession) SessionStorageService.Factory.getInstance().readSession("1");
		assertEquals("test", result.getAttribute("test"));
		SessionStorageService.Factory.getInstance().deleteSession("1");
		assertNull(SessionStorageService.Factory.getInstance().readSession("1"));

	}
	
	@Test
	public void sessionLifecycleUpdateTest() throws Exception{
		
		SimpleSession session = new SimpleSession();
		session.setAttribute("test", "test");
		session.setId("1");
		
		SessionStorageService.Factory.getInstance().createSession("1", session);
		
		session.setAttribute("new", "newvalue");
		session.setAttribute("test", "newvalue");
		SessionStorageService.Factory.getInstance().updateSession("1", session);
		
		SimpleSession result = (SimpleSession) SessionStorageService.Factory.getInstance().readSession("1");
		assertEquals("newvalue", result.getAttribute("new"));
		assertEquals("newvalue", result.getAttribute("test"));
		
		SessionStorageService.Factory.getInstance().deleteSession("1");
		assertNull(SessionStorageService.Factory.getInstance().readSession("1"));
	}
	
	@Test
	public void sessionNonExistingTest() throws Exception{
		SimpleSession result = (SimpleSession) SessionStorageService.Factory.getInstance().readSession("1");
		assertNull(result);		
	}
	
	@Test(expected = Exception.class)
	public void deleteNonExistingTest() throws Exception{
		SessionStorageService.Factory.getInstance().deleteSession("1");
	}
	
	@Test(expected = Exception.class)
	public void updateNonExistingTest() throws Exception{
		SessionStorageService.Factory.getInstance().updateSession("1", null);
	}

}
