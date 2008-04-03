package org.jbpcc.admin.delegates;

import org.apache.log4j.Logger;
import org.jbpcc.admin.constants.ErrorMessageKey;
import org.jbpcc.domain.dao.DAOFinderException;
import org.jbpcc.domain.model.UserVO;
import org.jbpcc.domain.dao.UserDAO;
import org.jbpcc.domain.dao.jpa.JPAServerDAO;
import org.jbpcc.domain.model.ServerVO;

public class DefaultUserLoginBD implements UserLoginBD {

    private static Logger LOGGER = Logger.getLogger(DefaultUserLoginBD.class.getName());
    private UserDAO dao = null;

    public DefaultUserLoginBD() {
    }

    public UserVO login(String loginName, String loginPassword) throws BusinessException {
        UserVO vo = null;
        try {
            LOGGER.debug("Attemp to login user with loginName->" + loginName + ", and password->" + loginPassword);
            vo = dao.findUserByLoginID(loginName);

        } catch (DAOFinderException ex) {
            throw new BusinessException(ErrorMessageKey.USER_LOGIN_NAME_INVALID);
        }

        if (!vo.getPassword().equals(loginPassword)) {
            throw new BusinessException(ErrorMessageKey.LOGIN_AUTHENTICATION_FAILED);
        }

        // Testing only
        try {
            ServerVO sVO = new ServerVO("Agent3", "Server3", "localhost", 8574);
            JPAServerDAO sDAO = new JPAServerDAO();
            sDAO.create(sVO);
            
            // Try to add the newly created ServerVO to user assigned ServerList
            vo.getAssignedServers().add(sVO);
            dao.update(vo);
            
        } catch (Exception exp) {
            exp.printStackTrace();
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
