package ru.otus.Utils;

import ru.otus.Container.CellUnit;
import java.util.Arrays;

public class BankonatHelperFunctions {

    /*
    print bills, which bankomat able to accept
     */
    public static void printAllowableBills(){
        System.out.println("Allowable bills which bankomat can accept:");
        for (CellUnit c: CellUnit.values()){
            System.out.println("=>" + c.name());
        }
    }

    /*
    check is user selected existed bill in bankomat
     */
    public static CellUnit insertedBillValue(int input){
        return Arrays.stream(CellUnit.values()).filter(c->c.getNominal()==input).findFirst().orElse(null);
    }

    /*
    check if user selected value and amount is divided by minimum nominal value of Cell unit
    */
    public static int validWithdrawalAmount(int input){
        int minNominal = Arrays.stream(CellUnit.values()).mapToInt(CellUnit::getNominal).min().orElse(-1);
        return input%minNominal==0 ? input : -1;
    }
}
