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
import java.io.InputStream;
import java.io.RandomAccessFile;

public class BackwardsFileInputStream extends InputStream {
    private final byte[] buffer = new byte[4096];
    private final RandomAccessFile randomAccessFile;
    private long currentPositionInFile;
    private int currentPositionInBuffer;

    public BackwardsFileInputStream(File file, long filePointer) throws IOException {
        assert (file != null) && file.exists() && file.isFile() && file.canRead();
        randomAccessFile = new RandomAccessFile(file, "r");
        randomAccessFile.seek(filePointer);
        currentPositionInFile = filePointer;
        currentPositionInBuffer = 0;
    }

    @Override
    public int read() throws IOException {
        if (currentPositionInFile <= 0) {
            return -1;
        }
        if (--currentPositionInBuffer < 0) {
            currentPositionInBuffer = buffer.length;
            long startOfBlock = currentPositionInFile - buffer.length;
            if (startOfBlock < 0) {
                currentPositionInBuffer = buffer.length + (int) startOfBlock;
                startOfBlock = 0;
            }
            randomAccessFile.seek(startOfBlock);
            randomAccessFile.readFully(buffer, 0, currentPositionInBuffer);
            return read();
        }
        currentPositionInFile--;
        return buffer[currentPositionInBuffer];
    }

    @Override
    public void close() throws IOException {
        randomAccessFile.close();
    }
}
