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
package org.jbpcc.admin.util.jmx.agent.logging;


import java.io.Serializable;
import java.util.List;

/**
 * A data class holding starting and ending file pointer with requested Log statements
 */
public class LogData implements Serializable {
    private long startFilePointer;
    private long endFilePointer;
    private List<String> logStatements;

    public LogData() {
    }

    public LogData(long startFilePointer, long endFilePointer, List<String> logStatements) {
        this.startFilePointer = startFilePointer;
        this.endFilePointer = endFilePointer;
        this.logStatements = logStatements;
    }

    public long getStartFilePointer() {
        return startFilePointer;
    }

    public void setStartFilePointer(long startFilePointer) {
        this.startFilePointer = startFilePointer;
    }

    public long getEndFilePointer() {
        return endFilePointer;
    }

    public void setEndFilePointer(long endFilePointer) {
        this.endFilePointer = endFilePointer;
    }

    public List<String> getLogStatements() {
        return logStatements;
    }

    public void setLogStatements(List<String> logStatements) {
        this.logStatements = logStatements;
    }
}
