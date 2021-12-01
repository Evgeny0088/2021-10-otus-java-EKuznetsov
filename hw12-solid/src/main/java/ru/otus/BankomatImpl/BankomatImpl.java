package ru.otus.BankomatImpl;

import ru.otus.Container.ContainerImpl;
import ru.otus.Container.CurrencySlotImpl;
import ru.otus.Container.CellImpl;
import ru.otus.Container.CellUnit;
import ru.otus.interfaces.Bankomat;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/*
Bankomat unit, contains:
    - name
    - version
    - container (can be empty)
 */
public class BankomatImpl implements Bankomat {

    private static final Logger log = Logger.getLogger(CellImpl.class.getName());

    public final String VERSION = "0.1";
    private final String name;
    private ContainerImpl container;

    public BankomatImpl(String name,ContainerImpl container){
        this.name = name;
        this.container = container;
    }

    public String getName() {
        return name;
    }

    public ContainerImpl getContainer() {
        return container;
    }

    public void setContainer(ContainerImpl container) {
        this.container = container;
    }

    /*
    adding money in bankomat, taking currency type and verified bill nominal
     */
    @Override
    public void addMoney(String currency, CellUnit bill){
        CurrencySlotImpl currencySlots = container.getSlotByCurrency(currency);
        if (currencySlots!=null){
            for (CellImpl cell : currencySlots.getCells()) {
                if (cell.getBillNominalValue() == bill.getNominal()){
                    cell.addMoneyToCell(bill);
                    break;
                }
            }
        }else {
            log.warning("required slot is not found!");
        }
    }

    /*
    Bankomat give money by minimum count of bills, cells are sorted by nominal value
     */
    @Override
    public Map<String, Integer> moneyWithdrawal(String currency, int amount){
        List<CellImpl> cells = container.getSlotByCurrency(currency).getCells().stream().sorted().toList(); // get cells with required currency type and sorted by nominal value
        int currentCell = cells.size()-1; // current cell in iteration
        int currentNomimal; //
        int countOfBillsInCell;
        int countOfBillsToClient=0;
        String currentBillName;
        Map<String, Integer> moneyToClient = new HashMap<>();
        while (amount>0 && currentCell>-1){
            countOfBillsInCell = cells.get(currentCell).getBillsInCell().size();
            if (countOfBillsInCell>0){
                currentBillName = cells.get(currentCell).getBillNominalName();
                currentNomimal = cells.get(currentCell).getBillsInCell().get(0).getNominal();
                while (true){
                    countOfBillsInCell--;
                    if (!(amount>=currentNomimal)) break;
                    amount-=currentNomimal;
                    countOfBillsToClient++;
                    cells.get(currentCell).getBillsInCell().remove(countOfBillsInCell-1);
                }
                if (countOfBillsToClient>0) moneyToClient.put(currentBillName, countOfBillsToClient);
            }
            currentCell--;
        }
        System.out.println(moneyToClient.entrySet().stream()
                .map(entry -> entry.getKey() + " - " + entry.getValue())
                .collect(Collectors.joining("\n\n")));
        return moneyToClient;
    }
}
