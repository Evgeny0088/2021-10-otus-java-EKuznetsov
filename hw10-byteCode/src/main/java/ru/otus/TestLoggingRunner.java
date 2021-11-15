package ru.otus;

import ru.otus.Annotations.TestLogging;
import ru.otus.TestLogging.TestLoggingProxy;

public class TestLoggingRunner {

    public static void main(String[] args) throws NoSuchMethodException {
        TestLogging testLogging = TestLoggingProxy.createTestLoggingInstance();
        testLogging.calculation(20);
        testLogging.stringCalculation("string!");
        testLogging.calculation(10,5);
        testLogging.emptyCalculation();
        testLogging.calculation(10, -2, "third parameter!");
    }
}
