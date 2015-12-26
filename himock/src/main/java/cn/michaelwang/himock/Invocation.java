package cn.michaelwang.himock;

import java.util.List;

public interface Invocation {
    int getObjectId();
    String getMethodName();
    Class<?>[] getParameterTypes();
    Object[] getArguments();
    Class<?> getReturnType();
    List<Class<Throwable>> getExceptionTypes();

    StackTraceElement[] getInvocationStackTrace();

    boolean sameMethod(Invocation invocation);
}
