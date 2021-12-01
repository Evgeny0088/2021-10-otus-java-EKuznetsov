package ru.otus.Client;

import ru.otus.interfaces.Client;

import java.util.HashMap;
import java.util.Map;

/*
card holder
 */
public class ClientImpl implements Client {

    private final String cardName;
    private boolean accountIsActive;
    private Map<String, Integer> cardAvailableAccounts;

    public ClientImpl(String cardName){
        this.cardName = cardName;
        this.cardAvailableAccounts = new HashMap<>();
    }

    public String getCardName() {
        return cardName;
    }

    public Map<String, Integer> getcardAvailableAcounts() {
        return cardAvailableAccounts;
    }

    public void setcardAvailableAcounts(Map<String, Integer> cardAvailableAccounts) {
        this.cardAvailableAccounts = cardAvailableAccounts;
    }

    /*
    check if client,s is not blocked
     */
    public boolean isAccountIsActive() {
        return accountIsActive;
    }

    public void setAccountIsActive(boolean accountIsActive) {
        this.accountIsActive = accountIsActive;
    }

    /*
    add money on card if it is empty or sum up this amount if card is not empty
     */
    @Override
    public void addMoney(String currency, int amount) {
        if (cardAvailableAccounts.get(currency)==null){
            cardAvailableAccounts.putIfAbsent(currency,amount);
        }else {
            cardAvailableAccounts.computeIfPresent(currency,(k,v)->v+amount);
        }
    }

    /*
    checks if enough money on card before client use it
     */
    @Override
    public boolean isEnoughMoney(String currency, int amount){
        return cardAvailableAccounts.get(currency)-amount>-1;
    }

    /*
    withdraw money from card
     */
    @Override
    public void moneyWithdrawal(String currency, int amount) {
        cardAvailableAccounts.computeIfPresent(currency,(k,v)->v-amount);
    }

    /*
    get card balance on specific currency
     */
    @Override
    public int moneyBalanceOnCard(String currency) {
        return cardAvailableAccounts.get(currency)!=null ? cardAvailableAccounts.get(currency) : 0;
    }
}
