

package org.jbpcc.admin.util;



public interface ResourceBundleProvider {

    /**
     * Returns the localised string of the specified {@code key} argument.
     * 
     * @param key
     *            The resource key. This argument must not be {@code null} or empty.
     * 
     * @return The localised string of the specified {@code key}.
     * @throws IllegalArgumentException
     *             Thrown if the specified {@code key} argument is either {@code null} or empty.
     * @see #getCurrentUserLocaleInContext()
     * @see ResourceBundle#getString(String)
     */
    String getLocalizedString(String key) throws IllegalArgumentException;

    /**
     * Return the localised string of the specified {@code key} argument.
     * 
     * @param key
     *            The resource key. This argument must not be {@code null} or empty.
     * @param resBundle
     *            reference to specific resource bundle.
     * 
     * @return The localised string of the specified {@code key}.
     * @throws IllegalArgumentException
     *             Thrown if the specified {@code key} argument is either {@code null} or empty.
     * @see #getCurrentUserLocaleInContext()
     * @see ResourceBundle#getString(String)
     */
    String getLocalizedString(String key, String resBundle);
}
