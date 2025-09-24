package com.bank.util;

import java.util.regex.Pattern;

public class InputValidator {
    private static final Pattern NAME_PATTERN = Pattern.compile("^[a-zA-Z\\s]{2,50}$");
    private static final Pattern ACCOUNT_NUMBER_PATTERN = Pattern.compile("^ACC\\d{6}$");

    public static boolean isValidName(String name) {
        return name != null && NAME_PATTERN.matcher(name).matches();
    }

    public static boolean isValidAccountNumber(String accountNumber) {
        return accountNumber != null && ACCOUNT_NUMBER_PATTERN.matcher(accountNumber).matches();
    }

    public static boolean isValidAmount(double amount) {
        return amount > 0 && amount <= 1000000;
    }

    public static boolean isValidAccountType(String accountType) {
        return accountType != null && 
               (accountType.equalsIgnoreCase("SAVINGS") || 
                accountType.equalsIgnoreCase("CURRENT") || 
                accountType.equalsIgnoreCase("SALARY"));
    }
}