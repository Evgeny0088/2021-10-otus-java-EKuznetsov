package ru.otus.Annotations;

public interface TestLogging {
    void calculation(int param) throws NoSuchMethodException;
    void calculation(Integer param) throws NoSuchMethodException;
    void calculation(String param1) throws NoSuchMethodException;
    void calculation(Boolean param1);
    void calculation(boolean param1);
    void calculation(Character param);
    void calculation(char param);
    void calculation(Byte param1, byte param2);
    void calculation(int param1, int param2);
    void calculation(String param1, String param2);
    void calculation(int param1, int param2, String param3);
    void calculation(Integer param1, int param2, String param3);
    void calculation(Long param1, Integer param2, Float param3);
    void calculation(Double param1, double param2);
    void calculation(long param1, float param2, double param3);
    void stringCalculation(String str) throws NoSuchMethodException;
    void emptyCalculation();
}
