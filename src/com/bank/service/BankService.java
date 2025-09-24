package com.bank.service;

import com.bank.model.Account;
import com.bank.model.Transaction;
import com.bank.exception.*;

import java.util.*;

public class BankService {
    private Map<String, Account> accounts;
    private List<Transaction> transactions;
    private int transactionCounter;

    public BankService() {
        this.accounts = new HashMap<>();
        this.transactions = new ArrayList<>();
        this.transactionCounter = 1;
    }

    public String createAccount(String accountHolderName, double initialDeposit, String accountType) 
            throws InvalidAmountException {
        if (initialDeposit < 0) {
            throw new InvalidAmountException("Initial deposit cannot be negative");
        }

        String accountNumber = generateAccountNumber();
        Account account = new Account(accountNumber, accountHolderName, initialDeposit, accountType);
        accounts.put(accountNumber, account);

        if (initialDeposit > 0) {
            recordTransaction(accountNumber, Transaction.TransactionType.DEPOSIT, 
                            initialDeposit, "Initial deposit");
        }

        return accountNumber;
    }

    public Account getAccount(String accountNumber) throws AccountNotFoundException {
        Account account = accounts.get(accountNumber);
        if (account == null || !account.isActive()) {
            throw new AccountNotFoundException("Account not found: " + accountNumber);
        }
        return account;
    }

    public void deposit(String accountNumber, double amount) 
            throws AccountNotFoundException, InvalidAmountException {
        if (amount <= 0) {
            throw new InvalidAmountException("Deposit amount must be positive");
        }

        Account account = getAccount(accountNumber);
        account.setBalance(account.getBalance() + amount);
        recordTransaction(accountNumber, Transaction.TransactionType.DEPOSIT, amount, "Deposit");
    }

    public void withdraw(String accountNumber, double amount) 
            throws AccountNotFoundException, InvalidAmountException, InsufficientFundsException {
        if (amount <= 0) {
            throw new InvalidAmountException("Withdrawal amount must be positive");
        }

        Account account = getAccount(accountNumber);
        if (account.getBalance() < amount) {
            throw new InsufficientFundsException(
                "Insufficient funds. Current balance: $" + account.getBalance());
        }

        account.setBalance(account.getBalance() - amount);
        recordTransaction(accountNumber, Transaction.TransactionType.WITHDRAWAL, amount, "Withdrawal");
    }

    public void transfer(String fromAccount, String toAccount, double amount)
            throws AccountNotFoundException, InvalidAmountException, InsufficientFundsException {
        if (fromAccount.equals(toAccount)) {
            throw new InvalidAmountException("Cannot transfer to the same account");
        }

        withdraw(fromAccount, amount);
        deposit(toAccount, amount);

        recordTransaction(fromAccount, Transaction.TransactionType.TRANSFER, 
                        amount, "Transfer to " + toAccount);
        recordTransaction(toAccount, Transaction.TransactionType.TRANSFER, 
                        amount, "Transfer from " + fromAccount);
    }

    private String generateAccountNumber() {
        return "ACC" + String.format("%06d", accounts.size() + 1);
    }

    private void recordTransaction(String accountNumber, Transaction.TransactionType type, 
                                 double amount, String description) {
        String transactionId = "TXN" + String.format("%06d", transactionCounter++);
        Transaction transaction = new Transaction(transactionId, accountNumber, type, amount, description);
        transactions.add(transaction);
    }

    public List<Transaction> getAccountTransactions(String accountNumber) throws AccountNotFoundException {
        getAccount(accountNumber);
        List<Transaction> accountTransactions = new ArrayList<>();
        for (Transaction t : transactions) {
            if (t.getAccountNumber().equals(accountNumber)) {
                accountTransactions.add(t);
            }
        }
        return accountTransactions;
    }

    public double getTotalBankBalance() {
        return accounts.values().stream()
                .filter(Account::isActive)
                .mapToDouble(Account::getBalance)
                .sum();
    }

    public int getTotalAccounts() {
        return (int) accounts.values().stream()
                .filter(Account::isActive)
                .count();
    }
}