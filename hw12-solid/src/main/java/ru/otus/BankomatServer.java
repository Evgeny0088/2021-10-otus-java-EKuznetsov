package ru.otus;

import ru.otus.BankomatImpl.BankomatImpl;
import ru.otus.BankomatImpl.BankomatHandlerImpl;
import ru.otus.Client.ClientBuilder;
import ru.otus.Client.ClientImpl;
import ru.otus.Container.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Scanner;

import static ru.otus.Utils.BankonatHelperFunctions.*;

public class BankomatServer {

    public static void main(String[] args) throws InvocationTargetException, IllegalAccessException {

        BankomatImpl bankomat = new BankomatImpl("Tinkoff",new ContainerImpl());

        CurrencySlotImpl currencySlot1 = CurrencySlotBuilder.currencySlotBuild(CurrencyType.RUB);
        CurrencySlotImpl currencySlot2 = CurrencySlotBuilder.currencySlotBuild(CurrencyType.USD);
        CurrencySlotImpl currencySlot3 = CurrencySlotBuilder.currencySlotBuild(CurrencyType.EUR);

        bankomat.getContainer().addCurrencySlot(currencySlot1);
        bankomat.getContainer().addCurrencySlot(currencySlot2);
        bankomat.getContainer().addCurrencySlot(currencySlot3);

        ClientImpl client = ClientBuilder.clientBuilder();
        client.addMoney(CurrencyType.RUB.name(),5000);

        BankomatHandlerImpl bankomatProcessor = new BankomatHandlerImpl(bankomat,client);

        runServer(bankomatProcessor);
    }

    public static void runServer(BankomatHandlerImpl bankomatProcessor) throws InvocationTargetException, IllegalAccessException {
        System.out.println("#####if you want to quit, please type 0######\n");
        String selectedCurrency;
        if (!bankomatGreetingInfo(bankomatProcessor)){
            System.out.println("client card is disabled, operation is aborted, please try later");
            System.exit(0);
        }

        Map<String, Integer> currencyAvailableInBankomat = getCurrencyOptionsInBankomat(bankomatProcessor.getBankomat().getContainer()); // get currencies in bankomat

        if (currencyAvailableInBankomat.isEmpty()){ // check if currency slots is not empty
            System.exit(0);
        }
        while (true){
            Scanner userInput = new Scanner(System.in);
            String userOption = userInput.nextLine().toLowerCase().strip();
            if (userOption.equals("0"))System.exit(0);// if user type 0, then quit bankomat bankomatProcessor
            selectedCurrency = userCurrencySelection(userOption,currencyAvailableInBankomat); // user selects currency for future operations
            if (selectedCurrency.isBlank()){ // if no currency in bankomat, print notification and repeat attempt
                wrongSelectionHandler(userOption);
            }else {
                System.out.println("Selected currency: " + selectedCurrency);
                break;
            }
        }

        Class<?> bankomatProcessorClass = bankomatProcessor.getClass(); // get BankomatHandler class for future method calls depends on user selection
        Map<Method, Integer> serverOptionsAvailableForUser = getBankomatServerOptions(bankomatProcessorClass); // get list of bankomatProcessor options
        Method serverOption;
        if (serverOptionsAvailableForUser.isEmpty()){ // verify if any option is available
            System.exit(0);
        }
        while (true){
            Scanner userInput = new Scanner(System.in);
            String userOption = userInput.nextLine().toLowerCase().strip();
            if (userOption.equals("0"))System.exit(0);// if user type 0, then quit bankomat bankomatProcessor
            serverOption = userServerOptionsSelection(userOption,serverOptionsAvailableForUser); // user try to get one of the bankomatProcessor option
            if (serverOption==null){
                wrongSelectionHandler(userOption);
            }else {
                serverOption.invoke(bankomatProcessor,selectedCurrency);
            }
        }
    }
}
