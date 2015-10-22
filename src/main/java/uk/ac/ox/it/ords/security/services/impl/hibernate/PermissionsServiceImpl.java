package uk.ac.ox.it.ords.security.services.impl.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.ox.it.ords.security.model.Permission;
import uk.ac.ox.it.ords.security.services.PermissionsService;
import uk.ac.ox.it.ords.security.services.impl.AbstractPermissionsService;

public class PermissionsServiceImpl extends AbstractPermissionsService
		implements PermissionsService {

	private static Logger log = LoggerFactory.getLogger(PermissionsServiceImpl.class);

	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	public PermissionsServiceImpl(){
		setSessionFactory (HibernateUtils.getSessionFactory());
	}

	public void createPermission(Permission permission) throws Exception {
		Session session = this.sessionFactory.getCurrentSession();
		session.beginTransaction();
		try {
			session.save(permission);
			session.getTransaction().commit();
		} catch (Exception e) {
			log.error("Error creating permission", e);
			session.getTransaction().rollback();
			throw new Exception("Cannot create project",e);
		} finally {
			HibernateUtils.closeSession();
		}
		
	}

	public void deletePermission(Permission permission) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
