package org.jbpcc.admin.delegates;

import org.jbpcc.domain.model.UserVO;

public interface UserLoginBD {
    UserVO login(String loginName, String loginPassword) throws BusinessException;
}
