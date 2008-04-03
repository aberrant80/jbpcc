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
package org.jbpcc.domain.model;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * A VO to store JBPCC login user data
 * @version $Revision:  $
 */
@Entity
@Table(name = "JBPCC_USERS")
@NamedQueries({
    @NamedQuery(name = UserVO.QUERY_FIND_USER_BY_LOGIN_ID,
    query = "SELECT u FROM UserVO u WHERE u.loginName =:loginID")
})
public class UserVO implements Cloneable, Serializable {

    public static final String QUERY_FIND_USER_BY_LOGIN_ID = "UserVO.findByLoginID";
    
    @Id
    @TableGenerator(name = "USERS_GEN", table = "JBPCC_IDGEN", pkColumnName = "GEN_NAME", valueColumnName = "GEN_VAL", pkColumnValue = "USERS_GEN")
    @GeneratedValue(generator = "USERS_GEN")
    private Integer id;
    private String loginName;
    private String password;
    private String surName;
    private String foreName;
    private Boolean enabled;
    
    @ManyToMany
    @JoinTable(name = "JBPCC_USERSERVERS",
    joinColumns = @JoinColumn(name = "USERID"),
    inverseJoinColumns = @JoinColumn(name = "SERVERID"))
    private List<ServerVO> assignedServers;

    public UserVO() {
    }

    public UserVO(String loginName, String password) {
        this.loginName = loginName;
        this.password = password;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getForeName() {
        return foreName;
    }

    public void setForeName(String foreName) {
        this.foreName = foreName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSurName() {
        return surName;
    }

    public void setSurName(String surName) {
        this.surName = surName;
    }

    public List<ServerVO> getAssignedServers() {
        return assignedServers;
    }

    public void setAssignedServers(List<ServerVO> assignedServers) {
        this.assignedServers = assignedServers;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }
}
