package org.jbpcc.admin.delegates;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.jbpcc.admin.constants.ErrorMessageKey;
import org.jbpcc.domain.dao.DAOFinderException;
import org.jbpcc.domain.model.UserVO;
import org.jbpcc.domain.dao.UserDAO;
import org.jbpcc.domain.dao.jpa.JPAUserDAO;

public class DefaultUserLoginBD implements UserLoginBD {

    private UserDAO dao = null;
   

    public DefaultUserLoginBD() {
    }

    public UserVO login(String loginName, String loginPassword) throws BusinessException {
        UserVO vo = null;
        try {
            vo = dao.findUserByLoginID(loginName);
        } catch (DAOFinderException ex) {
            throw new BusinessException(ErrorMessageKey.USER_LOGIN_NAME_INVALID);
        }

        if (!vo.getPassword().equals(loginPassword)) {
            throw new BusinessException(ErrorMessageKey.LOGIN_AUTHENTICATION_FAILED);
        }

        return vo;
    }

    public UserDAO getDao() {
        return dao;
    }

    public void setDao(UserDAO dao) {
        this.dao = dao;
    }
    
    
}
