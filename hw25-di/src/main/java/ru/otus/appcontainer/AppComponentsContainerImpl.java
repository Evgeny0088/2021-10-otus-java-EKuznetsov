package ru.otus.appcontainer;

import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainer;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;
import ru.otus.config.BeanNotFoundException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class AppComponentsContainerImpl implements AppComponentsContainer {

    private final List<Method> validAppConfigMethods = new ArrayList<>();
    private final Map<String, Method> validAppConfigMethodsByName = new HashMap<>();
    private final Map<String, Object> createdBeans = new HashMap<>();
    private Object appConfigInstance;

    public AppComponentsContainerImpl(Class<?> initialConfigClass) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        processConfig(initialConfigClass);

    }

    private void processConfig(Class<?> initialConfigClass) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        setAppContext(initialConfigClass);
        Constructor<?> constructor = initialConfigClass.getConstructor();
        appConfigInstance = constructor.newInstance();
    }

    private void setAppContext(Class<?> initialConfigClass) {
        if (!initialConfigClass.isAnnotationPresent(AppComponentsContainerConfig.class)){
            throw new IllegalArgumentException(String.format("Given class is no appConfigInstance = checkConfigClass(initialConfigClass); t config %s", initialConfigClass.getName()));}
        Arrays.stream(initialConfigClass.getDeclaredMethods()).filter(method->
                method.isAnnotationPresent(AppComponent.class)).forEach(this::collectMetaData);
    }

    @Override
    public <C> C getAppComponent(Class<C> componentClass) throws InvocationTargetException, IllegalAccessException {
        Method validMethod = componentClass.getInterfaces().length == 0 ?
                validAppConfigMethods.stream().filter(method->componentClass.getSimpleName().equals(method.getReturnType().getSimpleName()))
                        .findFirst()
                        .orElseThrow(()->new BeanNotFoundException(String.format("Not possible to create bean from provided class: <%s>, check return types in AppConfig class",
                                componentClass.getSimpleName()),new RuntimeException()))
                : validAppConfigMethods.stream().filter(method-> Arrays.stream(componentClass.getInterfaces()).anyMatch(
                        interFace->interFace.getSimpleName().equals(method.getReturnType().getSimpleName())))
                .findFirst()
                .orElseThrow(()->new BeanNotFoundException(String.format("No suitable interface found at <%s> class for bean creation",
                        componentClass.getSimpleName()),new RuntimeException()));

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

    /*
    collecting main information (annotated methods and Annotation,s name into context)
     */
    private void collectMetaData(Method method){
        AppComponent annotation = method.getAnnotation(AppComponent.class);
        String classNameInAnnotation = annotation.name();
        this.validAppConfigMethodsByName.put(classNameInAnnotation.isBlank()
                ? method.getReturnType().getSimpleName() : classNameInAnnotation,method);
        this.validAppConfigMethods.add(method);
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
            int finalPosition = position; int finalPosition1 = position;
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
