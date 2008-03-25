

package org.jbpcc.admin.delegates;


import org.jbpcc.admin.constants.ErrorMessageKey;

public class BusinessException extends Exception {

    private ErrorMessageKey businessExceptionKey;
    private String[] parameters;

    public BusinessException(Throwable t, ErrorMessageKey businessExceptionKey) {
        this(t, businessExceptionKey, (String[]) null);
    }

    public BusinessException(Throwable t, ErrorMessageKey businessExceptionKey, String... parameters) {
        super(t);
        this.businessExceptionKey = businessExceptionKey;
        this.parameters = parameters;
    }

    public BusinessException(ErrorMessageKey businessExceptionKey) {
        this(businessExceptionKey, (String[]) null);
    }

    public BusinessException(ErrorMessageKey businessExceptionKey, String[] parameters) {
        super();
        this.businessExceptionKey = businessExceptionKey;
        this.parameters = parameters;
    }

    public ErrorMessageKey getBusinessExceptionKey() {
        return businessExceptionKey;
    }

    public void setBusinessExceptionKey(ErrorMessageKey businessExceptionKey) {
        this.businessExceptionKey = businessExceptionKey;
    }

    public String[] getParameters() {
        return parameters;
    }

    public void setParameters(String[] parameters) {
        this.parameters = parameters;
    }
}
