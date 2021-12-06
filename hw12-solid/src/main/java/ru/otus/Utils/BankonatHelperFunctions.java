package ru.otus.Utils;

import ru.otus.Container.CellUnit;

import java.util.Arrays;

public class BankonatHelperFunctions {
    /*
    check if user selected existed bill in bankomat, if not null return CellUnit
     */
    public static CellUnit insertedBill(int input) {
        return Arrays.stream(CellUnit.values()).filter(c -> c.getNominal() == input).findFirst().orElse(null);
    }

    /*
    check if user selected value and amount is divided by minimum nominal value of Cell unit, if ok return input value
    */
    public static int validWithdrawalAmount(int input) {
        int minNominal = Arrays.stream(CellUnit.values()).mapToInt(CellUnit::getNominal).min().orElse(-1);
        return input % minNominal == 0 ? input : -1;
    }
}
