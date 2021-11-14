package ru.otus.utils;

import ru.otus.Annotations.Log;

import java.lang.reflect.Method;

public class TestLoggingUtils {
    /*
    check if annotated method inside TestLogging class is exists
    and if count of arguments equals to called method (it is required since we have several methods with the same name)
     */
    public static boolean IsLoggedAnnotatedMethod(Class<?> testClass, Method method){
        for (Method m: testClass.getDeclaredMethods()){
            if (m.isAnnotationPresent(Log.class) && m.getParameters().length == method.getParameters().length){
                return true;
            }
        }
        return false;
    }
}
