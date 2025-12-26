/******************************************************************************
*	Online Banking System - Transaction Analyzer (Innovation Feature)
*	Date: December, 2025
*******************************************************************************/

package com.aditya;

import java.sql.*;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Analyzes transaction patterns and provides insights.
 * Innovation feature: generates transaction summaries and statistics.
 */
public class TransactionAnalyzer {
    private String customerId;
    private DBConnection dbConnection;

    public TransactionAnalyzer(String customerId) {
        this.customerId = customerId;
        this.dbConnection = new DBConnection();
    }

    private static final Logger logger = LoggerFactory.getLogger(TransactionAnalyzer.class);

    /**
     * Get total transactions count for customer.
     * @return count of transactions
     */
    public int getTotalTransactionCount() {
        int count = 0;
        try {
            Connection conn = dbConnection.openConn();
            String sql = "SELECT COUNT(*) FROM Transactions WHERE CustomerID = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, customerId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
            pstmt.close();
            dbConnection.closeConn();
        } catch (SQLException e) {
            logger.error("Error counting transactions: {}", e.getMessage(), e);
        }
        return count;
    }

    /**
     * Get total transaction amount for customer.
     * @return sum of all transaction amounts
     */
    public float getTotalTransactionAmount() {
        float total = 0;
        try {
            Connection conn = dbConnection.openConn();
            String sql = "SELECT SUM(TransactionAmount) FROM Transactions WHERE CustomerID = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, customerId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                total = rs.getFloat(1);
            }
            pstmt.close();
            dbConnection.closeConn();
        } catch (SQLException e) {
            logger.error("Error calculating total: {}", e.getMessage(), e);
        }
        return total;
    }

    /**
     * Get average transaction amount.
     * @return average amount per transaction
     */
    public float getAverageTransactionAmount() {
        int count = getTotalTransactionCount();
        if (count == 0) return 0;
        return getTotalTransactionAmount() / count;
    }

    /**
     * Get transaction breakdown by type.
     * @return map of transaction type to count
     */
    public Map<String, Integer> getTransactionsByType() {
        Map<String, Integer> breakdown = new HashMap<>();
        try {
            Connection conn = dbConnection.openConn();
            String sql = "SELECT TransactionType, COUNT(*) as cnt FROM Transactions WHERE CustomerID = ? GROUP BY TransactionType";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, customerId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                breakdown.put(rs.getString("TransactionType"), rs.getInt("cnt"));
            }
            pstmt.close();
            dbConnection.closeConn();
        } catch (SQLException e) {
            logger.error("Error analyzing transactions: {}", e.getMessage(), e);
        }
        return breakdown;
    }

    /**
     * Generate a summary report of transactions.
     * @return formatted summary string
     */
    public String generateSummaryReport() {
        StringBuilder report = new StringBuilder();
        report.append("===== TRANSACTION SUMMARY REPORT =====\n");
        report.append("Customer ID: ").append(customerId).append("\n");
        report.append("Total Transactions: ").append(getTotalTransactionCount()).append("\n");
        report.append("Total Amount: $").append(String.format("%.2f", getTotalTransactionAmount())).append("\n");
        report.append("Average Amount: $").append(String.format("%.2f", getAverageTransactionAmount())).append("\n");
        
        Map<String, Integer> breakdown = getTransactionsByType();
        report.append("\nTransaction Breakdown:\n");
        for (Map.Entry<String, Integer> entry : breakdown.entrySet()) {
            report.append("  ").append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }
        report.append("=====================================\n");
        
        return report.toString();
    }
}
