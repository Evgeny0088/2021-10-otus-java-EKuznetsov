package ru.otus.interfaces;

import ru.otus.Container.CellImpl;
import ru.otus.Container.CellUnit;

import java.util.Map;

public interface Bankomat {
    void addMoney(int amount);
    Map<String, Integer> moneyWithdrawal(int amount);
    void addCell(CellImpl cell);
    int getMoneyInBankomat();
    CellImpl getCellByName(CellUnit cellName);
}
