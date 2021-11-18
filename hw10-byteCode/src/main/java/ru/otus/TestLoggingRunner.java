package ru.otus;

import ru.otus.Annotations.TestLogging;
import ru.otus.TestLogging.TestLoggingProxy;

public class TestLoggingRunner {

    public static void main(String[] args) throws NoSuchMethodException {

        // list of used parameters
        Double p1 = 11.2;
        Float p2 = 10.1f;
        Integer p3 = 5;
        Boolean p4 = true;
        boolean p5 = false;
        String p6 = "string";
        double p7 = 20.0;
        float p8 = 30.5f;
        Long p9 = 10L;
        long p10 = 10;
        int p11 = 0;
        Integer p12 = 5;
        Character p13 = 'a';
        char p14 = 'b';
        Byte p15 = 1;
        byte p16 = 127;

        TestLogging testLogging = TestLoggingProxy.createTestLoggingInstance();

        testLogging.calculation(p11);
        testLogging.calculation(p6);
        testLogging.calculation(p12);
        testLogging.calculation(p11,p11);
        testLogging.calculation("one", "two");
        testLogging.calculation(p9,p12,p2);
        testLogging.calculation(p10,p8,p7);
        testLogging.calculation(p15, p16);
        testLogging.calculation(p1,p7);
        testLogging.calculation(p13);
        testLogging.calculation(p14);
        testLogging.calculation(p4);
        testLogging.calculation(p5);
        testLogging.stringCalculation(p6);
        testLogging.emptyCalculation();
    }
}