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
import java.util.Hashtable;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.SimpleSession;
import org.apache.shiro.session.mgt.eis.CachingSessionDAO;

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
	
    private Hashtable<Serializable, Session> map = new Hashtable<Serializable, Session>();
    
    private final SessionStorage storage;
    
    public SSOSessionDAO(){
    	this.storage = new SessionStorage("ordsSessionStore");
        storage.initStore(map);
    }

	@Override
	protected void doDelete(Session session) {
        load();
        map.remove(session.getId());
        store();
	}

	@Override
	protected void doUpdate(Session session) {
        load();
        map.put(session.getId(), session);
        store();	
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

        load();
        map.put(sessionId, session);
        store();
        return sessionId;
	}

	@Override
	protected Session doReadSession(Serializable sessionId) {
        load();
        return map.get(sessionId);
	}
	
    private synchronized void store() {
        storage.store(map);
    }

    @SuppressWarnings("unchecked")
	private synchronized void load() {
        map = (Hashtable<Serializable, Session>) storage.load();
    }

}
