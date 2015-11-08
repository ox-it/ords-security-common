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

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@SuppressWarnings("serial")
@Entity
@Table(name = "ordsaudit")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Audit implements Serializable{
	
    @Id
    @GeneratedValue
    @JsonProperty
    private int auditId;
    
    public enum AuditType { GENERIC_NOTAUTH, LOGIN, LOGIN_FAILED, LOGOFF, SIGNUP, SIGNUP_FAILED,
    CREATE_PROJECT, DELETE_PROJECT, UPDATE_PROJECT, CREATE_PROJECT_FAILED,
    CREATE_PROJECT_BILLING, DELETE_PROJECT_BILLING, UPDATE_PROJECT_BILLING,
    UPDATE_PROJECT_USER, DELETE_PROJECT_USER, CREATE_PROJECT_USER,
    COMMIT_NEW_DATA, COMMIT_DATA_CHANGE, DELETE_DATA_ROW,
    CREATE_PHYSICAL_DATABASE, CREATE_LOGICAL_DATABASE, EDIT_LOGICAL_DATABASE, UPLOAD_DATABASE,
    DELETE_PHYSICAL_DATABASE, DELETE_LOGICAL_DATABASE,
    CREATE_VIEW, DELETE_VIEW,
    ADD_USER_TO_DATABASE_FOR_ODBC, REMOVE_USER_TO_DATABASE_FOR_ODBC, ADD_ODBC_ROLE, REMOVE_ODBC_ROLE,
    ADD_IP_FOR_USER,
    RUN_USER_QUERY};
    
    private String auditType;
    
    private String message = "";
    
    private int projectId;
    
    /**
     * This is the user who causes the audit - i.e. the actor
     */
    private String userId;
    
    @JsonProperty
    private Timestamp timeOfOperation = new Timestamp(new Date().getTime());

    @JsonProperty
	public int getAuditId() {
		return auditId;
	}
  
    @JsonIgnore
	public void setAuditId(int auditId) {
		this.auditId = auditId;
	}
	
	public String getAuditType() {
		return auditType;
	}
	
	public void setAuditType(String auditType) {
        this.auditType = auditType.toString().replace("_", " ");
	}
	
	public String getMessage() {
		return message;
	}
	
    public void setMessage(String message) {
        if (message == null) {
            this.message = "";
        }
        else if (message.length() > 1000) {
            this.message = message.substring(0, 999);
        }
        else {
            this.message = message;
        }
    }
    
	public int getProjectId() {
		return projectId;
	}
	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
    @JsonProperty
	public Timestamp getTimeOfOperation() {
		return timeOfOperation;
	}
    @JsonIgnore
	public void setTimeOfOperation(Timestamp timeOfOperation) {
		this.timeOfOperation = timeOfOperation;
	}

    
    
}
