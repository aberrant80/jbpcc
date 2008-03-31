

package org.jbpcc.admin.util;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

public class ApplicationProperties {
    private static ApplicationProperties instance = null;
    private static PropertiesConfiguration config = null;
    
    private ApplicationProperties() {    
    }
    
    public static void init(String fileName) {
        try {
            config = new PropertiesConfiguration(fileName);
        } catch (ConfigurationException ex) {
           ex.printStackTrace();
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
