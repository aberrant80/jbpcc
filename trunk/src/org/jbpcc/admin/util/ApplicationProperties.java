

package org.jbpcc.admin.util;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;

public class ApplicationProperties {
    private static final Logger LOGGER = Logger.getLogger(ApplicationProperties.class.getName());
    private static ApplicationProperties instance = null;
    private static PropertiesConfiguration config = null;
    
    private ApplicationProperties() {    
    }
    
    public static void init(String fileName) {
        try {
            config = new PropertiesConfiguration(fileName);
        } catch (ConfigurationException ex) {
           LOGGER.error(ex, ex.getCause());
        }
        
    }
    
    public static synchronized  ApplicationProperties getInstance() {
        if (instance == null ) {
            instance = new ApplicationProperties();
            
        }
        return instance;
    }
    
   public String getProperty(String paraName) {
       String value = null;
       if(config != null && config.containsKey(paraName)) {
           value = config.getString(paraName);
       }
       return value;
   }    
     

}
