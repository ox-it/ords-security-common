package uk.ac.ox.it.ords.security.services.impl.hibernate;

import org.apache.commons.lang.SerializationUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.SimpleSession;
import org.hibernate.SessionFactory;

import uk.ac.ox.it.ords.security.SimplePersistentSession;
import uk.ac.ox.it.ords.security.services.SessionStorageService;

public class SessionStorageServiceImpl implements SessionStorageService {
	
	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	public SessionStorageServiceImpl(){
		setSessionFactory (HibernateUtils.getSessionFactory());
	}
	
	private Session fromSimplePersistentSession(SimplePersistentSession sps){
		return (Session) SerializationUtils.deserialize(sps.getSession());
	}
	
	private SimplePersistentSession toSimplePersistentSession(SimpleSession session){
		SimplePersistentSession sps = new SimplePersistentSession();
		sps.setSessionId(session.getId().toString());
		sps.setSession(SerializationUtils.serialize(session));
		return sps;
	}

	public Session readSession(String sessionId) throws Exception {
		org.hibernate.Session hsession = this.sessionFactory.getCurrentSession();
		hsession.beginTransaction();
		
		try {
			SimplePersistentSession sps = (SimplePersistentSession) hsession.get(SimplePersistentSession.class, sessionId);
			hsession.getTransaction().commit();
			return fromSimplePersistentSession(sps);
		} catch (Exception e) {
			hsession.getTransaction().rollback();
			throw new Exception("Cannot load session",e);
		} finally {
			HibernateUtils.closeSession();
		}
	}

	public void createSession(String sessionId, Session session)  throws Exception{
		org.hibernate.Session hsession = this.sessionFactory.getCurrentSession();
		hsession.beginTransaction();
		SimplePersistentSession sps = toSimplePersistentSession((SimpleSession)session);
		try {
			hsession.save(sps);
			hsession.getTransaction().commit();
		} catch (Exception e) {
			hsession.getTransaction().rollback();
			throw new Exception("Cannot save session",e);
		} finally {
			HibernateUtils.closeSession();
		}
	}

	public void updateSession(String sessionId, Session session)  throws Exception{
		org.hibernate.Session hsession = this.sessionFactory.getCurrentSession();
		hsession.beginTransaction();
		SimplePersistentSession sps = toSimplePersistentSession((SimpleSession)session);
		try {
			hsession.update(sps);
			hsession.getTransaction().commit();
		} catch (Exception e) {
			hsession.getTransaction().rollback();
			throw new Exception("Cannot save session",e);
		} finally {
			HibernateUtils.closeSession();
		}
	}

	public void deleteSession(String sessionId)  throws Exception{
		
		SimpleSession simpleSession = (SimpleSession) readSession(sessionId);
		
		org.hibernate.Session hsession = this.sessionFactory.getCurrentSession();
		hsession.beginTransaction();
		try {
			SimplePersistentSession sps = toSimplePersistentSession(simpleSession);
			hsession.delete(sps);
			hsession.getTransaction().commit();
		} catch (Exception e) {
			hsession.getTransaction().rollback();
			throw new Exception("Cannot delete session",e);
		} finally {
			HibernateUtils.closeSession();
		}
	}

}
