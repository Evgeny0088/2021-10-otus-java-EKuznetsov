package ru.otus.Utils;

import ru.otus.BankomatImpl.BankomatServerImpl;
import ru.otus.Container.CellUnit;
import ru.otus.Container.ContainerImpl;
import ru.otus.Container.CurrencySlotImpl;
import ru.otus.interfaces.Handler;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class BankonatHelperFunctions {

    /*
    provides general info about bankomat and client status
     */
    public static boolean bankomatGreetingInfo(BankomatServerImpl bankomatServer){
        String delimiter = "\n";
        boolean cardIsActive = bankomatServer.getClient().isAccountIsActive();
        String greeting = "Welcome to Bankomat!" + delimiter +
                "Bankomat info:" + delimiter +
                "model: " + bankomatServer.getBankomat().getName() + delimiter +
                "version: " + bankomatServer.getBankomat().VERSION + delimiter +
                "Client info:" + delimiter +
                "card id: " + bankomatServer.getClient().getCardName() + delimiter +
                "card status: " + (cardIsActive ? "active" : "disabled");
        System.out.println(greeting);
        return cardIsActive;
    }

    /*
    Check if bankomat has any currency, and it is not empty, pack them in Map for future options in server run
     */
    public static Map<String, Integer> getCurrencyOptionsInBankomat(ContainerImpl container){
        Map<String, Integer> currencyOptions = new HashMap<>();
        String delimiter = "\n";
        String showCurrencyOptions;
        if (!container.getCurrencySlots().isEmpty()){
            int i = 1;
            for (CurrencySlotImpl c: container.getCurrencySlots()){
                currencyOptions.put(c.getCurrencyType(),i++);
            }
            showCurrencyOptions =
                    "Available currency in bankomat:" + delimiter + printCurrencyOptions(currencyOptions);
        }else {
            showCurrencyOptions = "Bankomat is empty, please come back later!";
        }
        System.out.println(showCurrencyOptions);
        return currencyOptions;
    }

    /*
    helper to print out currency information
    */
    private static String printCurrencyOptions(Map<String, Integer> options){
        return options.entrySet().stream()
                .map(entry -> entry.getKey() + " - click => " + entry.getValue())
                .collect(Collectors.joining("\n\n"));
    }

    /**
     * Check if user selected valid currency from available options.
     */
    public static String userCurrencySelection(String input, Map<String, Integer> currencyOptions){
        String currencyType;
        try{
            int i = Integer.parseInt(input);
            currencyType = currencyOptions.entrySet().stream()
                    .filter(entry -> entry.getValue()==i)
                    .findFirst().map(Map.Entry::getKey).orElse("");
        }catch(Exception e){
            currencyType = "";
            return currencyType;
        }
        return currencyType;
    }

    /*
    gets bankomat methods available in BankomatServerImpl class, annotated by Handler
     */
    public static Map<Method, Integer> getBankomatServerOptions(Class<?> serverClass){
        String delimiter = "\n";
        String showServerOptions="";
        Map<Method, Integer> options = new HashMap<>();
        int i = 1;
        for (Method m: serverClass.getDeclaredMethods()){
            if (m.isAnnotationPresent(Handler.class)){
                options.put(m,i++);
            }
        }
        if (!options.isEmpty()) {
            showServerOptions = "Available options in bankomat:" + delimiter + printServerOptions(options);
        }else {
            showServerOptions = "Bankomat is broken, please come back later!";
        }
        System.out.println(showServerOptions);
        return options;
    }

    /**
     * Check if user selection is valid from available options.
     */
    public static Method userServerOptionsSelection(String input, Map<Method, Integer> currencyOptions){
        Method serverOption;
        try{
            int i = Integer.parseInt(input);
            serverOption = currencyOptions.entrySet().stream()
                    .filter(entry -> entry.getValue()==i)
                    .findFirst().map(Map.Entry::getKey).orElse(null);
        }catch(Exception e){
            return null;
        }
        return serverOption;
    }

    /*
    prints out methods available in bankomat server
     */
    private static String printServerOptions(Map<Method, Integer> options){
        return options.entrySet().stream()
                .map(entry -> entry.getKey().getName() + " - click => " + entry.getValue())
                .collect(Collectors.joining("\n\n"));
    }

    /*
    prints out if user selected wrong option
     */
    public static void wrongSelectionHandler(String userInput){
        System.out.printf("you typed: %s. That option does not exits, please try again...",userInput);
    }

    /*
    print currency bills, which bankomat able to accept
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
    public static CellUnit insertedBillValue(String input){
        try{
            int rightBill = Integer.parseInt(input);
            return Arrays.stream(CellUnit.values()).filter(c->c.getNominal()==rightBill).findFirst().orElse(null);
        }catch(Exception e){
            return null;
        }
    }

    /*
    check is user selected valid value and amount is divided by minimum nominal value of Cell unit
    */
    public static int userAmountValue(String input){
        try{
            int rightValue = Integer.parseInt(input);
            int minNominal = Arrays.stream(CellUnit.values()).mapToInt(CellUnit::getNominal).min().orElse(-1);
            return rightValue%minNominal==0 ? rightValue : -1;
        }catch(Exception e){
            return -1;
        }
    }
}
