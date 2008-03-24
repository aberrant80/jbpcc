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
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * An abstract value object with an id that can be marked or flagged as being selected.
 */
public abstract class ValueObject  implements Serializable, Cloneable {
        private Integer id = null;
    private boolean selected = false;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Checks if this value object is currently marked as selected.
     * 
     * @return <code>true</code> if selected; <code>false</code> otherwise
     */
    public boolean isSelected() {
        return selected;
    }

    /**
     * Sets or marks this value object's selected flag.
     * 
     * @param selected
     *            set <code>true</code> to mark selected; <code>false</code> to mark as not selected
     */
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public ValueObject clone() throws CloneNotSupportedException {
        return (ValueObject) super.clone();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ValueObject)) {
            return false;
        }
        ValueObject other = (ValueObject) obj;
        return new EqualsBuilder().append(id, other.id).append(selected, other.selected).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(id).append(selected).toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append(id).append(selected).toString();
    }

}
