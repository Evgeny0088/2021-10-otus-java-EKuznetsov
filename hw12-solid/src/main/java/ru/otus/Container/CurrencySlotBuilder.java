package ru.otus.Container;

public class CurrencySlotBuilder {
    public static CurrencySlotImpl currencySlotBuild(CurrencyType type){
        CurrencySlotImpl currencySlot = new CurrencySlotImpl(type);
        CellImpl cell1 = CellBuilder.cellLoader(CellUnit.Bill50);
        CellImpl cell2 = CellBuilder.cellLoader(CellUnit.Bill100);
        CellImpl cell3 = CellBuilder.cellLoader(CellUnit.Bill500);
        currencySlot.addCell(cell1);
        currencySlot.addCell(cell2);
        currencySlot.addCell(cell3);
        return currencySlot;
    }
}
