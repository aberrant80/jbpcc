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

package org.jbpcc.admin.jsf;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.el.MethodBinding;
import javax.faces.el.ValueBinding;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpSession;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.jsf.FacesContextUtils;


import org.jbpcc.admin.constants.ErrorMessageKey;
import org.jbpcc.admin.constants.SessionObjectKey;

import org.jbpcc.admin.util.ResourceBundleProvider;
import org.jbpcc.admin.util.ResourceBundleUtil;


/**
 * Utility class for common methods shared between backing beans and converters etc.
 */
public class JsfUtil {

    private static final String NO_RESOURCE_FOUND = "** No Resource found for ";
    private static ResourceBundleProvider resourceBundleProvider = new ResourceBundleUtil();
   
    /**
     * Accepts a reference to a JSF binding expression and returns the matching object (or creates it) Partially
     * refactored out of AbstractPetClinicBackingBean so it can be reused in converters.
     * 
     * @param ctx
     *            FacesContext
     * @param expression
     * @return Managed object
     */
    public static Object resolveExpression(FacesContext ctx, String expression) {
        Application app = ctx.getApplication();
        ValueBinding bind = app.createValueBinding(expression);
        return bind.getValue(ctx);
    }

    /**
     * Convenience method for resolving a reference to a managed bean by name rather than by expression. Refactored out
     * of AbstractPetClinicBackingBean so it can be reused in converters.
     * 
     * @param ctx
     *            FacesContext
     * @param beanName
     * @return Managed object
     */
    public static Object getManagedBeanValue(FacesContext ctx, String beanName) {
        StringBuffer buff = new StringBuffer("#{");
        buff.append(beanName);
        buff.append("}");
        return resolveExpression(ctx, buff.toString());
    }

    public static Object getManagedBeanValue(String beanName) {
        FacesContext ctx = FacesContext.getCurrentInstance();
        return getManagedBeanValue(ctx, beanName);

    }

    /**
     * Sets a new object into a JSF managed bean. Note: will fail silently if the supplied object does not match the
     * type of the managed bean.
     * 
     * @param ctx
     *            FacesContext
     * @param expression
     * @param newValue
     */
    public static void setExpressionValue(FacesContext ctx, String expression, Object newValue) {
        Application app = ctx.getApplication();
        ValueBinding bind = app.createValueBinding(expression);
        if (bind.getType(ctx) == newValue.getClass()) {
            bind.setValue(ctx, newValue);
        }
    }

    /**
     * Convenience method for setting the value of a managed bean by name rather than by expression.
     * 
     * @param ctx
     *            FacesContext
     * @param beanName
     * @param newValue
     */
    public static void setManagedBeanValue(FacesContext ctx, String beanName, Object newValue) {
        StringBuffer buff = new StringBuffer("#{");
        buff.append(beanName);
        buff.append("}");
        setExpressionValue(ctx, buff.toString(), newValue);
    }

    public static void establishNewSession() {
        FacesContext ctx = getCurrentFacesContext();
        ctx.getExternalContext().getSession(true);
    }

    /**
     * Convenience method for setting Session variables.
     * 
     * @param ctx
     *            FacesContext
     * @param key
     *            object key
     * @param object
     *            value to store
     */
    public static void storeOnSession(SessionObjectKey sessionConstants, Object object) {
        FacesContext ctx = getCurrentFacesContext();
        HttpSession session = (HttpSession) ctx.getExternalContext().getSession(true);
        session.setAttribute(sessionConstants.getKey(), object);
    }

    @SuppressWarnings("unchecked")
    public static void cleanHttpSession() {
        HttpSession session = (HttpSession) getCurrentFacesContext().getExternalContext().getSession(true);
        SessionObjectKey sessionObjKeyArray[] = SessionObjectKey.values();
        for (SessionObjectKey objectKey : sessionObjKeyArray) {
            session.removeAttribute(objectKey.getKey());
        }

        java.util.Enumeration attrs = session.getAttributeNames();
        while (attrs.hasMoreElements()) {
            String attr = (String) attrs.nextElement();
            if (attr.endsWith("Bean")) {
                session.removeAttribute(attr);
            }
        }
    }

    public static void removeFromSession(SessionObjectKey sessionConstants) {
        FacesContext ctx = getCurrentFacesContext();
        HttpSession session = (HttpSession) ctx.getExternalContext().getSession(true);
        session.removeAttribute(sessionConstants.getKey());
    }

    /**
     * Convenience method for retrieving session variables.
     * 
     * @param sessionObjectKey
     *            the session object key; cannot be <code>null</code>
     */
    public static Object getFromSession(SessionObjectKey sessionObjectKey) {
        FacesContext ctx = getCurrentFacesContext();
        HttpSession session = (HttpSession) ctx.getExternalContext().getSession(true);
        return session.getAttribute(sessionObjectKey.getKey());
    }

    /**
     * Convenience method for retrieving session variables.
     * 
     * @param session
     *            the session to retrieve it from; cannot be <code>null</code>
     * @param sessionObjectKey
     *            the session object key; cannot be <code>null</code>
     */
    public static Object getFromSession(HttpSession session, SessionObjectKey sessionObjectKey) {
        return session.getAttribute(sessionObjectKey.getKey());
    }

    /**
     * Convenience method to construct a <code>FacesMesssage</code> from a defined error key and severity. This
     * assumes that the error keys follow the convention of using the "_detail" for the detailed part of the message,
     * otherwise the main message is returned for the detail as well.
     * 
     * @param key
     *            for the error message in the resource bundle
     * @param severity
     * @return Faces Message object
     */
    public static FacesMessage getMessageFromBundle(String key, FacesMessage.Severity severity) {
        ResourceBundle bundle = getBundle();
        String summary = getStringSafely(bundle, key, null);
        String detail = getStringSafely(bundle, key + "_detail", summary);
        FacesMessage message = new FacesMessage(summary, detail);
        message.setSeverity(severity);
        return message;
    }

    /*
     * Internal method to pull out the correct local message bundle.
     */
    private static ResourceBundle getBundle() {
        FacesContext ctx = FacesContext.getCurrentInstance();
        UIViewRoot uiRoot = ctx.getViewRoot();
        Locale locale = uiRoot.getLocale();
        ClassLoader ldr = Thread.currentThread().getContextClassLoader();
        return ResourceBundle.getBundle(ctx.getApplication().getMessageBundle(), locale, ldr);
    }

    /*
     * Internal method to proxy for resource keys that don't exist.
     */
    private static String getStringSafely(ResourceBundle bundle, String key, String defaultValue) {
        String resource = null;

        try {
            resource = bundle.getString(key);
            if (resource == null) {
                if (defaultValue != null) {
                    resource = defaultValue;
                } else {
                    resource = NO_RESOURCE_FOUND + key;
                }
            }
        } catch (MissingResourceException me) {
            resource = "???<" + key + ">???";
        }
        return resource;
    }

    /**
     * Util method to construct action listener binding based on the action listener name.
     * <p>
     * Make sure that your actionListener has method signature: <code>
     * void actionListener(ActionEvent event) {
     * }
     * </code>
     * 
     * @param actionListener
     *            Name of method to be executed when action event is triggered
     * @param actionListener
     *            For example, #{activityBean.handleSave} activityBean is a bean reference defined in faces-config.xml
     *            The bean contains method handleSave
     * @return methodBinding MethodBinding object that wraps the actionListener
     */
    public static MethodBinding getActionMethodBinding(String actionListener) {
        return JsfUtil.getMethodBinding(actionListener, ActionEvent.class);
    }

    /**
     * Util method to construct method binding object based on the specified listener name. Method arguments are
     * determined from the specified event class.
     * <p>
     * Make sure that your listener has method signature: <code>
     * void eventListener(<Event> event) {
     * }
     * </code>
     * 
     * @param eventListener
     *            For example, #{activityTabListener.processTabChange} activityTabListener is a bean reference defined
     *            in faces-config.xml The bean contains method processTabChange
     * @return methodBinding MethodBinding object that wraps the event listener
     */
    public static MethodBinding getMethodBinding(String eventListener, Class<ActionEvent> eventClass) {
        Class<?> args[] = {eventClass};
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Application application = facesContext.getApplication();

        MethodBinding mb = application.createMethodBinding(eventListener, args);
        return mb;
    }

    /**
     * Return wrapper object that encapsulates the containing application environment. This means we don't have to aware
     * if this application is running in a servlet or a portlet environment.
     * 
     * @return externalContext wrapper object
     * @see javax.faces.context.ExternalContext
     */
    public static ExternalContext getExternalContext() {
        FacesContext facesContext = getCurrentFacesContext();
        return facesContext.getExternalContext();
    }

    /**
     * Return <code>FacesContext</code> instance associated with a particular request. <p/> Note: This instance
     * remains active until its <code>release()</code> method is called. While <code>FacesContext</code> instance is
     * still active, it must not be referenced from any other thread.
     * 
     * @return facesContext FacesContext instance
     */
    public static FacesContext getCurrentFacesContext() {
        return FacesContext.getCurrentInstance();
    }

    /**
     * Return root of <code>WebApplicationContext</code> associated with the current faces context.
     * <code>WebApplicationContext</code> is used for accessing Spring context from JSF code.
     * 
     * @return webAppContext WebApplicationContext instance
     */
    public static WebApplicationContext getWebApplicationInContext() {
        FacesContext facesContext = getCurrentFacesContext();
        return FacesContextUtils.getWebApplicationContext(facesContext);
    }

    public static String formatMessage(String msgCode, String resBundleKey, String... params) {
        String localizedMessage = resourceBundleProvider.getLocalizedString(msgCode, resBundleKey);
        String formattedMessage = localizedMessage;
        if (params != null) {
            formattedMessage = MessageFormat.format(localizedMessage, (Object[]) params);
        }
        return formattedMessage;
    }

    public static void addErrorMessage(String clientId, ErrorMessageKey messageKey, String... params) {
        String formattedMessage = formatMessage(messageKey.getKey(), ResourceBundleUtil.ERROR_BUNDLE, params);
        FacesMessage facesMessage = new FacesMessage(formattedMessage, formattedMessage);
        facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
        getCurrentFacesContext().addMessage(clientId, facesMessage);
    }

    public static void setResourceBundleProvider(ResourceBundleProvider resourceBundleProvider) {
        JsfUtil.resourceBundleProvider = resourceBundleProvider;
    }

}
