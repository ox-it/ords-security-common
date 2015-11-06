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

import org.junit.Test;

import uk.ac.ox.it.ords.security.model.Permission;
import uk.ac.ox.it.ords.security.services.PermissionsService;

public class PermissionServiceImplTest {
	
	@Test
	public void testCreatePermissions() throws Exception{
		Permission permission = new Permission();
		permission.setPermission("do:any:thing");
		permission.setRole("ultrauser");
		
		PermissionsService.Factory.getInstance().createPermission(permission);
		
		assertEquals(1, PermissionsService.Factory.getInstance().getPermissionsForRole("ultrauser").size());

		assertEquals("do:any:thing", PermissionsService.Factory.getInstance().getPermissionsForRole("ultrauser").get(0).getPermission());

		PermissionsService.Factory.getInstance().deletePermission(permission);
		
		assertEquals(0, PermissionsService.Factory.getInstance().getPermissionsForRole("ultrauser").size());

	}
	
	@Test
	public void testDontCreateDuplicatePermissions() throws Exception{
		Permission permission = new Permission();
		permission.setPermission("do:any:thing");
		permission.setRole("ultrauser");
		PermissionsService.Factory.getInstance().createPermission(permission);
		
		Permission newPermission = new Permission();
		newPermission.setPermission("do:any:thing");
		newPermission.setRole("ultrauser");
		PermissionsService.Factory.getInstance().createPermission(newPermission);
		
		assertEquals(1, PermissionsService.Factory.getInstance().getPermissionsForRole("ultrauser").size());

		assertEquals("do:any:thing", PermissionsService.Factory.getInstance().getPermissionsForRole("ultrauser").get(0).getPermission());

		PermissionsService.Factory.getInstance().deletePermission(permission);
		
		assertEquals(0, PermissionsService.Factory.getInstance().getPermissionsForRole("ultrauser").size());

	}

}
