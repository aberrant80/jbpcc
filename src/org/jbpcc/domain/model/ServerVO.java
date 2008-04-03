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
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * A VO to store JBPCC Server Config Data
 */
@Entity
@Table(name = "JBPCC_SERVERS")
public class ServerVO implements Cloneable, Serializable {

    @Id
    @TableGenerator(name = "SERVERS_GEN", table = "JBPCC_IDGEN", pkColumnName = "GEN_NAME", valueColumnName = "GEN_VAL", pkColumnValue = "SERVERS_GEN")
    @GeneratedValue(generator = "SERVERS_GEN")
    private Integer id;
    private String name;
    private String jmxAgentName;
    private String ip;
    private Integer port;
    private String userName;
    private String password;
    
    @ManyToMany(mappedBy = "assignedServers",
                fetch = FetchType.EAGER)
    @JoinTable(name = "JBPCC_USERSERVERS",
    joinColumns = @JoinColumn(name = "SERVERID"),
    inverseJoinColumns = @JoinColumn(name = "USERID"))
    private List<UserVO> userVOList;

    public ServerVO() {
    }

    public ServerVO(String name, String jmxAgentName, String ip, Integer port) {
        this.name = name;
        this.jmxAgentName = jmxAgentName;
        this.ip = ip;
        this.port = port;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getJmxAgentName() {
        return jmxAgentName;
    }

    public void setJmxAgentName(String jmxAgentName) {
        this.jmxAgentName = jmxAgentName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<UserVO> getUserVOList() {
        return userVOList;
    }

    public void setUserVOList(List<UserVO> userVOList) {
        this.userVOList = userVOList;
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
