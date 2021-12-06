package ru.otus.Container;

/*
bill unit for bankomat
 */
public enum CellUnit {

    Bill50(50),Bill100(100),Bill500(500);

    private final int nominal;

    CellUnit(int nominal){
        this.nominal = nominal;
    }

    public int getNominal() {
        return nominal;
    }
}
