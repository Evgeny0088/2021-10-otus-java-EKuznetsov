package ru.otus.Container;

import ru.otus.interfaces.Container;

import java.util.HashSet;
import java.util.Set;

/*
main container of bankomat, storage for all slots with different currency types
Can be inserted into bankomat as empty and fill up by slots later
 */
public class ContainerImpl implements Container {

    public final String VERSION = "0.1";
    private Set<CurrencySlotImpl> currencySlots;

    public ContainerImpl(){
        this.currencySlots = new HashSet<>();
    }

    public Set<CurrencySlotImpl> getCurrencySlots() { // gets all slots in container
        return currencySlots;
    }

    public void setCurrencySlots(Set<CurrencySlotImpl> currencySlots) {
        this.currencySlots = currencySlots;
    }

    /*
    Adding new currency slot if not exists yet
     */
    @Override
    public void addCurrencySlot(CurrencySlotImpl currency) {
        currencySlots.add(currency);
    }

    /*
    Get specific slot by currency type, or null if not exists
     */
    @Override
    public CurrencySlotImpl getSlotByCurrency(String currency){
        return currencySlots.stream().filter(c->c.getCurrencyType().equals(currency))
                .findFirst().orElse(null);
    }
}
