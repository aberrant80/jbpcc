package org.jbpcc.admin.util;


import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;

import org.jbpcc.admin.jsf.JsfUtil;

public class ResourceBundleUtil implements ResourceBundleProvider {
    public static final String DEFAULT_BUNDLE = "resources.messages";
    public static final String ERROR_BUNDLE = "resources.errormsg";
    
    private static final String EMPTY_RESOURCE = "-";
    private static final String MISSING_RESOURCE = "???";
    private static final Map<Locale, ResourceBundleSet> LOCALE_BUNDLES = new HashMap<Locale, ResourceBundleSet>();

    /**
     * Return the localized object of the specified {@code key} argument.
     * 
     * @param key
     *            The resouce key. This argument must not be {@code null} or empty.
     * 
     * @return The localized object of the specified {@code key}.
     * @throws IllegalArgumentException
     *             Thrown if the specified {@code key} argument is either {@code null} or empty.
     * @see java.util.ResourceBundle#getObject(String)
     */
    public Object getLocalizedObject(String key) throws IllegalArgumentException {
        ResourceBundle resourceBundle = getResourceBundleInContext();
        return resourceBundle.getObject(key);
    }

    /**
     * Return the curent resource bundle user in context. This method never returns {@code null} object.
     * 
     * @return The current resource bundle user in context.
     * @throws MissingResourceException
     *             Thrown if the resource bundle base file is not found.
     */
    private ResourceBundle getResourceBundleInContext() throws MissingResourceException {
        return getResourceBundleInContext(DEFAULT_BUNDLE);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.talgentra.tallyman.eai.admin.wac.util.ResourceBundleProvider#getLocalizedString(java.lang.String)
     */
    public String getLocalizedString(String key) throws IllegalArgumentException {
        ResourceBundle resourceBundle = getResourceBundleInContext();
        return getStringSafely(resourceBundle, key);
    }

    /**
     * Return value from the specified resource bundle using the specified resource key
     * 
     * @param bundle
     *            ResourceBundle instance
     * @param key
     *            Key to retrieve the value
     * 
     * @return value Value associated with the <code>key</code> inside the specified <code>bundle</code>
     */
    private String getStringSafely(ResourceBundle bundle, String key) {
        String value;
        try {
            value = bundle.getString(key);

            if (value == null) {
                value = EMPTY_RESOURCE;
            }
        } catch (MissingResourceException me) {
            value = MISSING_RESOURCE + key + MISSING_RESOURCE;
        }
        return value;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.talgentra.tallyman.eai.admin.wac.util.ResourceBundleProvider#getLocalizedString(java.lang.String,
     *      java.lang.String)
     */
    public String getLocalizedString(String key, String resBundle) {
        ResourceBundle resourceBundle = getResourceBundleInContext(resBundle);
        return resourceBundle.getString(key);
    }

    /**
     * Return <code>ResourceBundle</code> instance with the specified property file name and the current user locale.
     * 
     * @param resBundleKey
     *            Key to get resource bundle instance which is the property file name
     * 
     * @return resourceBundle ResourceBundle instance
     */
    private ResourceBundle getResourceBundleInContext(String resBundleKey) {
        Locale locale = getCurrentUserLocaleInContext();
        ResourceBundleSet resourceBundleSet = getResourceBundleSet(locale);
        return resourceBundleSet.getResourceBundle(resBundleKey, locale);
    }

    /**
     * Returns the current user locale in context.
     * 
     * @return The user locale in context.
     */
    public Locale getCurrentUserLocaleInContext() {
        FacesContext facesContext = JsfUtil.getCurrentFacesContext();
        UIViewRoot viewRoot = facesContext.getViewRoot();
        return viewRoot.getLocale();
    }

    /*
     * Return <code>ResourceBundleSet</code> based on current user locale in context Instantiates a new <code>ResourceBundleSet</code>
     * if no instance of <code>ResourceBundleSet</code> exists for the specified user locale <p/> Don't synchronized
     * the entire method as it slows down the performance. Only synchronised the scope when <code>resourceBundleSet</code>
     * for the specified locale does not exist
     * 
     * @param locale The current user locale in context
     * 
     * @return resourceBundleSet Instance of <code>ResourceBundleSet</code>
     */
    private ResourceBundleSet getResourceBundleSet(Locale locale) {
        ResourceBundleSet resourceBundleSet = LOCALE_BUNDLES.get(locale);

        if (resourceBundleSet == null) {
            // Ensure only one access to LOCALE_RESBUNDLE
            synchronized (LOCALE_BUNDLES) {
                // When resourceBundleSet was set at the start of this method, resourceBundleSet was null
                // Since the assignment was no synchronised, more than 1 access is allowed
                // When entering this scope, only 1 access is allowed at a time so resourceBundleSet must be set
                // again to make sure resourceBundleSet has not been instantiated
                resourceBundleSet = LOCALE_BUNDLES.get(locale);
                if (resourceBundleSet == null) {
                    resourceBundleSet = new ResourceBundleSet();
                    LOCALE_BUNDLES.put(locale, resourceBundleSet);
                }
            }
        }
        return resourceBundleSet;
    }

    /*
     * Data structure that holds different resource bundle files with the same user locale
     */
    private static class ResourceBundleSet {
        // Map file's name with ResourceBundle instance
        private HashMap<String, ResourceBundle> resourceBundleMap = new HashMap<String, ResourceBundle>();

        /**
         * Retrieve resource bundle based on the specified file's name and current user locale
         * 
         * @param resBundleKey
         *            Resource bundle's file name
         * @param locale
         *            Current user locale in context
         * 
         * @return resourceBundle Resource bundle instance
         */
        public ResourceBundle getResourceBundle(String resBundleKey, Locale locale) {
            ResourceBundle resourceBundle = resourceBundleMap.get(resBundleKey);
            if (resourceBundle == null) {
                resourceBundle = ResourceBundle.getBundle(resBundleKey, locale);
                resourceBundleMap.put(resBundleKey, resourceBundle);
            }
            return resourceBundle;
        }
    }
}
