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
package uk.ac.ox.it.ords.security.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class AuditTest {

	/**
	 * This is largely just to ensure we don't accidentally delete one of these
	 * http://stackoverflow.com/questions/1079700/how-to-test-enum-types
	 */
	@Test
	public void testEnumerations(){
		
		assertNotNull(Audit.AuditType.ADD_IP_FOR_USER.name());
		assertNotNull(Audit.AuditType.ADD_ODBC_ROLE.name());
		assertNotNull(Audit.AuditType.ADD_USER_TO_DATABASE_FOR_ODBC.name());
		assertNotNull(Audit.AuditType.COMMIT_DATA_CHANGE.name());
		assertNotNull(Audit.AuditType.COMMIT_NEW_DATA.name());
		assertNotNull(Audit.AuditType.CREATE_LOGICAL_DATABASE.name());
		assertNotNull(Audit.AuditType.CREATE_PHYSICAL_DATABASE.name());
		assertNotNull(Audit.AuditType.CREATE_PROJECT.name());
		assertNotNull(Audit.AuditType.CREATE_PROJECT_FAILED.name());
		assertNotNull(Audit.AuditType.CREATE_PROJECT_USER.name());
		assertNotNull(Audit.AuditType.CREATE_VIEW.name());
		assertNotNull(Audit.AuditType.DELETE_DATA_ROW.name());
		assertNotNull(Audit.AuditType.DELETE_LOGICAL_DATABASE.name());
		assertNotNull(Audit.AuditType.DELETE_PHYSICAL_DATABASE.name());
		assertNotNull(Audit.AuditType.DELETE_PROJECT.name());
		assertNotNull(Audit.AuditType.DELETE_PROJECT_USER.name());
		assertNotNull(Audit.AuditType.DELETE_VIEW.name());
		assertNotNull(Audit.AuditType.EDIT_LOGICAL_DATABASE.name());
		assertNotNull(Audit.AuditType.GENERIC_NOTAUTH.name());
		assertNotNull(Audit.AuditType.LOGIN.name());
		assertNotNull(Audit.AuditType.LOGIN_FAILED.name());
		assertNotNull(Audit.AuditType.LOGOFF.name());
		assertNotNull(Audit.AuditType.REMOVE_ODBC_ROLE.name());
		assertNotNull(Audit.AuditType.REMOVE_USER_TO_DATABASE_FOR_ODBC.name());
		assertNotNull(Audit.AuditType.RUN_USER_QUERY.name());
		assertNotNull(Audit.AuditType.SIGNUP.name());
		assertNotNull(Audit.AuditType.SIGNUP_FAILED.name());
		
	}
	
	@Test
	public void messageSize(){
		Audit audit = new Audit();
		audit.setMessage(null);
		assertEquals("", audit.getMessage());
		
		audit.setMessage("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed viverra eros vel nisl consectetur, at consectetur lacus vestibulum. Phasellus non laoreet urna. Pellentesque fringilla, tellus et viverra dignissim, risus augue venenatis quam, non molestie risus sem at dui. Aliquam erat volutpat. Ut vel felis leo. Donec vitae massa suscipit, blandit massa ut, accumsan quam. In a laoreet odio, a convallis quam. Maecenas imperdiet sodales purus, a scelerisque leo. In dapibus lobortis lorem ac consectetur. In imperdiet enim ex, in convallis lectus vestibulum id. Fusce faucibus venenatis leo sed placerat. Pellentesque et suscipit est, sed ornare nulla. Donec et malesuada justo. Aenean sit amet nisl eget ligula consectetur pharetra in vitae ex. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Morbi nec libero a nulla tempus consequat. Morbi eget velit volutpat, molestie ex sit amet, pulvinar dolor. Nulla lacinia, risus sit amet faucibus fringilla, libero eros pellentesque tellus, a fringilla mi orci sed metus. Maecenas ante ante, mattis sed odio eu, ornare lacinia metus. Fusce cras amet.");
		assertEquals("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed viverra eros vel nisl consectetur, at consectetur lacus vestibulum. Phasellus non laoreet urna. Pellentesque fringilla, tellus et viverra dignissim, risus augue venenatis quam, non molestie risus sem at dui. Aliquam erat volutpat. Ut vel felis leo. Donec vitae massa suscipit, blandit massa ut, accumsan quam. In a laoreet odio, a convallis quam. Maecenas imperdiet sodales purus, a scelerisque leo. In dapibus lobortis lorem ac consectetur. In imperdiet enim ex, in convallis lectus vestibulum id. Fusce faucibus venenatis leo sed placerat. Pellentesque et suscipit est, sed ornare nulla. Donec et malesuada justo. Aenean sit amet nisl eget ligula consectetur pharetra in vitae ex. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Morbi nec libero a nulla tempus consequat. Morbi eget velit volutpat, molestie ex sit amet, pulvinar dolor. Nulla lacinia, risus sit amet faucibus fringilla, libero eros pellentesque tellus, a fring", audit.getMessage());
	}
	
}
