package com.bank.model;

import java.time.LocalDateTime;

public class Transaction {
    private String transactionId;
    private String accountNumber;
    private TransactionType type;
    private double amount;
    private LocalDateTime timestamp;
    private String description;

    public enum TransactionType {
        DEPOSIT, WITHDRAWAL, TRANSFER
    }

    public Transaction(String transactionId, String accountNumber, 
                      TransactionType type, double amount, String description) {
        this.transactionId = transactionId;
        this.accountNumber = accountNumber;
        this.type = type;
        this.amount = amount;
        this.timestamp = LocalDateTime.now();
        this.description = description;
    }

    public String getTransactionId() { return transactionId; }
    public String getAccountNumber() { return accountNumber; }
    public TransactionType getType() { return type; }
    public double getAmount() { return amount; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public String getDescription() { return description; }

    @Override
    public String toString() {
        return String.format("Transaction[ID: %s, Account: %s, Type: %s, Amount: $%.2f, Time: %s]",
                transactionId, accountNumber, type, amount, timestamp);
    }
}