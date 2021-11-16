package ru.otus.TestLogging;

import ru.otus.Annotations.Log;
import ru.otus.Annotations.TestLogging;

public class TestLoggingIml implements TestLogging {

    public TestLoggingIml() {
    }

    @Log
    @Override
    public void calculation(int param) {
        System.out.println("method is called!");
    }

    @Override
    public void calculation(int param1, int param2) {
        System.out.println("method is called!");
    }

    @Log
    @Override
    public void calculation(String param1, String param2) {
        System.out.println("method is called!");
    }

    @Log
    @Override
    public void stringCalculation(String str){
        System.out.println("method is called!");
    }

    @Log
    @Override
    public void calculation(int param1, int param2, String param3) {
        System.out.println("method is called!");
    }

    @Log
    @Override
    public void emptyCalculation() {
        System.out.println("method is called!");
    }
}
