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

import java.util.*;
import java.io.*;
import com.sun.mirror.apt.*;
import com.sun.mirror.declaration.*;
import com.sun.mirror.util.*;


public class BooleanMagicProcessorFactory implements AnnotationProcessorFactory {
    
    public static final String JAVAX_PERSISTANCE_ENTITY_ANNOTATION = "javax.persistence.Entity";
    public static final String JPA_BOOLEAN_MAP_ANNOTATION = "org.jbpcc.util.jpa.BooleanMagic";
    public static final String JAVAX_PERSISTANCE_TRANSIENT_ANNOTATION = "javax.persistence.Transient";

    public Collection<String> supportedOptions() {
         return Collections.emptyList();
        
    }

    public Collection<String> supportedAnnotationTypes() {
       /* List<String> supportedList = new ArrayList<String> ();
        supportedList.add(JAVAX_PERSISTANCE_TRANSIENT_ANNOTATION);
        supportedList.add(JPA_BOOLEAN_MAP_ANNOTATION);
        supportedList.add(JAVAX_PERSISTANCE_ENTITY_ANNOTATION);
        
        return supportedList;*/
        
        return Collections.singletonList(JPA_BOOLEAN_MAP_ANNOTATION);
       
    }

    public AnnotationProcessor getProcessorFor(Set<AnnotationTypeDeclaration> atd, AnnotationProcessorEnvironment env) {
        if(!atd.isEmpty()) {
            return new BooleanMagicProcessor(env);
        } else {
            return AnnotationProcessors.NO_OP;
        }
    }

}
