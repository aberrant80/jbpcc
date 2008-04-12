--
-- Licensed to the Apache Software Foundation (ASF) under one or more
-- contributor license agreements.  See the NOTICE file distributed with
-- this work for additional information regarding copyright ownership.
-- The ASF licenses this file to You under the Apache License, Version 2.0
-- (the "License"); you may not use this file except in compliance with
-- the License.  You may obtain a copy of the License at
--
--      http://www.apache.org/licenses/LICENSE-2.0
--
-- Unless required by applicable law or agreed to in writing, software
-- distributed under the License is distributed on an "AS IS" BASIS,
-- WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
-- See the License for the specific language governing permissions and
-- limitations under the License.
-- 
-- JBPCC Schema scripts by James Khoo
--
  

CREATE TABLE "JBPCC_USERS"
(
   ID INTEGER PRIMARY KEY not null,
   LOGINNAME VARCHAR(30) not null unique,
   FORENAME VARCHAR(30),
   SURNAME VARCHAR(30),
   ENABLED VARCHAR(10) not null,
   PASSWORD VARCHAR(1000)
);

CREATE TABLE "JBPCC_BATCH_MANAGERS"
(
   ID INTEGER PRIMARY KEY not null,
   NAME VARCHAR(150) not null unique,
   JMXAGENTNAME VARCHAR(100) not null,
   IP VARCHAR(40) not null,
   PORT INTEGER not null,
   USERNAME VARCHAR(150),
   PASSWORD VARCHAR(100)
);

CREATE TABLE "JBPCC_USER_BATCH_MANAGER"
(
   USERID INTEGER not null,
   MANAGERID INTEGER not null,
   PRIMARY KEY (USERID, MANAGERID),
   FOREIGN KEY (USERID) references JBPCC_USERS(ID),
   FOREIGN KEY (MANAGERID) references JBPCC_BATCH_MANAGERS(ID)
);


CREATE TABLE "JBPCC_IDGEN" (
    gen_name VARCHAR(80),
    gen_val INTEGER,
    CONSTRAINT pk_id_gen
    PRIMARY KEY (gen_name)
);

INSERT INTO JBPCC_IDGEN (gen_name, gen_val) VALUES ('USERS_GEN', 10);
INSERT INTO JBPCC_IDGEN (gen_name, gen_val) VALUES ('BATCH_MANAGER_GEN', 10);

--
-- Temporary  insert some dummy servers, and links to user Admin for
-- Testing purpose
-- 

INSERT INTO JBPCC_USERS(ID, LOGINNAME, FORENAME, SURNAME, ENABLED, PASSWORD) 
VALUES (1, 'admin', 'JBPCC', 'Administrator', 'Yes', 'admin');

