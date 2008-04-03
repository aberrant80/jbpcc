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

import java.io.Serializable;
import javax.persistence.EntityManager;
import org.jbpcc.domain.dao.BaseDAO;
import org.jbpcc.domain.dao.DAODeleteException;
import org.jbpcc.domain.dao.DAOFinderException;
import org.jbpcc.domain.dao.DAOInsertException;
import org.jbpcc.domain.dao.DAOUpdateException;


public  class BaseJPADAO <T, PK extends Serializable> 
    implements BaseDAO<T, PK> {
    private Class<T> type;
    
    public EntityManager getEntityManager(){
        return EntityManagerAccessor.getInstance().getEntityManager();
    }

    public T create(T newInstance) throws DAOInsertException {
        EntityManager em = getEntityManager();
        try {        
            em.getTransaction().begin();
            if( newInstance != null) {
                em.persist(newInstance);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            throw new DAOInsertException(ex.getMessage(), ex.getCause());
        } finally {
            em.close();
        }
        return newInstance;
        
    }
    

    public T find(PK id) throws DAOFinderException {
         EntityManager em = getEntityManager();
         T vo = null;
         try {
             vo = em.find(type,id);
         } catch (Exception ex) {
            throw new DAOFinderException(ex.getMessage(), ex.getCause());
        } finally {
            em.close();
        }
        return vo;
    }

    public void update(T transientObject) throws DAOUpdateException {
        EntityManager em = getEntityManager();
        try {        
            em.getTransaction().begin();
            if( transientObject != null) {
                em.merge(transientObject);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            throw new DAOUpdateException(ex.getMessage(), ex.getCause());
        } finally {
            em.close();
        }
    }

    public void delete(T persistentObject) throws DAODeleteException {
        EntityManager em = getEntityManager();
        try {        
            em.getTransaction().begin();
            if( persistentObject != null) {
                em.remove(persistentObject);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            throw new DAODeleteException(ex.getMessage(), ex.getCause());
        } finally {
            em.close();
        }
    }

  

}
