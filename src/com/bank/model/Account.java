package com.bank.model;

import java.time.LocalDateTime;

public class Account {
    private String accountNumber;
    private String accountHolderName;
    private double balance;
    private String accountType;
    private LocalDateTime createdDate;
    private boolean active;

    public Account(String accountNumber, String accountHolderName, 
                  double initialBalance, String accountType) {
        this.accountNumber = accountNumber;
        this.accountHolderName = accountHolderName;
        this.balance = initialBalance;
        this.accountType = accountType;
        this.createdDate = LocalDateTime.now();
        this.active = true;
    }

    public String getAccountNumber() { return accountNumber; }
    public String getAccountHolderName() { return accountHolderName; }
    public double getBalance() { return balance; }
    public String getAccountType() { return accountType; }
    public LocalDateTime getCreatedDate() { return createdDate; }
    public boolean isActive() { return active; }
    
    public void setBalance(double balance) { this.balance = balance; }
    public void setActive(boolean active) { this.active = active; }
    
    @Override
    public String toString() {
        return String.format("Account[Number: %s, Holder: %s, Balance: $%.2f, Type: %s, Active: %s]",
                accountNumber, accountHolderName, balance, accountType, active);
    }
}