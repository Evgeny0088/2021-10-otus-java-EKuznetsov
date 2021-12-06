package ru.otus.Container;

import ru.otus.interfaces.Container;

import java.util.ArrayList;
import java.util.List;

/*
main container of bankomat, storage for all cells
Can be inserted into bankomat as empty and loaded up later
 */
public class ContainerImpl implements Container {

    public final String VERSION = "0.1";
    private final List<CellImpl> cells;

    public ContainerImpl(){
        this.cells = new ArrayList<>();
    }

    public List<CellImpl> getCells() {
        return cells;
    }

    /*
    Adding new cell with specific nominal (if new cell with nominal exists it should be refused)
    */
    @Override
    public void addCell(CellImpl cell) {
        if (!cells.contains(cell)){
            cells.add(cell);
        }else {
            System.out.println("cell " + cell.getBillNominalName() + "is failed to attach, cell with this nominal already in bankomat!");
        }
    }

    /*
    get cell by specified nominal bill name
    */
    @Override
    public CellImpl getCellByNominal(CellUnit insertedBill){
        return getCells().stream()
                .filter(c -> c.getBillNominalValue() == insertedBill.getNominal()).findFirst().orElse(null);
    }
}
