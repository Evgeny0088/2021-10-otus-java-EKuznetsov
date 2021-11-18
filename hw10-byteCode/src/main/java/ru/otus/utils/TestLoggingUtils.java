package ru.otus.utils;

import ru.otus.Annotations.Log;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TestLoggingUtils {

    /*
    look up through specified class for annotated methods with @Log and append them to List of annotated methods
    */
    public static List<AnnotatedMethod> annotatedMethodList (Class<?> SpecifiedClass){
        List<AnnotatedMethod> methodsWithLogAnnotation = new ArrayList<>();
        for (Method m : SpecifiedClass.getDeclaredMethods()) {
            if (m.isAnnotationPresent(Log.class)) {
                methodsWithLogAnnotation.add(new AnnotatedMethod(m.getName(), m.getParameterTypes()));
            }
        }
        return methodsWithLogAnnotation;
    }

    /*
    check if called method is equals to one of the annotated method from the List, we need to meet following criteria:
    1. method name
    2. count of arguments
    3. argument types should be matched
    if args is null, then return with no parameters' statement!
     */
    public static String loggedMethod(List<AnnotatedMethod> annotatedMethods, Method method, Object[] args) {
        StringBuilder logBuilder = new StringBuilder();
        String delimiter = "\n";
        for (AnnotatedMethod am: annotatedMethods){
            if (am.getName().equals(method.getName()) && am.getParameterTypes().length == method.getParameterTypes().length) {
                if (parametersTypeCheck(am, method)){
                    logBuilder.append("executed method: ").append(method.getName()).append(delimiter);
                    if (method.getParameterTypes().length==0){
                        logBuilder.append("method without parameters!").append(delimiter); // if method without args return that string:
                        break;
                    }
                    String parameter = args.length > 1 ? "parameters" : "parameter";
                    logBuilder.append(String.format("with %d %s:", args.length, parameter)).append(delimiter);
                    logBuilder.append(
                            IntStream.range(0, args.length).mapToObj(i -> String.format("param ==> type -> %s :: value -> %s",
                                            am.getParameterTypes()[i].getName(), args[i]))
                                    .collect(Collectors.joining(delimiter))
                    ).append(delimiter);
                    break;
                }
            }
        }
        return logBuilder.toString();
    }

    /*
    checking arg types between annotated and called methods
     */
    private static boolean parametersTypeCheck(AnnotatedMethod annotatedMethod, Method calledMethod){
        return IntStream.range(0, annotatedMethod.getParameterTypes().length)
                .allMatch(i -> annotatedMethod.getParameterTypes()[i].getName().equals(calledMethod.getParameterTypes()[i].getName()));
    }

}