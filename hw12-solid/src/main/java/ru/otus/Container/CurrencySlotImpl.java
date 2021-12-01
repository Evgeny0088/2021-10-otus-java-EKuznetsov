package ru.otus.Container;

import ru.otus.interfaces.CurrencySlot;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

/*
Slot unit, contains cells with different nominal bills. Store only specific currency type bills! (only RUB OR USD OR EUR)
 */
public class CurrencySlotImpl implements CurrencySlot {

    private static final Logger log = Logger.getLogger(CurrencySlotImpl.class.getName());
    private final String currencyType;
    private List<CellImpl> cells;

    CurrencySlotImpl(CurrencyType type){
        this.currencyType = type.name();
        this.cells = new LinkedList<>();
    }

    public String getCurrencyType() {
        return currencyType;
    }

    public List<CellImpl> getCells() {
        return cells;
    } // gets all cells available in this slot

    public void setCells(LinkedList<CellImpl> cells) {
        this.cells = cells;
    }

    /*
    Adding new cell with specific nominal (if new cell with nominal is exists it should be refused)
     */
    @Override
    public void addCell(CellImpl cell) {
        if (!cells.contains(cell)){
            cells.add(cell);
        }else {
            log.warning("cell " + cell.getBillNominalName() + "is failed to attach, cell with this nominal already in slot!");
        }
    }

    /*
    Counts money left in this slot (summary of all cells in this slot)
     */
    @Override
    public int getMoneyInSlot() {
        return cells.stream().map(CellImpl::getMoneyInCell).reduce(0,Integer::sum);
    }

    /*
    get cell by specified nominal bill name
     */
    @Override
    public CellImpl getCellByName(String cellName) {
        return cells.stream()
                .filter(c->c.getBillNominalName().equals(cellName)).findFirst().orElse(null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CurrencySlotImpl currency = (CurrencySlotImpl) o;
        return currencyType.equals(currency.currencyType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(currencyType);
    }
}
