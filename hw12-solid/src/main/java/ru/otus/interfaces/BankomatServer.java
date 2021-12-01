package ru.otus.interfaces;

public interface BankomatServer {
    void addMoney(String selectedCurrency);
    void moneyWithdrawal(String selectedCurrency);
}
