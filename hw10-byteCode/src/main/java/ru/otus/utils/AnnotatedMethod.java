package ru.otus.utils;

public class AnnotatedMethod {
    private final String name;
    private final int argsCount;

    public AnnotatedMethod(String name, int argsCount) {
        this.name = name;
        this.argsCount = argsCount;
    }

    public String getName() {
        return name;
    }

    public int getArgsCount() {
        return argsCount;
    }
}
