package ru.otus.utils;

import ru.otus.TestClass;
import ru.otus.annotations.After;
import ru.otus.annotations.Before;
import ru.otus.annotations.Test;
import ru.otus.annotations.TestDescription;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Objects;

public class TestCasesUtils {

    /*
    method creates list of test methods annotated at the specified class
    */
    public static Deque<Method> getTestCases(Class<?> TestClass){
        Deque<Method> testCases = new ArrayDeque<>();
        for (Method m: TestClass.getDeclaredMethods()){
            if (m.isAnnotationPresent(Test.class)){
                testCases.add(m);
            }
        }
        return testCases;
    }

    /*
    this method creates instance of TestCase object for each test method annotated by @Test
    it should implements @Before methods, @Test method and finally @After methods if any exist
     */
    public static boolean createTestCase(Class<?> TestClass, Method test) throws InvocationTargetException,
            IllegalAccessException, NoSuchMethodException, InstantiationException {

        Constructor<?> constructor = TestClass.getConstructor();
        ru.otus.TestClass testClassInstance = (TestClass) constructor.newInstance();
        String methodDescription = "";
        if (test.isAnnotationPresent(TestDescription.class)){
            methodDescription = test.getAnnotation(TestDescription.class).description();
        }
        try{
            for (Method m: TestClass.getDeclaredMethods()){
                if (m.isAnnotationPresent(Before.class)){
                    m.invoke(testClassInstance);
                }
            }
            test.invoke(testClassInstance);
        } catch (InvocationTargetException | IllegalAccessException e) {
            System.out.println(test.getName() + " -> " + methodDescription + ":" + "FAILED");
            return false;
        }
        finally {
            for (Method m: TestClass.getDeclaredMethods()){
                if (m.isAnnotationPresent(After.class)){
                    m.invoke(testClassInstance);
                }
            }
        }
        System.out.println(test.getName() + " -> " + methodDescription + ":" + "PASSED");
        return true;
    }

    /*
    Prints out summary statistic about passed and failed tests
     */
    public static void results(int testsPassed, int testsFailed){
        System.out.println("\nSUMMARY:");
        if (testsFailed==0){
            System.out.printf("all %d tests passed!\n", testsPassed);
        }
        else if (testsPassed==0){
            System.err.printf("all %d tests failed!\n", testsFailed);
        }
        else {
            System.out.printf("%d tests passed and %d tests failed\n",testsPassed, testsFailed);
        }
    }

    /*
    this method compares expected value and actual value from @Test method
    it accepts Generic types for expected and actual values
    */
    public static <T,V> boolean assertion(T expectedValue, V actualValue, String methodName) {
        if (expectedValue==null || actualValue==null){
            System.err.println("FAILED test -> " + methodName + ":");
            System.err.print("no error information available!\n");
            System.err.print("expected or actual value is null, please correct your input data!\n");
            return false;
        }
        if (!Objects.equals(expectedValue,actualValue)) {
            System.err.println("FAILED test -> " + methodName + ":");
            System.err.printf("Reason of failure for %s:\n", methodName);
            System.err.printf("expected value: <%s>,\nbut was <%s>%n\n", expectedValue.toString(), actualValue.toString());
            return false;
        }
        return true;
    }
}
