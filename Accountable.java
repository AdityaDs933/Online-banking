package com.aditya;

public interface Accountable {
    boolean openAcct();
    String getAccno(String customerId);
    float getBalance();
    float getBalance(String acctNumber);
    boolean deposit(String customerId);
    boolean Withdraw(String customerId);
}
