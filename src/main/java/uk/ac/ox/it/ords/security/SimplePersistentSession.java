package uk.ac.ox.it.ords.security;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

@Entity
@Table(name="session")
public class SimplePersistentSession {

	private static final long serialVersionUID = 360058244086959179L;
	
	@Id
	private String sessionId;
	
	@Type(type="org.hibernate.type.BinaryType") 
	private byte[] session;

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public byte[] getSession() {
		return session;
	}

	public void setSession(byte[] session) {
		this.session = session;
	}
	
	

}
