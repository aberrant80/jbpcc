/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jbpcc.admin.util;

import java.util.logging.Level;
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
   
   public void setProperty(String paraName, String paraValue) {
        try {
            config.setProperty(paraName, paraValue);
            config.save();
        } catch (ConfigurationException ex) {
           ex.printStackTrace();
        }
   }
     

}
