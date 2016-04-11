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

import org.junit.Test;

import uk.ac.ox.it.ords.security.model.Audit;
import uk.ac.ox.it.ords.security.services.AuditService;

public class AuditServiceImplTest {
	
	@Test
	public void createAuditRecordByProject(){
		Audit audit = new Audit();
		audit.setProjectId(12345);
		audit.setUserId("a.user@somewhere.com");
		audit.setMessage("Test Audit Record");
		audit.setAuditType("TEST");
		AuditService.Factory.getInstance().createNewAudit(audit);
		
		assertEquals(1, AuditService.Factory.getInstance().getAuditListForProject(12345).size());
		assertEquals(1, AuditService.Factory.getInstance().getAuditListForUser("a.user@somewhere.com").size());
		
		assertEquals("a.user@somewhere.com", AuditService.Factory.getInstance().getAuditListForProject(12345).get(0).getUserId());
		assertEquals("Test Audit Record", AuditService.Factory.getInstance().getAuditListForProject(12345).get(0).getMessage());
		assertEquals("TEST", AuditService.Factory.getInstance().getAuditListForProject(12345).get(0).getAuditType());
		assertEquals(12345, AuditService.Factory.getInstance().getAuditListForProject(12345).get(0).getProjectId());

		assertNotNull(AuditService.Factory.getInstance().getAuditListForProject(12345).get(0).getTimeOfOperation());
		assertTrue(AuditService.Factory.getInstance().getAuditListForProject(12345).get(0).getAuditId() > 0);


		
		audit = new Audit();
		audit.setProjectId(12468);
		audit.setUserId("a.user@somewhere.com");
		audit.setMessage("Test Audit Record 2");
		audit.setAuditType("TEST");
		AuditService.Factory.getInstance().createNewAudit(audit);
		
		assertEquals(1, AuditService.Factory.getInstance().getAuditListForProject(12468).size());
		assertEquals(1, AuditService.Factory.getInstance().getAuditListForProject(12345).size());
	}
	
	@Test
	public void createAuditRecordByDatabase(){
		Audit audit = new Audit();
		audit.setLogicalDatabaseId(99);
		audit.setUserId("b.user@somewhere.com");
		audit.setMessage("Test Audit Record");
		audit.setAuditType("TEST");
		AuditService.Factory.getInstance().createNewAudit(audit);
		
		assertEquals(1, AuditService.Factory.getInstance().getAuditListForDatabase(99).size());
		assertEquals(1, AuditService.Factory.getInstance().getAuditListForUser("b.user@somewhere.com").size());
		
		assertEquals("b.user@somewhere.com", AuditService.Factory.getInstance().getAuditListForDatabase(99).get(0).getUserId());
		assertEquals("Test Audit Record", AuditService.Factory.getInstance().getAuditListForDatabase(99).get(0).getMessage());
		assertEquals("TEST", AuditService.Factory.getInstance().getAuditListForDatabase(99).get(0).getAuditType());
		assertEquals(0, AuditService.Factory.getInstance().getAuditListForDatabase(99).get(0).getProjectId());
		assertEquals(99, AuditService.Factory.getInstance().getAuditListForDatabase(99).get(0).getLogicalDatabaseId());

		assertNotNull(AuditService.Factory.getInstance().getAuditListForDatabase(99).get(0).getTimeOfOperation());
		assertTrue(AuditService.Factory.getInstance().getAuditListForDatabase(99).get(0).getAuditId() > 0);


		
		audit = new Audit();
		audit.setLogicalDatabaseId(22);
		audit.setUserId("b.user@somewhere.com");
		audit.setMessage("Test Audit Record 2");
		audit.setAuditType("TEST");
		AuditService.Factory.getInstance().createNewAudit(audit);
		
		assertEquals(1, AuditService.Factory.getInstance().getAuditListForDatabase(22).size());
		assertEquals(1, AuditService.Factory.getInstance().getAuditListForDatabase(99).size());
	}
	
	@Test
	public void createAuditRecordByUser(){
		Audit audit = new Audit();
		audit.setProjectId(12345);
		audit.setUserId("another.user@somewhere.com");
		audit.setMessage("Test Audit Record");
		audit.setAuditType("TEST");
		AuditService.Factory.getInstance().createNewAudit(audit);
		
		assertEquals(1, AuditService.Factory.getInstance().getAuditListForUser("another.user@somewhere.com").size());
	}

}
