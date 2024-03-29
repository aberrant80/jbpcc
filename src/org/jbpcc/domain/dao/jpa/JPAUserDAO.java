/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package org.jbpcc.domain.dao.jpa;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import org.jbpcc.domain.dao.DAOFinderException;
import org.jbpcc.domain.dao.UserDAO;
import org.jbpcc.domain.model.UserVO;


public class JPAUserDAO extends BaseJPADAO <UserVO, Integer> implements UserDAO {
    
    public UserVO findUserByLoginID(String loginID)  throws DAOFinderException {
        
        UserVO vo = null;
        EntityManager em = getEntityManager();
        try {
        vo = (UserVO) getEntityManager().createNamedQuery(UserVO.QUERY_FIND_USER_BY_LOGIN_ID)
                .setParameter("loginID", loginID)
                .getSingleResult();
        } catch (Exception e) {
            throw new DAOFinderException("Problem of findUserByLoginID:" + loginID, e.getCause());
        } finally {
            em.close();
        }
        return vo;
    }

    public List<UserVO> findAllUsers() {
        ArrayList<UserVO> userVOList = null;
        userVOList = (ArrayList<UserVO>) getEntityManager().createNamedQuery("UserVO.findAll")
                                         .getResultList();
        
        return userVOList;
    }

}
