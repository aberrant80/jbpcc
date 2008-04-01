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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.apache.log4j.xml.DOMConfigurator;
import org.jbpcc.admin.util.ApplicationProperties;
import org.jbpcc.admin.util.DBUtil;

/**
 * The <code>StartUpListener</code> for JBPCC prepares the necessary properties required for proper functioning of
 * the web application.
 */
public class StartUpListener implements ServletContextListener {

    private static String JBPCC_DB_DRIVER_KEY = "jbpcc.db.driver";
    private static String JBPCC_DB_URL_KEY = "jbpcc.db.url";
    private static String JBPCC_DB_DRIVER_TOKEN = "%jbpcc.db.driver%";
    private static String JBPCC_DB_URL_TOKEN = "%jbpcc.db.url%";
    private static String JBPCC_PROPERTIES_DIR = File.separator + "WEB-INF" + File.separator + "properties" + File.separator;
    private static String JBPCC_JPA_TEMPLATE_FILE_NAME = "persistence_template.xml";
    private static String JBPCC_JPA_CONFIG_FILE_NAME = "persistence.xml";

    public void contextDestroyed(ServletContextEvent event) {
    }

    public void contextInitialized(ServletContextEvent event) {
        String prefix = event.getServletContext().getRealPath("/");
        String file = (String) event.getServletContext().getInitParameter("log4j-init-file");

        if (file != null) {
            DOMConfigurator.configure(prefix + file);
        }

        String propertiesFile = (String) event.getServletContext().getInitParameter("jbpcc-application-properties");
        ApplicationProperties.init(prefix + propertiesFile);

        DBUtil.initJBPCCDB(prefix);
        generetePersistanceConfigFromTemplate(prefix);

    }

    private void generetePersistanceConfigFromTemplate(String contextPath) {
        File template = new File(contextPath + JBPCC_PROPERTIES_DIR + JBPCC_JPA_TEMPLATE_FILE_NAME);
        File target = new File(contextPath + JBPCC_PROPERTIES_DIR + JBPCC_JPA_CONFIG_FILE_NAME);
        try {
            System.out.println("Template at " + template);
            System.out.println("Target at " + target);
            BufferedReader in = new BufferedReader(new FileReader(template));
            BufferedWriter out = new BufferedWriter(new FileWriter(target));
            String line = null;

            while ((line = in.readLine()) != null) {
                if (line.indexOf(JBPCC_DB_DRIVER_TOKEN) >= 0) { 
                    line = line.replaceAll(JBPCC_DB_DRIVER_TOKEN, ApplicationProperties.getInstance().getProperty(JBPCC_DB_DRIVER_KEY));
                } 
                if (line.indexOf(JBPCC_DB_URL_TOKEN) >= 0) {  
                    line = line.replaceAll(JBPCC_DB_URL_TOKEN, ApplicationProperties.getInstance().getProperty(JBPCC_DB_URL_TOKEN));
                }
                out.write(line + "\n");
            }

            out.close();
            in.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

    }
}
