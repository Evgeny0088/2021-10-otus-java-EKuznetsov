package ru.otus.appcontainer.api;

import java.lang.reflect.InvocationTargetException;

public interface AppComponentsContainer {
    <C> C getAppComponent(Class<C> componentClass) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException;
    <C>C getAppComponent(String componentName) throws InvocationTargetException, IllegalAccessException;
}
