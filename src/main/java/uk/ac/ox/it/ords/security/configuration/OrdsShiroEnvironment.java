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
package uk.ac.ox.it.ords.security.configuration;

import org.apache.shiro.web.env.IniWebEnvironment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OrdsShiroEnvironment extends IniWebEnvironment{
	
	protected static String SHIRO_CONFIGURATION_PROPERTY = "ords.shiro.configuration";

	static Logger log = LoggerFactory.getLogger(OrdsShiroEnvironment.class);

	@Override
	public void init() {
		
		//
		// Load via metaconfiguration
		//
		String shiroConfigLocation = MetaConfiguration.getConfiguration().getString(SHIRO_CONFIGURATION_PROPERTY);
		
		if (shiroConfigLocation == null){
			log.warn("No hibernate configuration found; using default shiro.ini");
		} else {
			log.info("Shiro configuration found; using configuration from " + shiroConfigLocation);
			
			
			this.setConfigLocations(shiroConfigLocation);
			this.setIni(this.getSpecifiedIni(this.getConfigLocations()));			
		}
		
		super.init();
	}


	
	
}
