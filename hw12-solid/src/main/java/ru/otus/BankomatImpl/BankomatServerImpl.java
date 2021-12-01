package ru.otus.BankomatImpl;

import ru.otus.Client.ClientImpl;
import ru.otus.Container.CellImpl;
import ru.otus.Container.CellUnit;
import ru.otus.Container.CurrencySlotImpl;
import ru.otus.interfaces.BankomatServer;
import ru.otus.interfaces.Handler;

import java.util.Scanner;
import java.util.logging.Logger;

import static ru.otus.Utils.BankonatHelperFunctions.*;

public class BankomatServerImpl implements BankomatServer {

    private static final Logger log = Logger.getLogger(BankomatServerImpl.class.getName());

    private final BankomatImpl bankomat;
    private final ClientImpl client;

    public BankomatServerImpl(BankomatImpl bankomat, ClientImpl client) {
        this.bankomat = bankomat;
        this.client = client;
    }

    public BankomatImpl getBankomat() {
        return bankomat;
    }

    public ClientImpl getClient() {
        return client;
    }

    @Override
    @Handler // marks that this function to be called by user request!
    public void addMoney(String selectedCurrency){
        printAllowableBills();
        System.out.println("###Add money on your account!###");
        while (true){
            System.out.println("Please insert money in bankomat, type amount:");
            Scanner userInput = new Scanner(System.in);
            String userOption = userInput.nextLine().toLowerCase().strip();
            if (userOption.equals("0"))System.exit(0);// if user type 0, then quit bankomat server
            CellUnit rightBill = insertedBillValue(userOption); // check if inserted bill is valid
            if (rightBill==null){
                System.out.println("Bill cannot be recognized, please try again!");
            }else {
                CurrencySlotImpl selectedSlot = bankomat.getContainer().getSlotByCurrency(selectedCurrency); // get slot by currency type
                if (selectedSlot!=null){ // if slot exists then get cell with specified nominal
                    CellImpl cell = selectedSlot.getCellByName(rightBill.name());
                    if (cell!=null){ // if user typed correct nominal name then inset money in bankomat and
                        cell.addMoneyToCell(rightBill);
                        client.addMoney(selectedCurrency, rightBill.getNominal()); // and add money to client account
                        System.out.println("Successfully inserted!");
                        System.out.println("Card balance: " + client.moneyBalanceOnCard(selectedCurrency));
                    }
                }else {
                    System.out.println("something is wrong with your bill, try again!");
                    log.warning("bill is not inserted, server error");
                }
            }
        }
    }

    @Override
    @Handler
    public void moneyWithdrawal(String selectedCurrency){
        System.out.println("How much money you want to get?");
        while (true){
            Scanner userInput = new Scanner(System.in);
            String userOption = userInput.nextLine().toLowerCase().strip();
            if (userOption.equals("0"))System.exit(0);// if user type 0, then quit bankomat server
            int userAmount = userAmountValue(userOption);
            if (userAmount==-1){ // if -1 then user typed amount is not multiple to any bill
                System.out.println("amount cannot be served by bankomat! Please try another amount...");
            }else {
                if (userAmount < bankomat.getContainer().getSlotByCurrency(selectedCurrency).getMoneyInSlot() // check if enough money in bankomat and on client card
                        && userAmount < client.moneyBalanceOnCard(selectedCurrency)){
                    bankomat.moneyWithdrawal(selectedCurrency,userAmount); // then withdraw money from bankomat and client card
                    client.moneyWithdrawal(selectedCurrency,userAmount);
                    System.out.println("Successfully done!");
                    System.out.println("Card balance: " + client.moneyBalanceOnCard(selectedCurrency));
                }else {
                    System.out.println("Not enough money on card or in bankomat, try another amount!");
                }
            }
        }
    }
}
