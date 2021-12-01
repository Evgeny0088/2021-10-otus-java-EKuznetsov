package ru.otus.interfaces;

import ru.otus.Container.CellUnit;

import java.util.Map;

public interface Bankomat {

    void addMoney(String currency, CellUnit bill);
    Map<String, Integer> moneyWithdrawal(String currency, int amount);
}
