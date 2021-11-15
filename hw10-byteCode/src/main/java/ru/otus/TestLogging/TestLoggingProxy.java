package ru.otus.TestLogging;

import ru.otus.Annotations.TestLogging;
import ru.otus.utils.AnnotatedMethod;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;
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
        private final List<AnnotatedMethod> annotatedMethods;
        private final TestLogging testLogging;

        public TestLoggingHandler(TestLogging testLogging){
            this.testLogging = testLogging;
            this.annotatedMethods = annotatedMethodList(testLogging.getClass());
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
