package ru.otus.interfaces;

import ru.otus.Container.CellImpl;
import ru.otus.Container.CellUnit;

public interface Cell extends Comparable<CellImpl>{
    void addMoneyToCell(CellUnit bill);
    int getMoneyInCell();
    boolean isEmptyCell();
}
