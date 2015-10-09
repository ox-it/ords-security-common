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

import static org.junit.Assert.assertEquals;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.junit.Test;

public class SSORealmTest extends SSORealm{
	
	@Test
	public void checkToken(){
		SSORealm realm = new SSORealm();
		RemoteUserToken token = new RemoteUserToken("pingu", "antarctica");
		AuthenticationInfo info = realm.doGetAuthenticationInfo(token);
		assertEquals("pingu", info.getPrincipals().getPrimaryPrincipal().toString());
		assertEquals("antarctica", info.getCredentials().toString());
	}
	
	@Test (expected=AuthenticationException.class)
	public void checkNullToken(){
		SSORealm realm = new SSORealm();
		AuthenticationInfo info = realm.doGetAuthenticationInfo(null);
		assertEquals(null, info);
	}
	
	@Test (expected=AuthenticationException.class)
	public void checkEmptyToken(){
		SSORealm realm = new SSORealm();
		RemoteUserToken token = new RemoteUserToken("", "antarctica");
		AuthenticationInfo info = realm.doGetAuthenticationInfo(token);
		assertEquals(null, info);
	}
	
	@Test (expected=AuthenticationException.class)
	public void checkWhitespaceToken(){
		SSORealm realm = new SSORealm();
		RemoteUserToken token = new RemoteUserToken(" ", "");
		AuthenticationInfo info = realm.doGetAuthenticationInfo(token);
		assertEquals(null, info);
	}
	
	//
	// Just check all the constructors work ok ...
	//
	@Test
	public void constructors(){
		new SSORealm();
		new SSORealm(new MemoryConstrainedCacheManager());
		new SSORealm(new MemoryConstrainedCacheManager(), new SimpleCredentialsMatcher());
		new SSORealm(new SimpleCredentialsMatcher());
	}

}
