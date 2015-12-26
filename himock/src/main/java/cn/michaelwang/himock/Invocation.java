package cn.michaelwang.himock;

import java.util.List;

public interface Invocation {
    int getObjectId();
    String getMethodName();

    Object[] getParameters();

    StackTraceElement[] getInvocationStackTrace();

    Class<?> getReturnType();
    List<Class<Throwable>> getExceptionTypes();

    boolean sameMethod(Invocation invocation);

}
