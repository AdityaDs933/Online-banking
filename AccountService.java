/******************************************************************************
*	Online Banking System - Account Service Interface for OOP
*	Date: December, 2025
*******************************************************************************/

package com.aditya;

/**
 * Interface defining account operations contract.
 * Demonstrates polymorphism and separation of concerns.
 */
public interface AccountService {
    /**
     * Sign up a new account.
     * @return true if signup successful, false otherwise
     */
    boolean signUp();

    /**
     * Sign in with credentials.
     * @return account holder name if login successful, empty string otherwise
     */
    String signIn();

    /**
     * Change account password.
     * @param newPassword the new password to set
     * @return true if password changed successfully
     */
    boolean changePassword(String newPassword);

    /**
     * Get the account holder's name.
     * @return the name
     */
    String getName();
}
