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

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import uk.ac.ox.it.ords.security.services.ODBCService;

public class ODBCServiceImplTest extends ODBCServiceImpl{

	@Test
	public void getRoles() throws Exception{
		assertEquals(0, ODBCService.Factory.getInstance().getAllODBCRolesForDatabase("localhost", "ordstest").size());	
	}
	
	@Test
	public void createAndDropRoles() throws Exception{
		
		List<String> commandList = new ArrayList<String>();
		
		//
		// Create a database
		//
		commandList.add("create database main_99_99");
		//
		// Create a role
		//
		String roleName = "test_ords_main_99_99";
		String userPassword = "test";
		String command = String.format("create role \"%s\" nosuperuser login createdb inherit nocreaterole password '%s' valid until '2045-01-01'",
				roleName,
				userPassword);
		commandList.add(command);
		
		runSQLStatements(commandList, "localhost", "ordstest");
		
		assertEquals(1, ODBCService.Factory.getInstance().getAllODBCRolesForDatabase("localhost", "main_99_99").size());	

		ODBCService.Factory.getInstance().removeRole("test_ords_main_99_99", "localhost", "main_99_99");

		assertEquals(0, ODBCService.Factory.getInstance().getAllODBCRolesForDatabase("localhost", "main_99_99").size());	

	}
	
}
