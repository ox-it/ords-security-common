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

import uk.ac.ox.it.ords.security.model.Audit;
import uk.ac.ox.it.ords.security.services.AuditService;

public class AuditServiceImpl implements AuditService {
	
	Logger log = LoggerFactory.getLogger(AuditServiceImpl.class);
	
	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	public AuditServiceImpl() {
		setSessionFactory (HibernateUtils.getSessionFactory());
	}

	/* (non-Javadoc)
	 * @see uk.ac.ox.it.ords.security.services.AuditService#createNotAuthRecord(java.lang.String, int)
	 */
	public void createNotAuthRecord(String request) {
		Audit audit = new Audit();
		audit.setAuditType(Audit.AuditType.GENERIC_NOTAUTH.name());
		audit.setMessage(request);
		createNewAudit(audit);
	}

	/* (non-Javadoc)
	 * @see uk.ac.ox.it.ords.security.services.AuditService#getAuditListForProject(int)
	 */
	@SuppressWarnings("unchecked")
	public List<Audit> getAuditListForProject(int projectId) {
		Session session = this.sessionFactory.getCurrentSession();
		List<Audit> auditRecords = null;
		try {
			session.beginTransaction();
			auditRecords = session.createCriteria(Audit.class).add(Restrictions.eq("projectId", projectId)).list();
			session.getTransaction().commit();
		} catch (Exception e) {
			log.error("Error creating audit record", e);
			session.getTransaction().rollback();
		} finally {
			HibernateUtils.closeSession();
		}
		return auditRecords;
	}

	/* (non-Javadoc)
	 * @see uk.ac.ox.it.ords.security.services.AuditService#getAuditListForUser(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public List<Audit> getAuditListForUser(String userId) {
		Session session = this.sessionFactory.getCurrentSession();
		List<Audit> auditRecords = null;
		try {
			session.beginTransaction();
			auditRecords = session.createCriteria(Audit.class).add(Restrictions.eq("userId", userId)).list();
			session.getTransaction().commit();
		} catch (Exception e) {
			log.error("Error creating audit record", e);
			session.getTransaction().rollback();
		} finally {
			HibernateUtils.closeSession();
		}
		return auditRecords;
	}

	/* (non-Javadoc)
	 * @see uk.ac.ox.it.ords.security.services.AuditService#createNewAudit(uk.ac.ox.it.ords.security.model.Audit)
	 */
	public void createNewAudit(Audit audit) {
		Session session = this.sessionFactory.getCurrentSession();
		try {
			session.beginTransaction();
			session.save(audit);
			session.getTransaction().commit();
		} catch (Exception e) {
			log.error("Error creating audit record", e);
			session.getTransaction().rollback();
		} finally {
			HibernateUtils.closeSession();
		}
	}


}
