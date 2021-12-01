package ru.otus.interfaces;

public interface Client {
    boolean isEnoughMoney(String currency, int amount);
    void addMoney(String currency, int amount);
    void moneyWithdrawal(String currency, int amount);
    int moneyBalanceOnCard(String currency);
}
