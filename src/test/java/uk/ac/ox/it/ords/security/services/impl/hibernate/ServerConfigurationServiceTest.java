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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import uk.ac.ox.it.ords.security.model.DatabaseServer;
import uk.ac.ox.it.ords.security.services.ServerConfigurationService;

public class ServerConfigurationServiceTest {
	
	@Test
	public void loadServers() throws Exception{
		DatabaseServer server = ServerConfigurationService.Factory.getInstance().getDatabaseServer();	
		assertEquals("localhost", server.getHost());
		assertEquals(5432, server.getPort());
		assertEquals("ords", server.getUsername());
		assertEquals("ords", server.getPassword());
		assertEquals("ordstest", server.getMasterDatabaseName());
		
		assertEquals(1, ServerConfigurationService.Factory.getInstance().getAllDatabaseServers().size());
		assertEquals(server, ServerConfigurationService.Factory.getInstance().getDatabaseServer("localhost"));
	}

	@Test
	public void getServerByAlias() throws Exception{
		DatabaseServer server = ServerConfigurationService.Factory.getInstance().getDatabaseServer();	
		assertEquals("main", server.getAlias());
		assertEquals("localhost", server.getHost());
		assertEquals(5432, server.getPort());
		assertEquals("ords", server.getUsername());
		assertEquals("ords", server.getPassword());
		assertEquals("ordstest", server.getMasterDatabaseName());
		
		assertEquals(1, ServerConfigurationService.Factory.getInstance().getAllDatabaseServers().size());
		assertEquals(server, ServerConfigurationService.Factory.getInstance().getDatabaseServer("localhost"));
		assertEquals(server, ServerConfigurationService.Factory.getInstance().getDatabaseServer("main"));
		assertEquals(server, ServerConfigurationService.Factory.getInstance().getDatabaseServerByAlias("main"));
		assertNull(ServerConfigurationService.Factory.getInstance().getDatabaseServerByAlias("banana"));
		assertNull(ServerConfigurationService.Factory.getInstance().getDatabaseServer("banana"));

	}

}
