package org.jbpcc.admin.delegates;

import org.jbpcc.admin.constants.ErrorMessageKey;
import org.jbpcc.domain.model.UserVO;


public class DefaultUserLoginBD implements UserLoginBD {

    public UserVO login(String loginName, String loginPassword) throws BusinessException {
        UserVO vo =null;
        if(loginName.equalsIgnoreCase(loginPassword)) {
            vo = new UserVO();
            vo.setLoginName(loginName);
            vo.setPassword(loginPassword);
         } else {
            throw new BusinessException(ErrorMessageKey.LOGIN_AUTHENTICATION_FAILED);
         }
        
        return vo;
    }

}
