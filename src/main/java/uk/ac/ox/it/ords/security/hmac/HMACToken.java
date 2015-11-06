package uk.ac.ox.it.ords.security.hmac;

import org.apache.shiro.authc.AuthenticationToken;

public class HMACToken implements AuthenticationToken {

	private Object principal;
	private Object credentials;
	
	public HMACToken(){
	}
	
	public HMACToken(Object principal, Object credentials){
		this.principal = principal;
		this.credentials = credentials;
	}

	public Object getPrincipal() {
		return principal;
	}

	public Object getCredentials() {
		return credentials;
	}

	public void setPrincipal(Object principal) {
		this.principal = principal;
	}

	public void setCredentials(Object credentials) {
		this.credentials = credentials;
	}
}