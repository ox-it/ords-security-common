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
package uk.ac.ox.it.ords.security.hmac;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.security.SignatureException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.junit.Test;
import org.mockito.Mockito;

import uk.ac.ox.it.ords.security.hmac.Hmac;

public class HmacTest {
	
	private HttpServletRequest createRequest(String host, String method, String uri, Map<String,String> params){
		HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
		Mockito.when(request.getParameterMap()).thenReturn(params);
		Mockito.when(request.getHeader("Host")).thenReturn("ords.it.ac.uk");
		Mockito.when(request.getMethod()).thenReturn("POST");
		Mockito.when(request.getRequestURI()).thenReturn("/");
		
		for (String param : params.keySet()){
			Mockito.when(request.getParameter(param)).thenReturn(params.get(param));
		}
		return request;
	}

	@Test
	public void basicSignedRequest() throws SignatureException{
		Map<String, String> consumers = new HashMap<String, String>();
		consumers.put("TEST", "secret");
		
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("test", "test-value");
		params.put("timestamp", Hmac.getFormattedDate(new Date(System.currentTimeMillis())));
		params.put("nonce", UUID.randomUUID().toString());
		
		HttpServletRequest request = this.createRequest("ords.it.ac.uk", "POST", "/", params);
		
		String query = Hmac.getCanonicalParameters(params);
		String reqString = Hmac.getCanonicalRequest("POST", "ords.it.ac.uk", "/", query);
		String signature = Hmac.getHmac(reqString, "secret");
		Mockito.when(request.getHeader("Authorization")).thenReturn("TEST "+signature);
		
		assertTrue(Hmac.isValidSignedRequest(request, consumers));
	}
	
	@Test
	public void basicSignedRequestCases() throws SignatureException{
		Map<String, String> consumers = new HashMap<String, String>();
		consumers.put("TEST", "secret");
			
		Map<String, String> params = new HashMap<String, String>();
		params.put("test", "test-value");
		params.put("timestamp", Hmac.getFormattedDate(new Date(System.currentTimeMillis())));
		params.put("nonce", UUID.randomUUID().toString());
		
		HttpServletRequest request = this.createRequest("ords.IT.ac.uk", "PoST", "/", params);
		
		String query = Hmac.getCanonicalParameters(params);
		String reqString = Hmac.getCanonicalRequest("POST", "ords.it.ac.uk", "/", query);
		String signature = Hmac.getHmac(reqString, "secret");
		Mockito.when(request.getHeader("Authorization")).thenReturn("TEST "+signature);
		
		assertTrue(Hmac.isValidSignedRequest(request, consumers));
	}
	
	@Test
	public void signedRequestWithNoTimestamp() throws SignatureException{
		Map<String, String> consumers = new HashMap<String, String>();
		consumers.put("TEST", "secret");
			
		Map<String, String> params = new HashMap<String, String>();
		params.put("test", "test-value");
		params.put("nonce", UUID.randomUUID().toString());
		
		HttpServletRequest request = this.createRequest("ords.IT.ac.uk", "PoST", "/", params);
		
		String query = Hmac.getCanonicalParameters(params);
		String reqString = Hmac.getCanonicalRequest("POST", "ords.it.ac.uk", "/", query);
		String signature = Hmac.getHmac(reqString, "secret");
		Mockito.when(request.getHeader("Authorization")).thenReturn("TEST "+signature);
		
		assertFalse(Hmac.isValidSignedRequest(request, consumers));
	}
	
	@Test
	public void signedRequestTwoMinutesOld() throws SignatureException{
		Map<String, String> consumers = new HashMap<String, String>();
		consumers.put("TEST", "secret");
		
		long time = Calendar.getInstance().getTimeInMillis()-120000L; // set to 2 mins ago
			
		Map<String, String> params = new HashMap<String, String>();
		params.put("test", "test-value");
		params.put("timestamp", Hmac.getFormattedDate(new Date(time)));
		params.put("nonce", UUID.randomUUID().toString());
		
		HttpServletRequest request = this.createRequest("ords.IT.ac.uk", "PoST", "/", params);
		
		String query = Hmac.getCanonicalParameters(params);
		String reqString = Hmac.getCanonicalRequest("POST", "ords.it.ac.uk", "/", query);
		String signature = Hmac.getHmac(reqString, "secret");
		Mockito.when(request.getHeader("Authorization")).thenReturn("TEST "+signature);
		
		assertTrue(Hmac.isValidSignedRequest(request, consumers));
	}
	
	@Test
	public void signedRequestOneHourOld() throws SignatureException{
		Map<String, String> consumers = new HashMap<String, String>();
		consumers.put("TEST", "secret");
		
		long time = System.currentTimeMillis()-3600000L; // set to one hour ago
			
		Map<String, String> params = new HashMap<String, String>();
		params.put("test", "test-value");
		params.put("timestamp", Hmac.getFormattedDate(new Date(time)));
		params.put("nonce", UUID.randomUUID().toString());
		
		HttpServletRequest request = this.createRequest("ords.IT.ac.uk", "PoST", "/", params);
		
		String query = Hmac.getCanonicalParameters(params);
		String reqString = Hmac.getCanonicalRequest("POST", "ords.it.ac.uk", "/", query);
		String signature = Hmac.getHmac(reqString, "secret");
		Mockito.when(request.getHeader("Authorization")).thenReturn("TEST "+signature);
		
		assertFalse(Hmac.isValidSignedRequest(request, consumers));
	}
	

	// Set authz header with signature but no key
	@Test
	public void signedRequestWithNoApiKey() throws SignatureException{
		
		Map<String, String> consumers = new HashMap<String, String>();
		consumers.put("TEST", "secret");
		
		long time = System.currentTimeMillis();
			
		Map<String, String> params = new HashMap<String, String>();
		params.put("test", "test-value");
		params.put("timestamp", Hmac.getFormattedDate(new Date(time)));
		params.put("nonce", UUID.randomUUID().toString());
		
		HttpServletRequest request = this.createRequest("ords.IT.ac.uk", "PoST", "/", params);
		
		String query = Hmac.getCanonicalParameters(params);
		String reqString = Hmac.getCanonicalRequest("POST", "ords.it.ac.uk", "/", query);
		String signature = Hmac.getHmac(reqString, "secret");
		Mockito.when(request.getHeader("Authorization")).thenReturn(signature);
		
		assertFalse(Hmac.isValidSignedRequest(request, consumers));
	}
	
	// Set authz header with extra info
	@Test
	public void signedRequestWithExtraInfo() throws SignatureException{
		
		Map<String, String> consumers = new HashMap<String, String>();
		consumers.put("TEST", "secret");
		
		long time = System.currentTimeMillis();
			
		Map<String, String> params = new HashMap<String, String>();
		params.put("test", "test-value");
		params.put("timestamp", Hmac.getFormattedDate(new Date(time)));
		params.put("nonce", UUID.randomUUID().toString());
		
		HttpServletRequest request = this.createRequest("ords.IT.ac.uk", "PoST", "/", params);
		
		String query = Hmac.getCanonicalParameters(params);
		String reqString = Hmac.getCanonicalRequest("POST", "ords.it.ac.uk", "/", query);
		String signature = Hmac.getHmac(reqString, "secret");
		Mockito.when(request.getHeader("Authorization")).thenReturn("TEST "+signature + "EXTRASTUFF");
		
		assertFalse(Hmac.isValidSignedRequest(request, consumers));
	}
	
	// Set authz header with key but no signaure	
	@Test
	public void unsignedRequestWithApiKey() throws SignatureException{
		Map<String, String> consumers = new HashMap<String, String>();
		consumers.put("TEST", "secret");
		
		long time = System.currentTimeMillis();
			
		Map<String, String> params = new HashMap<String, String>();
		params.put("test", "test-value");
		params.put("timestamp", Hmac.getFormattedDate(new Date(time)));
		params.put("nonce", UUID.randomUUID().toString());
		
		HttpServletRequest request = this.createRequest("ords.IT.ac.uk", "PoST", "/", params);
		
		Mockito.when(request.getHeader("Authorization")).thenReturn("TEST");
		
		assertFalse(Hmac.isValidSignedRequest(request, consumers));
	}
	
	// Set authz header with key but no signaure
	@Test
	public void unsignedRequestEmptyHeader() throws SignatureException{
		Map<String, String> consumers = new HashMap<String, String>();
		consumers.put("TEST", "secret");
		
		long time = System.currentTimeMillis();
			
		Map<String, String> params = new HashMap<String, String>();
		params.put("test", "test-value");
		params.put("timestamp", Hmac.getFormattedDate(new Date(time)));
		params.put("nonce", UUID.randomUUID().toString());
		
		HttpServletRequest request = this.createRequest("ords.IT.ac.uk", "PoST", "/", params);
		
		Mockito.when(request.getHeader("Authorization")).thenReturn("");
		
		assertFalse(Hmac.isValidSignedRequest(request, consumers));
	}
	
	// Wrong secret for key used to sign request
	@Test
	public void signedRequestSignedWithBadSecret() throws SignatureException{
		Map<String, String> consumers = new HashMap<String, String>();
		consumers.put("TEST", "secret");
		
		long time = System.currentTimeMillis();
			
		Map<String, String> params = new HashMap<String, String>();
		params.put("test", "test-value");
		params.put("timestamp", Hmac.getFormattedDate(new Date(time)));
		params.put("nonce", UUID.randomUUID().toString());
		
		HttpServletRequest request = this.createRequest("ords.IT.ac.uk", "PoST", "/", params);
		
		String query = Hmac.getCanonicalParameters(params);
		String reqString = Hmac.getCanonicalRequest("POST", "ords.it.ac.uk", "/", query);
		String signature = Hmac.getHmac(reqString, "wrongkey");
		Mockito.when(request.getHeader("Authorization")).thenReturn("TEST "+signature);
		
		assertFalse(Hmac.isValidSignedRequest(request, consumers));
	}
	
	@Test
	public void signedRequestSignedWithBadSignature() throws SignatureException{
		Map<String, String> consumers = new HashMap<String, String>();
		consumers.put("TEST", "secret");
		
		long time = System.currentTimeMillis();
			
		Map<String, String> params = new HashMap<String, String>();
		params.put("test", "test-value");
		params.put("timestamp", Hmac.getFormattedDate(new Date(time)));
		params.put("nonce", UUID.randomUUID().toString());
		
		HttpServletRequest request = this.createRequest("ords.IT.ac.uk", "PoST", "/", params);
		
		String query = Hmac.getCanonicalParameters(params);
		String reqString = Hmac.getCanonicalRequest("POST", "ords.it.ac.uk", "/", query);
		String signature = Hmac.getHmac(reqString, "secret");
		Mockito.when(request.getHeader("Authorization")).thenReturn("TEST "+signature +"9");
		
		assertFalse(Hmac.isValidSignedRequest(request, consumers));
	}
	
	@Test
	public void signedRequestSignedWithBadApiKey() throws SignatureException{
		Map<String, String> consumers = new HashMap<String, String>();
		consumers.put("TEST", "secret");
		
		long time = System.currentTimeMillis();
			
		Map<String, String> params = new HashMap<String, String>();
		params.put("test", "test-value");
		params.put("timestamp", Hmac.getFormattedDate(new Date(time)));
		params.put("nonce", UUID.randomUUID().toString());
		
		HttpServletRequest request = this.createRequest("ords.IT.ac.uk", "PoST", "/", params);
		
		String query = Hmac.getCanonicalParameters(params);
		String reqString = Hmac.getCanonicalRequest("POST", "ords.it.ac.uk", "/", query);
		String signature = Hmac.getHmac(reqString, "secret");
		Mockito.when(request.getHeader("Authorization")).thenReturn("BANANA "+signature);
		
		assertFalse(Hmac.isValidSignedRequest(request, consumers));
	}
	
	@Test
	public void signedRequestWithBadTimeStamp() throws SignatureException{
		Map<String, String> consumers = new HashMap<String, String>();
		consumers.put("TEST", "secret");
				
		Map<String, String> params = new HashMap<String, String>();
		params.put("test", "test-value");
		params.put("timestamp", "999999999999");
		params.put("nonce", UUID.randomUUID().toString());
		
		HttpServletRequest request = this.createRequest("ords.IT.ac.uk", "PoST", "/", params);
		
		String query = Hmac.getCanonicalParameters(params);
		String reqString = Hmac.getCanonicalRequest("POST", "ords.it.ac.uk", "/", query);
		String signature = Hmac.getHmac(reqString, "secret");
		Mockito.when(request.getHeader("Authorization")).thenReturn("TEST "+signature);
		
		assertFalse(Hmac.isValidSignedRequest(request, consumers));
	}
	
	@Test
	public void unsignedRequest() throws SignatureException{
		
		Map<String, String> consumers = new HashMap<String, String>();
		consumers.put("TEST", "secret");
		
		long time = System.currentTimeMillis();
			
		Map<String, String> params = new HashMap<String, String>();
		params.put("test", "test-value");
		params.put("timestamp", Hmac.getFormattedDate(new Date(time)));
		params.put("nonce", UUID.randomUUID().toString());
		
		HttpServletRequest request = this.createRequest("ords.IT.ac.uk", "PoST", "/", params);
		
		String query = Hmac.getCanonicalParameters(params);
		String reqString = Hmac.getCanonicalRequest("POST", "ords.it.ac.uk", "/", query);
		String signature = Hmac.getHmac(reqString, "secret");
		
		assertFalse(Hmac.isValidSignedRequest(request, consumers));
	}
	
	@Test
	public void signedRequestWithNoNonce() throws SignatureException{
		
		Map<String, String> consumers = new HashMap<String, String>();
		consumers.put("TEST", "secret");
		
		long time = System.currentTimeMillis();
			
		Map<String, String> params = new HashMap<String, String>();
		params.put("test", "test-value");
		params.put("timestamp", Hmac.getFormattedDate(new Date(time)));
		
		HttpServletRequest request = this.createRequest("ords.IT.ac.uk", "PoST", "/", params);
		
		String query = Hmac.getCanonicalParameters(params);
		String reqString = Hmac.getCanonicalRequest("POST", "ords.it.ac.uk", "/", query);
		String signature = Hmac.getHmac(reqString, "secret");
		Mockito.when(request.getHeader("Authorization")).thenReturn("TEST "+signature);
		
		assertFalse(Hmac.isValidSignedRequest(request, consumers));
		
		
	}
	
	@Test
	public void signedRequestWithReusedNonce() throws SignatureException{
		
		Map<String, String> consumers = new HashMap<String, String>();
		consumers.put("TEST", "secret");
		
		String nonce = UUID.randomUUID().toString();
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("test", "test-value");
		params.put("timestamp", Hmac.getFormattedDate(new Date(System.currentTimeMillis())));
		params.put("nonce", nonce);
		
		HttpServletRequest request = this.createRequest("ords.it.ac.uk", "POST", "/", params);
		
		String query = Hmac.getCanonicalParameters(params);
		String reqString = Hmac.getCanonicalRequest("POST", "ords.it.ac.uk", "/", query);
		String signature = Hmac.getHmac(reqString, "secret");
		Mockito.when(request.getHeader("Authorization")).thenReturn("TEST "+signature);
		
		assertTrue(Hmac.isValidSignedRequest(request, consumers));
		
		
		params = new HashMap<String, String>();
		params.put("test", "test-value");
		params.put("timestamp", Hmac.getFormattedDate(new Date(System.currentTimeMillis())));
		params.put("nonce", nonce);
		request = this.createRequest("ords.it.ac.uk", "POST", "/", params);
		
		query = Hmac.getCanonicalParameters(params);
		reqString = Hmac.getCanonicalRequest("POST", "ords.it.ac.uk", "/", query);
		signature = Hmac.getHmac(reqString, "secret");
		Mockito.when(request.getHeader("Authorization")).thenReturn("TEST "+signature);

		assertFalse(Hmac.isValidSignedRequest(request, consumers));
	}

	@Test
	public void signedRequestEmptyNonce() throws SignatureException{
		Map<String, String> consumers = new HashMap<String, String>();
		consumers.put("TEST", "secret");
		
		String nonce = "";
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("test", "test-value");
		params.put("timestamp", Hmac.getFormattedDate(new Date(System.currentTimeMillis())));
		params.put("nonce", nonce);
		
		HttpServletRequest request = this.createRequest("ords.it.ac.uk", "POST", "/", params);
		
		String query = Hmac.getCanonicalParameters(params);
		String reqString = Hmac.getCanonicalRequest("POST", "ords.it.ac.uk", "/", query);
		String signature = Hmac.getHmac(reqString, "secret");
		Mockito.when(request.getHeader("Authorization")).thenReturn("TEST "+signature);
		
		assertFalse(Hmac.isValidSignedRequest(request, consumers));
	}
	
}
