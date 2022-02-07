package ru.otus.appcontainer;

import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainer;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;
import ru.otus.config.AppConfig;
import ru.otus.config.BeanNotFoundException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class AppComponentsContainerImpl implements AppComponentsContainer {

    private final List<Method> validAppConfigMethods = new ArrayList<>();
    private final Map<String, Method> validAppConfigMethodsByName = new HashMap<>();
    private final Map<String, Object> createdBeans = new HashMap<>();
    private final Object appConfigInstance = AppConfig.class.getConstructor().newInstance();

    public AppComponentsContainerImpl(Class<?> initialConfigClass) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        processConfig(initialConfigClass);
    }

    private void processConfig(Class<?> initialConfigClass){
        setAppContext(initialConfigClass);
    }

    private void setAppContext(Class<?> initialConfigClass) {
        if (!initialConfigClass.isAnnotationPresent(AppComponentsContainerConfig.class)){
            throw new IllegalArgumentException(String.format("Given class is no appConfigInstance = checkConfigClass(initialConfigClass); t config %s", initialConfigClass.getName()));}
        Arrays.stream(initialConfigClass.getDeclaredMethods()).filter(method->
                method.isAnnotationPresent(AppComponent.class)).forEach(this::collectMetaData);
    }

    @Override
    public <C> C getAppComponent(Class<C> componentClass) throws InvocationTargetException, IllegalAccessException {
        Method validMethod = getValidMethodForBeanCreation(componentClass);
        List<Object> validArgsForInvocation = validArgsForInvocation(validMethod);
        C bean = (C) validMethod.invoke(appConfigInstance,validArgsForInvocation.toArray());
        createdBeans.put(validMethod.getReturnType().getSimpleName(),bean);
        return bean;
    }

    @Override
    public <C> C getAppComponent(String componentName) throws InvocationTargetException, IllegalAccessException {
        Method validMethod = validAppConfigMethodsByName.get(componentName);
        if (validMethod != null){
            List<Object> validArgsForInvocation = validArgsForInvocation(validMethod);
            C bean = (C) validMethod.invoke(appConfigInstance,validArgsForInvocation.toArray());
            createdBeans.put(validMethod.getReturnType().getSimpleName(),bean);
            return bean;
        }else {
            throw new BeanNotFoundException(String.format("Bean with name <%s> not found!",componentName),new RuntimeException());
        }
    }

    /*
    ###############################################################################################################
    HELP METHODS BELOW
    ###############################################################################################################
     */


    private void collectMetaData(Method method){
        AppComponent annotation = method.getAnnotation(AppComponent.class);
        String classNameInAnnotation = annotation.name();
        this.validAppConfigMethodsByName.put(classNameInAnnotation.isBlank()
                ? method.getReturnType().getSimpleName() : classNameInAnnotation,method);
        this.validAppConfigMethods.add(method);
    }

    /*
    try to get method with valid returned type equal to input class or interface:
    there are two ways:
        1. if input class has interfaces then we should check them if any method in appConfig class has the same returning type
           if no method is found throw exception and bean cannot be created from input class
        2. if class is interface then go directly checking it, if failed to find method - throw new exception
    */
    private Method getValidMethodForBeanCreation(Class<?> componentClass){
        if (componentClass.getInterfaces().length == 0){
            for (Method method: validAppConfigMethods){
                if (method.getReturnType().getSimpleName().equals(componentClass.getSimpleName())){
                    return method;
                }
            }
            throw new BeanNotFoundException(String.format("Not possible to create bean from provided class: <%s>, check return types in AppConfig class",
                    componentClass.getSimpleName()),new RuntimeException());
        }else {
            for (Method method: validAppConfigMethods){
                for (Class<?> interFace: componentClass.getInterfaces()){
                    if (interFace.getSimpleName().equals(method.getReturnType().getSimpleName())){
                        return method;
                    }
                }
            }
            throw new BeanNotFoundException(String.format("No suitable interface found at <%s> class for bean creation",
                    componentClass.getSimpleName()),new RuntimeException());
        }
    }

    /*
    recursive method for collecting valid arguments before new bean
    */
    private List<Object> validArgsForInvocation(Method argumentCandidate) throws InvocationTargetException, IllegalAccessException {
        Class<?>[] args = argumentCandidate.getParameterTypes();
        List<Object> argsForPreviousMethod = new ArrayList<>();
        if (args.length == 0)return argsForPreviousMethod;
        int position = 0;
        while (position<args.length){
            int finalPosition = position;
            int finalPosition1 = position;
            Method validMethod = validAppConfigMethods.stream().filter(method->method.getReturnType().getSimpleName().equals(args[finalPosition].getSimpleName())).findFirst()
                    .orElseThrow(()->new BeanNotFoundException(String.format("Bean with name <%s> not found!", args[finalPosition1].getSimpleName()),new RuntimeException()));
            String beanInterFaceName = validMethod.getReturnType().getSimpleName();
            if (!createdBeans.containsKey(beanInterFaceName)){
                List<Object> validArgsForInvocation = validArgsForInvocation(validMethod);
                Object bean = validMethod.invoke(appConfigInstance,validArgsForInvocation.toArray());
                createdBeans.put(beanInterFaceName,bean);
                argsForPreviousMethod.add(bean);
            }
            else {argsForPreviousMethod.add(createdBeans.get(beanInterFaceName));}
            position++;
        }
        return argsForPreviousMethod;
    }
}
