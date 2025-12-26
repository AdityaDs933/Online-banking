/******************************************************************************
*	Online Banking System - Transaction Service Interface for OOP
*	Date: December, 2025
*******************************************************************************/

package com.aditya;

/**
 * Interface defining transaction operations contract.
 * Demonstrates polymorphism and extensibility.
 */
public interface TransactionService {
    /**
     * Record a transaction.
     * @return transaction number if successful
     */
    String recordTransaction();

    /**
     * Search transactions within a date range.
     * @param uname username to search transactions for
     * @return list of transactions
     */
    java.util.Vector<java.util.Vector<String>> searchTransaction(String uname);
}
