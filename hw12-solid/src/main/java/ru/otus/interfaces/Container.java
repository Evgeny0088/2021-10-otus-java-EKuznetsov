package ru.otus.interfaces;

import ru.otus.Container.CellImpl;
import ru.otus.Container.CellUnit;

public interface Container {

    void addCell(CellImpl cell);
    CellImpl getCellByNominal(CellUnit insertedBill);
}
