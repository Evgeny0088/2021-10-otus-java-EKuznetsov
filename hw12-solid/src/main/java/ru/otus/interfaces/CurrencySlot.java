package ru.otus.interfaces;

import ru.otus.Container.CellImpl;

public interface CurrencySlot {
    void addCell(CellImpl slot);
    int getMoneyInSlot();
    CellImpl getCellByName(String cellName);
}
