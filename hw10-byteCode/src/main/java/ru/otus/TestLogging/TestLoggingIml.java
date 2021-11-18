package ru.otus.TestLogging;

import ru.otus.Annotations.Log;
import ru.otus.Annotations.TestLogging;

public class TestLoggingIml implements TestLogging {

    public TestLoggingIml() {
    }

//    @Log
    @Override
    public void calculation(int param) {
        System.out.println("method is called!");
    }

//    @Log
    @Override
    public void calculation(int param1, int param2) {
        System.out.println("method is called!");
    }

//    @Log
    @Override
    public void calculation(String param1, String param2) {
        System.out.println("method is called!");
    }

//    @Log
    @Override
    public void stringCalculation(String str){
        System.out.println("method is called!");
    }

//    @Log
    @Override
    public void calculation(Integer param1, int param2, String param3) {
        System.out.println("method is called!");
    }

//    @Log
    @Override
    public void calculation(String param1) throws NoSuchMethodException {
        System.out.println("method is called!");
    }

//    @Log
    @Override
    public void calculation(int param1, int param2, String param3) {
        System.out.println("method is called!");
    }

//    @Log
    @Override
    public void calculation(Long param1, Integer param2, Float param3) {
        System.out.println("method is called!");
    }

//    @Log
    @Override
    public void calculation(Double param1, double param2) {
        System.out.println("method is called!");
    }

//    @Log
    @Override
    public void calculation(long param1, float param2, double param3) {
        System.out.println("method is called!");
    }

//    @Log
    @Override
    public void calculation(Boolean param1) {
        System.out.println("method is called!");
    }

//    @Log
    @Override
    public void calculation(boolean param1) {
        System.out.println("method is called!");
    }

//    @Log
    @Override
    public void calculation(Integer param) throws NoSuchMethodException {

    }

//    @Log
    @Override
    public void calculation(Character param) {

    }

//    @Log
    @Override
    public void calculation(char param) {

    }

//    @Log
    @Override
    public void calculation(Byte param1, byte param2) {

    }

//    @Log
    @Override
    public void emptyCalculation() {
        System.out.println("method is called!");
    }
}
