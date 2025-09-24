package com.bankk;
import com.bank.service.BankService;
import com.bank.model.Account;
import com.bank.model.Transaction;
import com.bank.exception.*;
import com.bank.util.InputValidator;
import java.util.List;
import java.util.Scanner;
public class BankApp {
    private BankService bankService;
    private Scanner scanner;
    private boolean running;
    public BankApp() {
        this.bankService = new BankService();
        this.scanner = new Scanner(System.in);
        this.running = true;
    }
    public void start() {
        System.out.println("🚀 Welcome to Professional Bank Management System 🚀");       
        while (running) {
            displayMainMenu();
            int choice = getIntInput("Choose an option: ");            
            try {
                switch (choice) {
                    case 1 -> createAccount();
                    case 2 -> depositMoney();
                    case 3 -> withdrawMoney();
                    case 4 -> transferMoney();
                    case 5 -> checkBalance();
                    case 6 -> viewAccountDetails();
                    case 7 -> viewTransactionHistory();
                    case 8 -> displayBankStatistics();
                    case 9 -> {
                        System.out.println("Thank you for using our Bank System! 👋");
                        running = false;
                    }
                    default -> System.out.println("❌ Invalid option! Please try again.");
                }
            } catch (Exception e) {
                System.out.println("❌ Error: " + e.getMessage());
            }
            
            if (running) {
                System.out.println("\nPress Enter to continue...");
                scanner.nextLine();
            }
        }
        scanner.close();
    }

    private void displayMainMenu() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("🏦 BANK MANAGEMENT SYSTEM");
        System.out.println("=".repeat(50));
        System.out.println("1. 📝 Create New Account");
        System.out.println("2. 💵 Deposit Money");
        System.out.println("3. 💳 Withdraw Money");
        System.out.println("4. 🔄 Transfer Money");
        System.out.println("5. 📊 Check Balance");
        System.out.println("6. 👤 View Account Details");
        System.out.println("7. 📋 View Transaction History");
        System.out.println("8. 📈 Bank Statistics");
        System.out.println("9. 🚪 Exit");
        System.out.println("=".repeat(50));
    }

    private void createAccount() {
        System.out.println("\n--- Create New Account ---");
        
        String name = getStringInput("Enter account holder name: ");
        if (!InputValidator.isValidName(name)) {
            System.out.println("❌ Invalid name format!");
            return;
        }

        double initialDeposit = getDoubleInput("Enter initial deposit: $");
        if (!InputValidator.isValidAmount(initialDeposit)) {
            System.out.println("❌ Invalid amount!");
            return;
        }

        System.out.println("Account types: SAVINGS, CURRENT, SALARY");
        String accountType = getStringInput("Enter account type: ").toUpperCase();
        if (!InputValidator.isValidAccountType(accountType)) {
            System.out.println("❌ Invalid account type!");
            return;
        }

        try {
            String accountNumber = bankService.createAccount(name, initialDeposit, accountType);
            System.out.println("✅ Account created successfully!");
            System.out.println("📋 Account Number: " + accountNumber);
        } catch (InvalidAmountException e) {
            System.out.println("❌ " + e.getMessage());
        }
    }

    private void depositMoney() {
        System.out.println("\n--- Deposit Money ---");
        String accountNumber = getAccountNumberInput();
        double amount = getDoubleInput("Enter deposit amount: $");

        try {
            bankService.deposit(accountNumber, amount);
            Account account = bankService.getAccount(accountNumber);
            System.out.println("✅ Deposit successful!");
            System.out.println("💰 New Balance: $" + account.getBalance());
        } catch (AccountNotFoundException | InvalidAmountException e) {
            System.out.println("❌ " + e.getMessage());
        }
    }

    private void withdrawMoney() {
        System.out.println("\n--- Withdraw Money ---");
        String accountNumber = getAccountNumberInput();
        double amount = getDoubleInput("Enter withdrawal amount: $");

        try {
            bankService.withdraw(accountNumber, amount);
            Account account = bankService.getAccount(accountNumber);
            System.out.println("✅ Withdrawal successful!");
            System.out.println("💰 Remaining Balance: $" + account.getBalance());
        } catch (AccountNotFoundException | InvalidAmountException | InsufficientFundsException e) {
            System.out.println("❌ " + e.getMessage());
        }
    }

    private void transferMoney() {
        System.out.println("\n--- Transfer Money ---");
        String fromAccount = getAccountNumberInput();
        String toAccount = getAccountNumberInput();
        double amount = getDoubleInput("Enter transfer amount: $");

        try {
            bankService.transfer(fromAccount, toAccount, amount);
            System.out.println("✅ Transfer successful!");
        } catch (AccountNotFoundException | InvalidAmountException | InsufficientFundsException e) {
            System.out.println("❌ " + e.getMessage());
        }
    }

    private void checkBalance() {
        System.out.println("\n--- Check Balance ---");
        String accountNumber = getAccountNumberInput();

        try {
            Account account = bankService.getAccount(accountNumber);
            System.out.println("💰 Account Balance: $" + account.getBalance());
        } catch (AccountNotFoundException e) {
            System.out.println("❌ " + e.getMessage());
        }
    }

    private void viewAccountDetails() {
        System.out.println("\n--- Account Details ---");
        String accountNumber = getAccountNumberInput();

        try {
            Account account = bankService.getAccount(accountNumber);
            System.out.println(account);
        } catch (AccountNotFoundException e) {
            System.out.println("❌ " + e.getMessage());
        }
    }

    private void viewTransactionHistory() {
        System.out.println("\n--- Transaction History ---");
        String accountNumber = getAccountNumberInput();

        try {
            List<Transaction> transactions = bankService.getAccountTransactions(accountNumber);
            if (transactions.isEmpty()) {
                System.out.println("No transactions found for this account.");
            } else {
                System.out.println("Transaction History for Account: " + accountNumber);
                for (Transaction transaction : transactions) {
                    System.out.println(transaction);
                }
            }
        } catch (AccountNotFoundException e) {
            System.out.println("❌ " + e.getMessage());
        }
    }

    private void displayBankStatistics() {
        System.out.println("\n--- Bank Statistics ---");
        System.out.println("📊 Total Accounts: " + bankService.getTotalAccounts());
        System.out.println("💰 Total Bank Balance: $" + bankService.getTotalBankBalance());
    }

    private String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("❌ Please enter a valid number!");
            }
        }
    }

    private double getDoubleInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Double.parseDouble(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("❌ Please enter a valid amount!");
            }
        }
    }

    private String getAccountNumberInput() {
        String accountNumber;
        do {
            accountNumber = getStringInput("Enter account number: ").toUpperCase();
            if (!InputValidator.isValidAccountNumber(accountNumber)) {
                System.out.println("❌ Invalid account number format! Format: ACC000001");
            }
        } while (!InputValidator.isValidAccountNumber(accountNumber));
        return accountNumber;
    }

    public static void main(String[] args) {
        BankApp app = new BankApp();
        app.start();
    }
}