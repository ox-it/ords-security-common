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

import java.io.Serializable;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.SimpleSession;
import org.apache.shiro.session.mgt.eis.CachingSessionDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.ox.it.ords.security.services.SessionStorageService;

/**
 * A very basic SSO DAO for sessions stored on disk; only use this if for some reason
 * you can't make use of the standard Shiro EnterpriseDAO with ehcache.
 * 
 * Example configuration below for a shared shiro.ini configuration
 * 
 * # Sessions
 * sessionManager = org.apache.shiro.web.session.mgt.DefaultWebSessionManager
 * securityManager.sessionManager = $sessionManager
 * securityManager.sessionMode = native
 * 
 * # Session DAO
 * sessionDAO = uk.ac.ox.it.ords.security.SSOSessionDAO
 * securityManager.sessionManager.sessionDAO = $sessionDAO
 * 
 * # SSO Cookie
 * cookie = org.apache.shiro.web.servlet.SimpleCookie
 * cookie.name = SSOcookie
 * cookie.path = /
 * cookie.domain = localhost
 * securityManager.sessionManager.sessionIdCookie = $cookie
 */
public class SSOSessionDAO extends CachingSessionDAO{
	
	public static Logger log = LoggerFactory.getLogger(SSOSessionDAO.class);
			      
    public SSOSessionDAO(){
    }

	@Override
	protected void doDelete(Session session) {
		String sessionId = ((SimpleSession)session).getId().toString();
		try {
			SessionStorageService.Factory.getInstance().deleteSession(sessionId);
		} catch (Exception e) {
			log.error("Error deleting session", e);
		}
	}

	@Override
	protected void doUpdate(Session session) {
		String sessionId = ((SimpleSession)session).getId().toString();
		try {
			SessionStorageService.Factory.getInstance().updateSession(sessionId, session);
		} catch (Exception e) {
			log.error("Error updating session", e);
		}
	}

	@Override
	protected Serializable doCreate(Session session) {
        if (session.getId() != null) {
            throw new IllegalStateException("SessionID is non-null during create call.");
        }

        final Serializable sessionId = generateSessionId(session);

        if (session instanceof SimpleSession) {
            ((SimpleSession) session).setId(sessionId);
        } else {
            throw new IllegalArgumentException("Unexpected session class for session: " + session);
        }

        try {
			SessionStorageService.Factory.getInstance().createSession(sessionId.toString(), session);
		} catch (Exception e) {
			log.error("Error creating session", e);
		}

        return sessionId;
	}

	@Override
	protected Session doReadSession(Serializable sessionId) {
        
        try {
			SimpleSession session = (SimpleSession) SessionStorageService.Factory.getInstance().readSession(sessionId.toString());
	        return session;
		} catch (Exception e) {
			log.error("Error reading session", e);
			return null;
		}
        
	}

}
