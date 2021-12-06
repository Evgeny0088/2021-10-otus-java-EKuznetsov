package ru.otus.BankomatImpl;

import ru.otus.Container.CellImpl;
import ru.otus.Container.CellUnit;
import ru.otus.Container.ContainerImpl;
import ru.otus.interfaces.Bankomat;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ru.otus.Utils.BankonatHelperFunctions.*;

public class BankomatImpl implements Bankomat {

    public final String VERSION = "0.1";
    private final String name;
    private final ContainerImpl container;

    public BankomatImpl(String name) {
        this.name = name;
        this.container = new ContainerImpl();
    }

    public String getName() {
        return name;
    }

    public ContainerImpl getContainer() {
        return container;
    }

    /*
    get available bills in bankomat before next operation
    */
    private void getAvailableBillsInBankomat() {
        System.out.println("Allowable bills which bankomat can accept:");
        container.getCells().stream().filter(cell -> !cell.isEmptyCell())
                .forEach(cell -> System.out.println("=> " + cell.getBillNominalName()));
    }

    /*
    adding money in bankomat, verifying that input amount equals one of the bill nominal!
    */
    @Override
    public void addMoney(int amount) {
        getAvailableBillsInBankomat();
        CellUnit rightBill = insertedBill(amount); // check if inserted bill is valid
        if (rightBill == null) {
            System.out.println("Bill cannot be recognized, please try another amount!");
        } else {
            container.getCellByNominal(rightBill).addMoneyToCell(rightBill);
            System.out.println("Money successfully inserted!");
        }
        System.out.println("Money in bankomat: " + getMoneyInBankomat());
    }

    /*
    Bankomat give money by minimum count of bills, cells are sorted by nominal value
     */
    @Override
    public void moneyWithdrawal(int amount) {
        getAvailableBillsInBankomat();
        int userAmount = validWithdrawalAmount(amount);
        if (userAmount == -1 || userAmount > getMoneyInBankomat()) { // if -1 then user typed amount is not multiple to any bill
            System.out.println("Failed to withdraw! Amount cannot be served or not enough money in bankomat! Please try another amount...");
            return;
        }
        List<CellImpl> sortedCells = container.getCells().stream().sorted().toList(); // get sortedCells with required currency type and sorted by nominal value
        int currentCell = sortedCells.size() - 1; // current cell in iteration
        int currentNomimal; // current nominal value in iteration
        int countOfBillsInCell; // how many bills left in cell
        int countOfBillsToClient = 0; // count of bills withdrawn from bankomat, needed for information
        String currentBillName; // bill name in iteration, needed for information
        Map<String, Integer> moneyFromBankomat = new HashMap<>();
        while (userAmount > 0 && currentCell > -1) {
            countOfBillsInCell = sortedCells.get(currentCell).getBillsInCell().size();
            currentBillName = sortedCells.get(currentCell).getBillNominalName();
            currentNomimal = CellUnit.valueOf(currentBillName).getNominal();
            while (countOfBillsInCell > 0) {
                countOfBillsInCell--;
                if (userAmount < currentNomimal) break;
                userAmount -= currentNomimal;
                countOfBillsToClient++;
                sortedCells.get(currentCell).getBillsInCell().remove(countOfBillsInCell);
                moneyFromBankomat.put(currentBillName, countOfBillsToClient);
            }
            countOfBillsToClient = 0;
            currentCell--;
        }
        if (userAmount > 0) {
            System.out.println("Failed to withdraw! Bankomat does not have enough bills");
        } else {
            System.out.println("Money successfully withdrawn:");
            System.out.println(moneyFromBankomat.entrySet().stream()
                    .map(entry -> entry.getKey() + " - " + entry.getValue())
                    .collect(Collectors.joining("\n")));
            System.out.println("Money in bankomat: " + getMoneyInBankomat());
        }
    }

    /*
    Counts money left in bankomat (summary of all cells in this slot)
    */
    @Override
    public int getMoneyInBankomat() {
        return container.getCells().stream().map(CellImpl::getMoneyInCell).reduce(0, Integer::sum);
    }
}
