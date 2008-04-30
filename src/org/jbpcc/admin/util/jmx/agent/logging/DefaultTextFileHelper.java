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


import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class DefaultTextFileHelper implements TextFileHelper {
    private static int LINE_SEPERARTOR_LENGTH = System.getProperty("line.separator").length();

    /**
     * {@inheritDoc}
     */    public LogData headLog(File logFile, int linesToRead) throws IOException {
        return headLog(logFile, linesToRead, 0l);
    }

    /**
     * {@inheritDoc}
     */     
    public LogData headLog(File logFile, int linesToRead, long fromFilePointer) throws IOException {
        assert (logFile != null) && logFile.exists() && logFile.isFile() && logFile.canRead();
        assert fromFilePointer >= 0l;
        assert linesToRead > 0;
        LogData logData = new LogData();
        RandomAccessFile raf = null;

        List<String> logStatments = new ArrayList<String>();
        raf = new RandomAccessFile(logFile, "r");
        raf.seek(fromFilePointer);
        logData.setStartFilePointer(raf.getFilePointer());
        for (String line = null; (linesToRead-- > 0) && (line = raf.readLine()) != null;) {
            logStatments.add(line);
        }
        logData.setEndFilePointer(raf.getFilePointer());
        logData.setLogStatements(logStatments);
        raf.close();

        return logData;
    }

    /**
     * {@inheritDoc}
     */
    public LogData tailLog(File logFile, int linesToRead) throws IOException {
        assert (logFile != null) && logFile.exists() && logFile.isFile() && logFile.canRead();
        RandomAccessFile raf = new RandomAccessFile(logFile, "r");
        long filePointer = raf.length();

        return tailLog(logFile, linesToRead, filePointer);
    }

    /**
     * {@inheritDoc}
     */    
    public LogData tailLog(File logFile, int linesToRead, long fromFilePointer) throws IOException {
        assert (logFile != null) && logFile.exists() && logFile.isFile() && logFile.canRead();
        assert fromFilePointer >= 0l;
        assert linesToRead > 0;
        LogData logData = new LogData();

        BackwardsFileInputStream bfis = new BackwardsFileInputStream(logFile, fromFilePointer);
        LinkedList<String> logStatements = new LinkedList<String>();
        long totalBytesReads = 0l;
        BufferedReader reader = new BufferedReader(new InputStreamReader(bfis, "ISO-8859-1"));
        for (String line = null; (linesToRead-- > 0) && (line = reader.readLine()) != null;) {
            // Reverse the order of the characters in the string
            char[] chars = line.toCharArray();
            for (int j = 0, k = chars.length - 1; j < k; j++, k--) {
                char temp = chars[j];
                chars[j] = chars[k];
                chars[k] = temp;
            }
            logStatements.addFirst(new String(chars));
            totalBytesReads = totalBytesReads + chars.length + LINE_SEPERARTOR_LENGTH;
        }

        logData.setEndFilePointer(fromFilePointer);
        logData.setLogStatements(logStatements);
        if (fromFilePointer - totalBytesReads > 0) {
            logData.setStartFilePointer(fromFilePointer - totalBytesReads);
        } else {
            logData.setStartFilePointer(0l);
        }

        bfis.close();
        reader.close();
        return logData;
    }
}
