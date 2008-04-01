

package org.jbpcc.domain.dao.jpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;


public class EntityManagerAccessor {
    private static EntityManagerAccessor instance = null;
    private final static String ENTITY_MANAGER_FACTORY_NAME = "jbpcc";
    private static EntityManagerFactory emf =  null;
    private static EntityManager em = null;
    
    private EntityManagerAccessor() {
        emf =  Persistence.createEntityManagerFactory(ENTITY_MANAGER_FACTORY_NAME);
        em = emf.createEntityManager();
    }


    public static synchronized EntityManagerAccessor getInstance() {
        if(instance == null) {
            instance = new EntityManagerAccessor();
        }
        return instance;
    }
    
    public static synchronized EntityManagerFactory getEntityManagerFactory() {
        return emf;
    }
    
    public static synchronized EntityManager getEntityManager() {
        return em;
    }

}
