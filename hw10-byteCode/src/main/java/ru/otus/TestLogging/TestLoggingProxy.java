package ru.otus.TestLogging;

import ru.otus.Annotations.TestLogging;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Set;
import java.util.logging.Logger;
import static ru.otus.utils.TestLoggingUtils.*;
public class TestLoggingProxy {

    public TestLoggingProxy(){}

    public static TestLogging createTestLoggingInstance(){
        InvocationHandler handler = new TestLoggingHandler(new TestLoggingIml());
        return (TestLogging) Proxy.newProxyInstance(TestLoggingProxy.class.getClassLoader(),
                new Class<?>[]{TestLogging.class}, handler);
    }

    static class TestLoggingHandler implements InvocationHandler{

        private static final Logger log = Logger.getLogger(TestLoggingHandler.class.getName());
        private final Set<Method> annotatedMethods;
        private final TestLogging testLogging;

        public TestLoggingHandler(TestLogging testLogging){
            this.testLogging = testLogging;
            System.out.println(testLogging.getClass());
            this.annotatedMethods = annotatedMethodList();
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            String loggedInformation = loggedMethod(annotatedMethods,method,args);
            if (!loggedInformation.isEmpty()){
                log.info(loggedInformation); // logging if information is not empty
            }
            return method.invoke(testLogging,args);
        }
    }
}
