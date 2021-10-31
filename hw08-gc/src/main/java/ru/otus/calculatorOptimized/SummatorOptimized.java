package ru.otus.calculatorOptimized;

import java.util.ArrayList;
import java.util.List;

public class SummatorOptimized {
    private int sum = 0;
    private int prevValue = 0;
    private int prevPrevValue = 0;
    private int sumLastThreeValues = 0;
    private int someValue = 0;
    private final List<DataOptimized> logValues = new ArrayList<>();

    public void calc(DataOptimized data) {
        logValues.add(data);
        if (logValues.size() % 6_600_000 == 0) {
            logValues.clear();
        }
        sum += data.getValue();

        sumLastThreeValues = data.getValue() + prevValue + prevPrevValue;

        prevPrevValue = prevValue;
        prevValue = data.getValue();

        /*
        put all constant values into temporarily int before iteration
         */
        int tempValue = sumLastThreeValues * sumLastThreeValues / (data.getValue() + 1) - sum;
        int tempLogValuesSize = logValues.size();

        for (int idx = 0; idx < 3; idx++) {
            someValue += tempValue;
            someValue = Math.abs(someValue) + tempLogValuesSize;
        }
    }

    public int getSum() {
        return sum;
    }

    public int getPrevValue() {
        return prevValue;
    }

    public int getPrevPrevValue() {
        return prevPrevValue;
    }

    public int getSumLastThreeValues() {
        return sumLastThreeValues;
    }

    public int getSomeValue() {
        return someValue;
    }
}
