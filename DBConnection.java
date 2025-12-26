/******************************************************************************
*	Program Author: Dr. Yongming Tang for CSCI 6810 Java and the Internet	  *
*	Date: September, 2012													  *
*******************************************************************************/

package com.aditya;

import java.util.*;
import java.sql.*;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * DBConnection backed by HikariCP DataSource. Supports default and custom URLs/drivers
 * to make integration testing (H2) easy.
 */
public class DBConnection {

    private static HikariDataSource dataSource;
    private String URL;
    private String driverClass;
    private String username;
    private String password;

    private static final Logger logger = LoggerFactory.getLogger(DBConnection.class);

    // Default constructor (reads environment variables, falls back to legacy URL)
    public DBConnection() {
        String envUrl = System.getenv("JDBC_URL");
        String envUser = System.getenv("JDBC_USER");
        String envPass = System.getenv("JDBC_PASSWORD");
        String envDriver = System.getenv("JDBC_DRIVER");
        if (envUrl != null && !envUrl.isEmpty()) {
            this.URL = envUrl;
            this.username = envUser;
            this.password = envPass;
            this.driverClass = envDriver;
        } else {
            this.URL = "jdbc:sqlserver://127.0.0.1:1433;databaseName=JavaClass;integratedSecurity=true;";
            this.driverClass = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
        }
        initDataSourceIfNeeded();
    }

    // Constructor that accepts custom JDBC URL (useful for tests)
    public DBConnection(String url) {
        this(url, null);
    }

    // Constructor that accepts JDBC URL and driver class
    public DBConnection(String url, String driverClass) {
        this.URL = url;
        this.driverClass = driverClass;
        initDataSourceIfNeeded();
    }

    private synchronized void initDataSourceIfNeeded() {
        if (dataSource != null) return;
        try {
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(URL);
            if (driverClass != null && !driverClass.isEmpty()) {
                config.setDriverClassName(driverClass);
            }
            if (username != null && !username.isEmpty()) {
                config.setUsername(username);
            }
            if (password != null && !password.isEmpty()) {
                config.setPassword(password);
            }
            config.setMaximumPoolSize(10);
            config.setPoolName("OnlineBankingPool");
            dataSource = new HikariDataSource(config);
            logger.info("Initialized HikariCP DataSource for URL {}", URL);
        } catch (Exception e) {
            logger.error("Failed to initialize DataSource: {}", e.getMessage(), e);
        }
    }

    public Connection openConn() {
        try {
            if (dataSource == null) initDataSourceIfNeeded();
            return dataSource.getConnection();
        } catch (SQLException e) {
            logger.error("Failed to open connection: {}", e.getMessage(), e);
            return null;
        }
    }

    public void closeConn() {
        // No-op: callers should close the Connection returned by openConn();
        // If you want to shutdown the pool, call shutdownPool().
    }

    public static synchronized void shutdownPool() {
        if (dataSource != null) {
            dataSource.close();
            dataSource = null;
            logger.info("HikariCP pool shut down");
        }
    }

        public java.util.List<Object> getNextRow(ResultSet rs,ResultSetMetaData rsmd) throws SQLException {
           java.util.List<Object> currentRow = new java.util.ArrayList<>();

           for(int i=1;i<=rsmd.getColumnCount();i++) {
              switch(rsmd.getColumnType(i)) {
                  case Types.VARCHAR:
                  case Types.LONGVARCHAR:
                      currentRow.add(rs.getString(i));
                      break;
                  case Types.INTEGER:
                      currentRow.add(rs.getLong(i));
                      break;
                  case Types.DOUBLE:
                      currentRow.add(rs.getDouble(i));
                      break;
                  case Types.FLOAT:
                      currentRow.add(rs.getFloat(i));
                      break;
                  default:
                      currentRow.add(rs.getObject(i));
                      break;
             }
           }

           return currentRow;
        }
}