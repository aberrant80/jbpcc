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

package org.jbpcc.domain.dao;

import java.io.Serializable;


public interface BaseDAO <T, PK extends Serializable> {

    /** Persist the newInstance object into database */
    T create(T newInstance) throws DAOInsertException;
    
    /**
     * Retrieve an object that was previously persisted to the database using
     * the indicated id as primary key
     *
     * @param id
     * @return
     * @throws org.jbpcc.domain.dao.DAOFinderException
     */
    T find(PK id) throws DAOFinderException;

    /** Save changes made to a persistent object.  */
    void update(T transientObject) throws DAOUpdateException;

    /** Remove an object from persistent storage in the database */
    void delete(T persistentObject) throws DAODeleteException;
}
