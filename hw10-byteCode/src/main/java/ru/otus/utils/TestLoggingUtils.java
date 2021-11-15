package ru.otus.utils;

import ru.otus.Annotations.Log;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TestLoggingUtils {

    /*
    look up through specified class for annotated methods with @Log and append them to List of annotated methods
    */
    public static List<AnnotatedMethod> annotatedMethodList (Class<?> SpecifiedClass){
        List<AnnotatedMethod> methodsWithLogAnnotation = new ArrayList<>();
        for (Method m : SpecifiedClass.getDeclaredMethods()) {
            if (m.isAnnotationPresent(Log.class)) {
                methodsWithLogAnnotation.add(new AnnotatedMethod(m.getName(), m.getParameters().length));
            }
        }
        return methodsWithLogAnnotation;
    }

    /*
    check if called method is equals to one of the annotated method from the List, we need to meet following criteria:
    1. method name
    2. count of arguments
     */
    public static String loggedMethod(List<AnnotatedMethod> annotatedMethods, Method method, Object[] args) {
        StringBuilder logBuilder = new StringBuilder();
        String delimiter = "\n";
        for (AnnotatedMethod am: annotatedMethods){
            if (am.getName().equals(method.getName()) && am.getArgsCount() == method.getParameters().length) {
                logBuilder.append("executed method: ").append(method.getName()).append(delimiter);
                if (method.getParameters().length == 0) { // if method without args return that string:
                    logBuilder.append("method without parameters!").append(delimiter);
                } else {
                    String parameter = args.length > 1 ? "parameters" : "parameter";
                    logBuilder.append(String.format("with %d %s:", args.length, parameter)).append(delimiter);
                    logBuilder.append(
                            Arrays.stream(args).map(param -> String.format("param ==> type -> %s :: value -> %s",
                                            param.getClass().getName(), param))
                                    .collect(Collectors.joining(delimiter))
                    ).append(delimiter);
                }
                break;
            }
        }
        return logBuilder.toString();
    }
}