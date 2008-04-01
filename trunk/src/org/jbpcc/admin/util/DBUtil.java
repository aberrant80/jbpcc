package org.jbpcc.admin.util;

import com.opensymphony.oscache.util.StringUtil;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.StringTokenizer;
import org.apache.log4j.Logger;

/**
 * Utilities class of interacting with database via JDBC.
 * @author jameskhoo
 */
public class DBUtil {

    private static final Logger LOGGER = Logger.getLogger(DBUtil.class.getName());
    private static String JBPCC_DB_DRIVER = "jbpcc.db.driver";
    private static String JBPCC_DB_URL = "jbpcc.db.url";
    private static String DERBY_PATH = "derby.path";
    private static String JBPCC_DB_SQL_FILE = "jbpcc.db.sql.file";
    private static String JBPCC_DB_QUARTZ_SQL_FILE ="jbpcc.db.quartz.sql.file";
    private static String DERBY_DB_TOKEN = "%derby.path%";
    private static String CHECK_JBPCC_SCHEMA_SQL = "SELECT count(*) FROM SYS.SYSTABLES WHERE TABLENAME like 'JBPCC_%'";
    private static String CHECK_JBPCC_QUARTZ_TABLE_SQL = "SELECT count(*) FROM SYS.SYSTABLES WHERE TABLENAME like 'QRTZ%'";
    
    
    public synchronized static void initJBPCCDB(String applicationPath) {
        Connection conn = null;
        Statement stmt = null;
        ResultSet resultSet = null;
        try {
            Class.forName(ApplicationProperties.getInstance().getProperty(JBPCC_DB_DRIVER));
            LOGGER.debug("JBPCC DB Driver loaded");

            String url = ApplicationProperties.getInstance().getProperty(JBPCC_DB_URL);
            String derby_path = ApplicationProperties.getInstance().getProperty(DERBY_PATH);

            url = url.replaceFirst(DERBY_DB_TOKEN, derby_path);
            ApplicationProperties.getInstance().setProperty(JBPCC_DB_URL, url);
            LOGGER.debug("Attemp to connect to Derby Database: " + url);

            conn = DriverManager.getConnection(url);
            LOGGER.info("Connected to database: " + url);
            stmt = conn.createStatement();
            LOGGER.debug("Check if JBPCC schema exists");
            if(!isSchemasExists(conn, CHECK_JBPCC_SCHEMA_SQL)) {
                LOGGER.debug("initializing JBPCC schema");
                String sqlFileName = ApplicationProperties.getInstance().getProperty(JBPCC_DB_SQL_FILE);
                setupDBObject(conn, sqlFileName, applicationPath);
                LOGGER.debug("JBPCC schema initalized");
            } else {
                LOGGER.debug("JBPCC schema esists");
            }
            
            LOGGER.debug("Check if QUARTZ schema exists");
            if(!isSchemasExists(conn, CHECK_JBPCC_QUARTZ_TABLE_SQL)) {
                LOGGER.debug("initializing QUARTZ schema");
                String sqlFileName = ApplicationProperties.getInstance().getProperty(JBPCC_DB_QUARTZ_SQL_FILE);
                setupDBObject(conn, sqlFileName, applicationPath);
                LOGGER.debug("JBPCC Quarrz schema initalized");
            } else {
                LOGGER.debug("JBPCC QUARTZ schema exists");
            }

        } catch (Exception ex) {
            LOGGER.error(ex, ex.getCause());
        } finally {
            try {
                conn.close();
                stmt.close();
            } catch (SQLException ex) {
                LOGGER.error(ex, ex.getCause());
            }
            
        }



    }
    
    private static boolean isSchemasExists(Connection conn, String sql) throws SQLException {
        Statement stmt = conn.createStatement();
        boolean schemaExists = false;
        ResultSet resultSet = null;
        int tableFounds = 0;
        resultSet = stmt.executeQuery(sql);
        if (resultSet.next()) {
            tableFounds = resultSet.getInt(1);
        }
        stmt.close();
        return tableFounds > 0;
    }
    
    
    private static void setupDBObject(Connection conn, String sqlFileName, String applicationPath) throws IOException, SQLException {
        String line = null;
        StringBuffer buf = new StringBuffer();
        File sqlFile = new File(applicationPath + sqlFileName);
        
        if(!sqlFile.exists()) {
            throw new IOException("SQLFile :" + sqlFileName + " does not exists at " + applicationPath );
        }
        
        LineNumberReader reader = new LineNumberReader(new FileReader(sqlFile));
        while( (line = reader.readLine()) != null) {
            buf.append(line);
        }
        
        StringTokenizer tokenizer = new StringTokenizer(buf.toString(), ";");
        String sql = null;
        while(tokenizer.hasMoreElements()) {
            sql = (String) tokenizer.nextElement();
            if(sql != null && !StringUtil.isEmpty(sql)) {
                LOGGER.debug("Executing SQL->" + sql);
                executeSQL(conn, sql);
            }
            
        }
    }
    
     private static void executeSQL(Connection conn, String sql) throws SQLException {
        Statement stmt = conn.createStatement();
        stmt.execute(sql);
        stmt.close();
    }
}
