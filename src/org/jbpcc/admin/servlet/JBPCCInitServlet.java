
package org.jbpcc.admin.servlet;


import javax.servlet.http.*;
import org.apache.log4j.xml.DOMConfigurator;
import org.jbpcc.admin.util.ApplicationProperties;

/** 
 * A Servlet to initialize log4j logger and JBPCC application properties.
 */
public class JBPCCInitServlet extends HttpServlet {

    public void init() {
        String prefix = getServletContext().getRealPath("/");
        String file = getInitParameter("log4j-init-file");
        System.out.println("Init log config ->" + prefix + file);
        // if the log4j-init-file is not set, then no point in trying
        if (file != null) {
            DOMConfigurator.configure(prefix + file);
        }
        
        String propertiesFile = getInitParameter("jbpcc-application-properties");
        ApplicationProperties.getInstance().init(prefix + propertiesFile);
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse res) {
    }
}

