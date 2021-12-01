package ru.otus.interfaces;

import ru.otus.Container.CurrencySlotImpl;

public interface Container {

    void addCurrencySlot(CurrencySlotImpl currency);
    CurrencySlotImpl getSlotByCurrency(String currency);

}
