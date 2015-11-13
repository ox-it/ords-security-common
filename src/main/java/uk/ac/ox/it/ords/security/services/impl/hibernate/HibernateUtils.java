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

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

import uk.ac.ox.it.ords.security.configuration.MetaConfiguration;

public class HibernateUtils
{
	private static SessionFactory sessionFactory;
	private static ServiceRegistry serviceRegistry;
	
	private static void init()
	{
		try
		{
			Configuration configuration;
			String hibernateConfigLocation = MetaConfiguration.getConfiguration().getString("hibernate.configuration");			
			if (hibernateConfigLocation == null){
				configuration = new Configuration().configure();
			} else {
				configuration = new Configuration().configure(hibernateConfigLocation);
			}
			serviceRegistry = new ServiceRegistryBuilder().applySettings(configuration.getProperties()).buildServiceRegistry();
			sessionFactory = configuration.buildSessionFactory(serviceRegistry);
		}
		catch (HibernateException he)
		{
			throw new ExceptionInInitializerError(he);
		}
	}

	public static SessionFactory getSessionFactory()
	{
		if (sessionFactory == null) init();
		return sessionFactory;
	}

	public static void closeSession() {
		if (sessionFactory.getCurrentSession().getTransaction().isActive()){
		}
		sessionFactory.getCurrentSession().close();
		
	} 
}
