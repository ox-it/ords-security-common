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

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
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

	/* (non-Javadoc)
	 * @see uk.ac.ox.it.ords.security.services.PermissionsService#createPermission(uk.ac.ox.it.ords.security.model.Permission)
	 */
	public void createPermission(Permission permission) throws Exception {
		Session session = this.sessionFactory.getCurrentSession();
		session.beginTransaction();
		try {
			
			//
			// Check if this already exists
			//
			Permission existingPermission = (Permission) session.createCriteria(Permission.class)
					.add(Restrictions.eq("permission", permission.getPermission()))
					.add(Restrictions.eq("role", permission.getRole()))
					.uniqueResult();
			if (existingPermission == null) { 
				session.save(permission);
			}
			session.getTransaction().commit();
		} catch (Exception e) {
			log.error("Error creating permission", e);
			session.getTransaction().rollback();
			throw new Exception("Cannot create permission",e);
		} finally {
			HibernateUtils.closeSession();
		}
		
	}

	/* (non-Javadoc)
	 * @see uk.ac.ox.it.ords.security.services.PermissionsService#deletePermission(uk.ac.ox.it.ords.security.model.Permission)
	 */
	public void deletePermission(Permission permission) throws Exception {
		Session session = this.sessionFactory.getCurrentSession();
		session.beginTransaction();
		try {
			session.delete(permission);
			session.getTransaction().commit();
		} catch (Exception e) {
			log.error("Error deleting permission", e);
			session.getTransaction().rollback();
			throw new Exception("Cannot delete permission",e);
		} finally {
			HibernateUtils.closeSession();
		}
	}

	/* (non-Javadoc)
	 * @see uk.ac.ox.it.ords.security.services.PermissionsService#getPermissionsForRole(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public List<Permission> getPermissionsForRole(String role) throws Exception {
		Session session = this.sessionFactory.getCurrentSession();
		session.beginTransaction();
		List<Permission> permissions = null;
		try {
			permissions = session.createCriteria(Permission.class)
					.add(Restrictions.eq("role", role))
					.list();
			session.getTransaction().commit();
		} catch (Exception e) {
			log.error("Error creating permission", e);
			session.getTransaction().rollback();
			throw new Exception("Cannot list permissions",e);
		} finally {
			HibernateUtils.closeSession();
		}
		return permissions;
	}

}
