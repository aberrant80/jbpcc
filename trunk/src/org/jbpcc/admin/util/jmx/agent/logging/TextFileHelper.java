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


import java.io.File;
import java.io.IOException;

public interface TextFileHelper {
    /**
     * Similar to Unix head command, read a text file forward from the beggining of the file till it reaches line limit,
     * or EOF
     * 
     * @param logFile
     *            the logFile
     * @param lines,
     *            Number of line to be read.
     * @return
     * @throws java.io.IOException
     */
    LogData headLog(File logFile, int lines) throws IOException;

    /**
     * Read a text file forward from given file pointer, till it reaches line limit, or reach EOF
     * 
     * @param logFile
     *            the logFile
     * @param lines,
     *            Number of line to be read.
     * @param fromFilePointer
     * @return
     * @throws java.io.IOException
     */
    LogData headLog(File logFile, int lines, long fromFilePointer) throws IOException;

    /**
     * Similar to Unix tail command. Read a text file backward from end of file, till it reaches line limit, or
     * beginning of file, reverse the lines reads prior return to caller.
     * 
     * @param logFile
     *            the logFile
     * @param lines,
     *            Number of line to be read.
     * @param fromFilePointer
     * @return
     * @throws java.io.IOException
     */
    LogData tailLog(File logFile, int lines) throws IOException;

    /**
     * Read a text file backward from the given file pointer, till it reaches line limit, or beginning of file, reverse
     * the lines reads prior return to caller.
     * 
     * @param logFile
     *            the logFile
     * @param lines,
     *            Number of line to be read.
     * @param fromFilePointer
     * @return
     * @throws java.io.IOException
     */
    LogData tailLog(File logFile, int line, long fromFilePointer) throws IOException;
}
