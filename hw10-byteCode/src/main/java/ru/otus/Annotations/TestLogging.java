package ru.otus.Annotations;

public interface TestLogging {
    void calculation(int param) throws NoSuchMethodException;
    void calculation(int param1, int param2);
    void calculation(String param1, String param2);
    void calculation(int param1, int param2, String param3);
    void stringCalculation(String str);
    void emptyCalculation();
}
