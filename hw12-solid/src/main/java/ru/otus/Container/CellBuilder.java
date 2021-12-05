package ru.otus.Container;

public class CellBuilder {
    public static CellImpl cellBuilder(CellUnit cellUnit){
        CellImpl cell = new CellImpl(cellUnit);
        for (int i=0; i<3;i++){
            cell.addMoneyToCell(cellUnit);
        }
        return cell;
    }
}
