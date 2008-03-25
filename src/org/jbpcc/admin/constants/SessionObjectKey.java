package org.jbpcc.admin.constants;


/**
 * Session keys are an enumeration of the objects stored inside a session.
 */
public enum SessionObjectKey {
    LOGIN("loginVO"),
    SERVER_OPERATION("serverOperation"),
    SCHE_SERVER_OPERATION("scheServerOperation");

    private String key;

    SessionObjectKey(String sessionKey) {
        this.key = sessionKey;
    }

    public String getKey() {
        return key;
    }
}
