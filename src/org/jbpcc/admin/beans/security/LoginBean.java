package org.jbpcc.admin.beans.security;

import org.jbpcc.admin.constants.NavigationKey;
import org.jbpcc.admin.delegates.BusinessException;
import org.jbpcc.admin.delegates.UserLoginBD;
import org.jbpcc.admin.jsf.JsfUtil;
import org.apache.log4j.Logger;
import org.jbpcc.admin.util.ApplicationProperties;

public class LoginBean {
    
    private final Logger LOGGER = Logger.getLogger(this.getClass().getName());

    private String loginName;
    private String loginPassword;
    private UserLoginBD userLoginBD = null;

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getLoginPassword() {
        return loginPassword;
    }

    public void setLoginPassword(String loginPassword) {
        this.loginPassword = loginPassword;
    }

    public UserLoginBD getUserLoginBD() {
        return userLoginBD;
    }

    public void setUserLoginBD(UserLoginBD userLoginBD) {
        this.userLoginBD = userLoginBD;
    }
    

    public String login() {
        LOGGER.debug("Login with with User, name->" + loginName);
        String nextPage = NavigationKey.LOGIN_SUCCESS.getKey();
        try {
            JsfUtil.establishNewSession();
            getUserLoginBD().login(loginName, loginPassword);

        } catch (BusinessException ex) {
           nextPage = NavigationKey.LOGIN_FAILURE.getKey();
        }
        return nextPage;
    }
    
    
     public String logout() {
       
        JsfUtil.cleanHttpSession();
        return NavigationKey.LOGOUT.getKey();
    }
}
