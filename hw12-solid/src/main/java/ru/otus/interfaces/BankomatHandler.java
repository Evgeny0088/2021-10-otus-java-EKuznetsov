package ru.otus.interfaces;

public interface BankomatHandler {
    void addMoney(String selectedCurrency);
    void moneyWithdrawal(String selectedCurrency);
}
