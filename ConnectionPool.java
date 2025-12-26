/******************************************************************************
*	Online Banking System - Thread-Safe Connection Pool for Multithreading
*	Date: December, 2025
*******************************************************************************/

package com.aditya;

import java.sql.*;
import java.util.*;

/**
 * Thread-safe connection pool for managing database connections.
 * Demonstrates synchronization and proper resource management.
 */
public class ConnectionPool {
    private static final int POOL_SIZE = 5;
    private static ConnectionPool instance;
    private Queue<Connection> availableConnections;
    private Set<Connection> usedConnections;
    private String url;
    private Object lock = new Object();

    private ConnectionPool(String url) {
        this.url = url;
        this.availableConnections = new LinkedList<>();
        this.usedConnections = Collections.synchronizedSet(new HashSet<>());
        initializePool();
    }

    /**
     * Get singleton instance of connection pool.
     * @param url database URL
     * @return connection pool instance
     */
    public static synchronized ConnectionPool getInstance(String url) {
        if (instance == null) {
            instance = new ConnectionPool(url);
        }
        return instance;
    }

    /**
     * Initialize the connection pool with default connections.
     */
    private void initializePool() {
        for (int i = 0; i < POOL_SIZE; i++) {
            try {
                Connection conn = DriverManager.getConnection(url);
                availableConnections.add(conn);
            } catch (SQLException e) {
                System.err.println("Failed to create connection: " + e.getMessage());
            }
        }
    }

    /**
     * Get a connection from the pool (thread-safe).
     * @return a database connection
     * @throws SQLException if no connection available
     */
    public synchronized Connection getConnection() throws SQLException {
        Connection conn = null;
        
        if (availableConnections.isEmpty()) {
            try {
                wait(); // Wait if no connections available
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new SQLException("Interrupted waiting for connection", e);
            }
        }

        conn = availableConnections.poll();
        if (conn != null) {
            usedConnections.add(conn);
        }
        return conn;
    }

    /**
     * Return a connection to the pool (thread-safe).
     * @param conn the connection to return
     */
    public synchronized void returnConnection(Connection conn) {
        if (conn != null) {
            usedConnections.remove(conn);
            availableConnections.add(conn);
            notify(); // Notify waiting threads
        }
    }

    /**
     * Close all connections in the pool.
     */
    public synchronized void closeAllConnections() {
        for (Connection conn : availableConnections) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
        availableConnections.clear();

        for (Connection conn : usedConnections) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
        usedConnections.clear();
    }

    /**
     * Get current pool statistics.
     * @return string with pool info
     */
    public synchronized String getPoolStats() {
        return String.format("Available: %d, Used: %d, Total: %d", 
            availableConnections.size(), usedConnections.size(), POOL_SIZE);
    }
}
