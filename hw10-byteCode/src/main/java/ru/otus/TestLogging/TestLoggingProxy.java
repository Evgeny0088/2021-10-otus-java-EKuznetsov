package ru.otus.TestLogging;

import ru.otus.Annotations.TestLogging;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.logging.Logger;
import java.util.stream.Collectors;
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

        private final TestLogging testLogging;

        public TestLoggingHandler(TestLogging testLogging){
            this.testLogging = testLogging;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (IsLoggedAnnotatedMethod(testLogging.getClass(),method)){ // if true we are logging auxiliary information about method arguments
                StringBuilder logBuilder = new StringBuilder();
                String delimiter = "\n";
                logBuilder.append("executed method: ").append(method.getName()).append(delimiter);
                if (method.getParameters().length == 0){ // if method without args
                    logBuilder.append("method without parameters!").append(delimiter);
                }else { // collecting argument,s information (type and value) for logger
                    String parameter = args.length>1 ? "parameters" : "parameter";
                    logBuilder.append(String.format("with %d %s:", args.length, parameter)).append(delimiter);
                    logBuilder.append(
                            Arrays.stream(args).map(param-> String.format("param ==> type -> %s :: value -> %s",
                            param.getClass().getName(), param))
                                    .collect(Collectors.joining(delimiter))
                    ).append(delimiter);
                }
                log.info(logBuilder.toString()); // log what we,ve collected
            }
            return method.invoke(testLogging,args);
        }
    }
}
