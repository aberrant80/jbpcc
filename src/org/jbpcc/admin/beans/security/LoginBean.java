package org.jbpcc.admin.beans.security;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.jbpcc.admin.constants.NavigationKey;
import org.jbpcc.admin.delegates.BusinessException;
import org.jbpcc.admin.delegates.UserLoginBD;
import org.jbpcc.admin.jsf.JsfUtil;

public class LoginBean {

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
        String nextPage = NavigationKey.LOGIN_SUCCESS.getKey();
        try {
            JsfUtil.establishNewSession();
            getUserLoginBD().login(loginName, loginPassword);

        } catch (BusinessException ex) {
            Logger.getLogger(LoginBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return nextPage;
    }
}
