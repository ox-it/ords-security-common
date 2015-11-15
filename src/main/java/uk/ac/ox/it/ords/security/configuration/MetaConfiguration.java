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

import java.io.File;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.DefaultConfigurationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Local configuration manager
 */
public class MetaConfiguration {

	public static Logger log = LoggerFactory.getLogger(MetaConfiguration.class);

	private static Configuration config;
	
	private static final String ORDS_CONFIG_FILENAME = "config.xml";
	
	/**
	 * Load meta-configuration
	 */
    private static void load(){
		try {
			
			String ordsConfigurationLocation = "";
			final String ORDS_CONF_DIR = System.getenv("ORDS_CONF_DIR");
			if (ORDS_CONF_DIR != null){
				ordsConfigurationLocation = ORDS_CONF_DIR + File.separator + ORDS_CONFIG_FILENAME;
			} else {
				ordsConfigurationLocation = ORDS_CONFIG_FILENAME;
			}
			
			DefaultConfigurationBuilder factory = new DefaultConfigurationBuilder(ordsConfigurationLocation);
			factory.load();
			config = factory.getConfiguration();
		} catch (Exception e) {
			config = null;
			log.warn("No server configuration location set; using defaults");
		}
	}
    
    public static Configuration getConfiguration(){
    	if (config == null) load();
    	return config;
    }
}
