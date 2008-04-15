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

package org.jbpcc.util.jpa;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An annotiation to provide automatic boolean mapping of 
 * legacy database, with table fields with combination of possible
 * boolean value, such as "Yes/No", "Y/N", "T/F", "True/False"
 * @author jameskhoo
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface BooleanMagic {
    public static final String TRUE_VALUE_KEY = "trueValue";
    public static final String COLUMN_NAME_KEY = "columnName";
    public static final String FALSE_VALUE_KEY = "falseValue"; 
    public static final String IF_NULL_KEY = "ifNull";
   
    String falseValue();
    String trueValue();
    String columnName();
    boolean ifNull() default false;
}
