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


package org.jbpcc.admin.servlet.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.apache.log4j.xml.DOMConfigurator;
import org.jbpcc.admin.util.ApplicationProperties;

/**
 * The <code>StartUpListener</code> for JBPCC prepares the necessary properties required for proper functioning of
 * the web application.
 */
public class StartUpListener implements ServletContextListener {

    public void contextDestroyed(ServletContextEvent event) {
    }

    public void contextInitialized(ServletContextEvent event) {
        String prefix = event.getServletContext().getRealPath("/");
        String file = (String) event.getServletContext().getInitParameter("log4j-init-file");
        System.out.println("Init log config ->" + prefix + file);
        // if the log4j-init-file is not set, then no point in trying
        
        if (file != null) {
            DOMConfigurator.configure(prefix + file);
        }
        
        String propertiesFile = (String) event.getServletContext().getInitParameter("jbpcc-application-properties");
        ApplicationProperties.init(prefix + propertiesFile);
         
    }

}
