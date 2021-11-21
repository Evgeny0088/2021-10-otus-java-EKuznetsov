package ru.otus.utils;

import ru.otus.Annotations.Log;
import ru.otus.Annotations.TestLogging;
import ru.otus.TestLogging.TestLoggingIml;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TestLoggingUtils {

    /*
    look up through interface and implementation classes for annotated methods with @Log and append them to List of annotated methods
    */
    public static Set<Method> annotatedMethodList (){
        return Arrays.stream(TestLogging.class.getDeclaredMethods()).filter(method -> {
            try {
                return TestLoggingIml.class.getDeclaredMethod(method.getName(), method.getParameterTypes()).isAnnotationPresent(Log.class);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
                return false;
            }
        }).collect(Collectors.toSet());
    }

    /*
    check if called method is equals to one of the annotated method from the List
    if args is null, then return with no parameters' statement!
     */
    public static String loggedMethod(Set<Method> annotatedMethods, Method method, Object[] args) {
        StringBuilder logBuilder = new StringBuilder();
        String delimiter = "\n";
        if (annotatedMethods.contains(method)){
            logBuilder.append("executed method: ").append(method.getName()).append(delimiter);
            if (method.getParameterTypes().length==0){
                logBuilder.append("method without parameters!").append(delimiter); // if method without args return that string:
            }else {
                String parameter = args.length > 1 ? "parameters" : "parameter";
                logBuilder.append(String.format("with %d %s:", args.length, parameter)).append(delimiter);
                logBuilder.append(
                        IntStream.range(0, args.length).mapToObj(i -> String.format("param ==> type -> %s :: value -> %s",
                                        method.getParameterTypes()[i].getName(), args[i]))
                                .collect(Collectors.joining(delimiter))
                ).append(delimiter);
            }
        }
        return logBuilder.toString();
    }
}