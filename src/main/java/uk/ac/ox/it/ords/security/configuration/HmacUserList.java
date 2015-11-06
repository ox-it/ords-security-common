package uk.ac.ox.it.ords.security.configuration;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provides access to HMAC keys for this service
 * @author scottw
 *
 */
public class HmacUserList {

	private static Logger log = LoggerFactory.getLogger(HmacUserList.class);

	public static Map<String, String> getHmacUsers(){

		Map<String, String> hmacUsers = new HashMap<String, String>();
		String hmacUserConfigLocation = MetaConfiguration.getConfigurationLocation("hmac");

		if (hmacUserConfigLocation != null){
			try {
				PropertiesConfiguration hmacUserConfiguration = new PropertiesConfiguration(hmacUserConfigLocation);

				@SuppressWarnings("rawtypes")
				Iterator keys = hmacUserConfiguration.getKeys();
				while (keys.hasNext()){
					String key = (String) keys.next();
					hmacUsers.put(key, hmacUserConfiguration.getString(key));
				}

			} catch (ConfigurationException e) {
				log.error("Problem loading HMAC user list from file");
			}
		}

		return hmacUsers;

	}
}
