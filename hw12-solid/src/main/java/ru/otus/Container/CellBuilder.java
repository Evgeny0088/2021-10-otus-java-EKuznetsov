package ru.otus.Container;

public class CellBuilder {
    public static CellImpl cellBuilder(CellUnit cellUnit, int billsCount){
        CellImpl cell = new CellImpl(cellUnit);
        for (int i=0; i<billsCount;i++){
            cell.addMoneyToCell(cellUnit);
        }
        return cell;
    }
}
