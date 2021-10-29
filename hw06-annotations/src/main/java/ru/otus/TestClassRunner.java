package ru.otus;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Deque;
import static ru.otus.utils.TestCasesUtils.*;

public class TestClassRunner {

    public static void main(String[] args) throws ClassNotFoundException, InvocationTargetException,
            NoSuchMethodException, InstantiationException, IllegalAccessException {
        testRunner(Class.forName("ru.otus.TestClass"));
    }

    /*
    method for running all testCases
     */
    public static void testRunner(Class<?> TestClass) throws NoSuchMethodException, InstantiationException,
            IllegalAccessException, InvocationTargetException {

        // counters for passed/failed tests
        int testsPassed = 0;
        int testsFailed = 0;

        // list of tests annotated at the specified class
        Deque<Method> testCases = getTestCases(TestClass);

        System.out.println("\nTESTS RESULTS:");
        while (!testCases.isEmpty()){
            if (createTestCase(TestClass, testCases.pop())){
                testsPassed++;
            }else {
                testsFailed++;
            }
        }
        // show results for all tests
        results(testsPassed, testsFailed);
    }
}
