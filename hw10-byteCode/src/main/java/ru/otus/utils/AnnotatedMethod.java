package ru.otus.utils;

public class AnnotatedMethod {
    private final String name;
    private final Class<?>[] parameterTypes;

    public AnnotatedMethod(String name, Class<?>[] parameterTypes) {
        this.name = name;
        this.parameterTypes = parameterTypes;
    }

    public String getName() {
        return name;
    }

    public Class<?>[] getParameterTypes() {
        return parameterTypes;
    }
}
