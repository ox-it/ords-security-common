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
package uk.ac.ox.it.ords.security.permissions;

/**
 * Shared permission definitions.
 * 
 * <p>Any permissions that need to be common across multiple services should be
 * identified here. Anything only referred to by a single service should be
 * defined within a subclass within that service.</p>
 */
public class Permissions {

	//
	// Permissions relating to projects. These permissions need to
	// be granted to a user on creation by the User Service as well
	// as being checked by the Project Service.
	//
	public static final String PROJECT_CREATE = "project:create";
	public static final String PROJECT_CREATE_FULL = "project:create-full";
	public static final String PROJECT_UPGRADE = "project:upgrade";
	public static final String PROJECT_MODIFY_ALL = "project:modify:*";
	public static final String PROJECT_VIEW_ALL = "project:view:*";
	public static final String PROJECT_VIEW_PUBLIC = "project:view-public";
	
	// common database permissions shared between database-service and database-structure-service

	public static String DATABASE_ANY_ACTION(int id){
		return "database:*:"+id;
	}
	public static String DATABASE_DELETE(int id){
		return "database:delete:"+id;
	}
	
	public static String DATABASE_MODIFY(int id){
		return "database:modify:"+id;
	}
	
	public static String DATABASE_VIEW(int id){
		return "database:view:"+id;
	}

	public static final String DATABASE_CREATE = "database:create";
	public static final String DATABASE_CREATE_FULL = "database:create-full";
	public static final String DATABASE_UPDATE_ALL = "database:update:*";
	public static final String DATABASE_DELETE_ALL = "database:delete:*";
	public static final String DATABASE_VIEW_ALL = "database:view:*";
	public static final String DATABASE_VIEW_PUBLIC = "database:view-public";
	
	
}
