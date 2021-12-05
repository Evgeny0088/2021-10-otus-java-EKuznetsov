package ru.otus;

import ru.otus.BankomatImpl.BankomatImpl;
import ru.otus.Container.CellBuilder;
import ru.otus.Container.CellUnit;

public class BankomatRunner {
    public static void main(String[] args) {
        BankomatImpl bankomat = new BankomatImpl("tinkoff");

        // load bankomat with new cells and nominals
        bankomat.addCell(CellBuilder.cellBuilder(CellUnit.Bill50));
        bankomat.addCell(CellBuilder.cellBuilder(CellUnit.Bill100));
        bankomat.addCell(CellBuilder.cellBuilder(CellUnit.Bill500));

        System.out.printf("\n###  bankomat is created, current balance: %d  ###\n\n", bankomat.getMoneyInBankomat());

        // if we add cell which already exists then method won,t be executed
//        bankomat.addCell(CellBuilder.cellBuilder(CellUnit.Bill50));

        System.out.println("add amount - 100:");
        bankomat.addMoney(100);
        System.out.println("\n");

        System.out.println("add amount - 500:");
        bankomat.addMoney(500);
        System.out.println("\n");

        System.out.println("add amount - 40:");
        bankomat.addMoney(40);
        System.out.println("\n");

        System.out.println("withdrawal amount - 450:");
        bankomat.moneyWithdrawal(450);
        System.out.println("\n");

        System.out.println("withdrawal amount - 900:");
        bankomat.moneyWithdrawal(900);
        System.out.println("\n");

        System.out.println("withdrawal amount - 910:");
        bankomat.moneyWithdrawal(910);
        System.out.println("\n");

        System.out.println("withdrawal amount - 200000:");
        bankomat.moneyWithdrawal(200000);
        System.out.println("\n");

    }
}
