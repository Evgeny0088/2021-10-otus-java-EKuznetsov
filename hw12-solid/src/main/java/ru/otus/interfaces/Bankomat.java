package ru.otus.interfaces;

public interface Bankomat {
    void addMoney(int amount);
    void moneyWithdrawal(int amount);
    int getMoneyInBankomat();
}
