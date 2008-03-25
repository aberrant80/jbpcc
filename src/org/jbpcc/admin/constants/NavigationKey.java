
package org.jbpcc.admin.constants;


/**
 * Navigation keys are enumerations for page navigation.
 */
public enum NavigationKey {
    LOGIN_SUCCESS("success"),
    LOGIN_FAILURE("failure"),
    LOGOUT("logout"),
    ADMIN("administration"),
    MONITOR("monitoring");

    private String key;

    NavigationKey(String navigateKey) {
        this.key = navigateKey;
    }

    public String getKey() {
        return key;
    }
}
