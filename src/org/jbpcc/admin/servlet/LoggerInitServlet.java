/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jbpcc.admin.servlet;


import javax.servlet.http.*;
import org.apache.log4j.xml.DOMConfigurator;

/**
 *
 * @author jameskhoo
 */
public class LoggerInitServlet extends HttpServlet {

    public void init() {
        String prefix = getServletContext().getRealPath("/");
        String file = getInitParameter("log4j-init-file");
        System.out.println("Init log config ->" + prefix + file);
        // if the log4j-init-file is not set, then no point in trying
        if (file != null) {
            DOMConfigurator.configure(prefix + file);
        }
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse res) {
    }
}

