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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetProvider;

import org.apache.log4j.Logger;

import uk.ac.ox.it.ords.security.configuration.MetaConfiguration;
import uk.ac.ox.it.ords.security.model.DatabaseServer;
import uk.ac.ox.it.ords.security.services.ODBCService;
import uk.ac.ox.it.ords.security.services.ServerConfigurationService;

public class ODBCServiceImpl implements ODBCService {
	
	static Logger log = Logger.getLogger(ODBCServiceImpl.class);
	
    public static final String SCHEMA_NAME = "public";
	protected static String ORDS_DATABASE_NAME = "ords.database.name";
	protected static String ORDS_DATABASE_USER = "ords.database.user";
	protected static String ORDS_DATABASE_PASSWORD = "ords.database.password";

	@Override
	public void removeRole(String role, String server, String database) throws Exception {    	
    	revokeFromDatabase(role, server, database);
    	dropRole(role, server, database);
	}
	
	/**
	 * Executes a set of commands to drop all privileges for a selected role
	 * @param odbcNameToRevoke the ODBC role name
	 * @param server the database server 
	 * @param databaseName the database name
	 * @return true; we'll remove this soon
	 * @throws Exception if revocation fails
	 */
    protected boolean revokeFromDatabase(String odbcNameToRevoke, String server, String databaseName) throws Exception {
    	List<String> commandList = getRevokeStatement(odbcNameToRevoke, databaseName);
    	runSQLStatements(commandList, server, databaseName);
    	// Some commands can only be run as a Postgres admin - there is one command in this work package like that, so run that here
    	String query = getSpecialRevokeStatement(odbcNameToRevoke);
       	commandList.clear();
    	commandList.add(query);
    	runSQLStatements(commandList, server, databaseName);
    	return true;
    }
	
    /**
     * Drops a role; first reassigning any objects owned by the role to
     * the generic ORDS role.
     * 
     * @param role the role to drop
     * @param server the database server
     * @param database the database
     * @throws Exception if dropping fails
     */
    protected void dropRole(String role, String server, String database) throws Exception{
    	List<String> commandList = new ArrayList<String>();
    	//
    	// Reassigns any remaining objects created by the role - for example, if this is an ODBC
    	// role with WRITE access it could have created tables or views, and we don't want to
    	// lose any of these. 
    	//
    	String query = String.format("REASSIGN OWNED BY \"%s\" TO \"ords\"", role);
    	commandList.add(query);
    	//
    	// Drops the rol
    	//
    	query = String.format("DROP ROLE \"%s\"", role);
    	commandList.add(query);
    	this.runSQLStatements(commandList, server, database);
    }
	
    /**
     * Gets a set of revoke statements for removing privileges for a role
     * @param roleName the name of the role to revoke
     * @param databaseName the database 
     * @return List of revocation statements for the role and database
     * @throws ClassNotFoundException if there is an SQL error
     * @throws SQLException if there is an SQL error
     */
    protected List<String> getRevokeStatement(String roleName, String databaseName) throws ClassNotFoundException, SQLException {
    	List<String> commandList = new ArrayList<String>(); 
    	commandList.add(String.format("REVOKE ALL PRIVILEGES ON ALL TABLES IN SCHEMA \"%s\" FROM \"%s\";", SCHEMA_NAME, roleName));
    	commandList.add(String.format("revoke all on schema %s from \"%s\";", SCHEMA_NAME, roleName));
    	commandList.add(String.format("revoke connect on database \"%s\" from \"%s\";", databaseName, roleName));
    	return commandList;
    }
    
    /**
     * Gets the special revocation statement for this role
     * @param roleName the role to revoke 
     * @return the SQL statement
     */
    protected String getSpecialRevokeStatement(String roleName) {
    	return String.format("alter default privileges in schema %s revoke all on tables from \"%s\" ;", SCHEMA_NAME, roleName);
    }
    
    /**
     * Executes a list of statements as the ORDS user	 
     * TODO move this to a generic support function as its not specific to ODBC services
     * @param statements the set of statements to execute
     * @param server the database server
     * @param databaseName the database
     * @throws Exception if there is a problem executing the statements
     */
	protected void runSQLStatements(List<String> statements, String server,
			String databaseName) throws Exception {
		Connection connection = null;
		Properties connectionProperties = new Properties();
		PreparedStatement preparedStatement = null;
		
		DatabaseServer databaseServer;
		
		if (server == null){
			databaseServer = ServerConfigurationService.Factory.getInstance().getOrdsDatabaseServer();
		} else {
			databaseServer = ServerConfigurationService.Factory.getInstance().getDatabaseServer(server);
		}
		
		connectionProperties.put("user", databaseServer.getUsername());
		connectionProperties.put("password", databaseServer.getPassword());
		
		if (databaseName == null) databaseName = databaseServer.getMasterDatabaseName();
		String connectionURL = databaseServer.getUrl(databaseName);
		
		try {
			connection = DriverManager.getConnection(connectionURL,
					connectionProperties);
			for (String statement: statements ) {
				preparedStatement = connection.prepareStatement(statement);
				preparedStatement.execute();
			}
		}
		finally {
			if (preparedStatement != null) {
				preparedStatement.close();
			}
			if (connection != null) {
				connection.close();
			}
		}
	}
	
	/**
	 * Gets all ODBC access roles for the specified database
	 * @param databaseServer the database server to get roles from
	 * @param databaseName the name of the database to get roles for
	 * @return List of roles as Strings
	 * @throws Exception if there is a problem obtaining the list of roles
	 */
	@Override
	public List<String> getAllODBCRolesForDatabase(String databaseServer, String databaseName) throws Exception{
		
		ArrayList<String> roles = new ArrayList<String>();
		//
		// Collect all roles associated with this database
		//
		String roleName = "%_ords_" + databaseName;
		
		//
		// We connect to the ORDS database in case the actual database has already been deleted and we
		// are running tests to check if all roles were dropped successfully
		//
		String ordsDatabase = MetaConfiguration.getConfiguration().getString(ORDS_DATABASE_NAME);
		
		String query = String.format("select rolname from pg_roles where rolname like '%s'", roleName);
		CachedRowSet results = this.runJDBCQuery(query, null, databaseServer, ordsDatabase);
		while(results.next()){
			roles.add(results.getString(1));
		}
		return roles;
	}
	
	/**
	 * Runs a JDBC query as the ORDS user
	 * TODO move this to a generic support class as its not specific to ODBC services
	 * @param query the query to run
	 * @param parameters the parameters to include in the statement
	 * @param server the database server
	 * @param databaseName the database
	 * @return either a CachedRowSet of the result, or NULL if the query wasn't a SELECT
	 * @throws Exception if there is a problem executing the query
	 */
	protected CachedRowSet runJDBCQuery(String query, List<Object> parameters,
			String server, String databaseName) throws Exception {
		
		Connection connection = null;
		Properties connectionProperties = new Properties();
		PreparedStatement preparedStatement = null;
		
		DatabaseServer databaseServer;
		
		if (server == null){
			databaseServer = ServerConfigurationService.Factory.getInstance().getOrdsDatabaseServer();
		} else {
			databaseServer = ServerConfigurationService.Factory.getInstance().getDatabaseServer(server);
		}
		
		connectionProperties.put("user", databaseServer.getUsername());
		connectionProperties.put("password", databaseServer.getPassword());
		
		if (databaseName == null) databaseName = databaseServer.getMasterDatabaseName();
		String connectionURL = databaseServer.getUrl(databaseName);
		try {
			connection = DriverManager.getConnection(connectionURL,
					connectionProperties);
			preparedStatement = connection.prepareStatement(query);
			if (query.toLowerCase().startsWith("select")) {
				ResultSet result = preparedStatement.executeQuery();
				CachedRowSet rowSet = RowSetProvider.newFactory()
						.createCachedRowSet();
				rowSet.populate(result);
				log.debug("prepareAndExecuteStatement:return result");
				return rowSet;
			} else {
				preparedStatement.execute();
				log.debug("prepareAndExecuteStatement:return null");
				return null;
			}

		} catch (SQLException e) {
			log.error("Error with this command", e);
			log.error("Query:" + query);
			throw e;
		} finally {
			if (preparedStatement != null) {
				preparedStatement.close();
			}
			if (connection != null) {
				connection.close();
			}
		}

	}

}
