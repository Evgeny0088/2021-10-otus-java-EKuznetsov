package ru.otus.Container;
import ru.otus.interfaces.Cell;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/*
Storage cell for specific bill value (50, 100 or 500)
 */
public class CellImpl implements Cell{

    private final String billNominalName;
    private final int billNominalValue;
    private List<CellUnit> billsInCell;

    public CellImpl(CellUnit cellUnit) {
        this.billNominalName = cellUnit.name();
        this.billNominalValue = cellUnit.getNominal();
        this.billsInCell = new ArrayList<>();
    }

    public String getBillNominalName() {
        return billNominalName;
    }

    public int getBillNominalValue() {
        return billNominalValue;
    }

    public List<CellUnit> getBillsInCell() {
        return billsInCell;
    } //gets all bills in cell

    public void setBillsInCell(List<CellUnit> billsInCell) {
        this.billsInCell = billsInCell;
    }

    /*
    gets how much money in cell
     */
    @Override
    public int getMoneyInCell() {
        return billsInCell.stream().map(CellUnit::getNominal).reduce(0,Integer::sum);
    }

    /*
    adding new bill in cell. Before accept bill to be checked if it is proper nominal value
    if not, then it should be refused
     */
    @Override
    public void addMoneyToCell(CellUnit bill){
        if (bill.getNominal() == billNominalValue){
            billsInCell.add(bill);
        }else {
            System.out.println("bill has different nominal, refused to insert!");
        }
    }

    /*
    checks if cell is empty (not any bills inside)
     */
    @Override
    public boolean isEmptyCell() {
        return billsInCell.isEmpty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CellImpl cell = (CellImpl) o;
        return billNominalValue == cell.getBillNominalValue();
    }

    @Override
    public int hashCode() {
        return Objects.hash(billNominalValue);
    }

    @Override
    public int compareTo(CellImpl cell) {
        return Integer.compare(this.billNominalValue,cell.billNominalValue);
    }
}
