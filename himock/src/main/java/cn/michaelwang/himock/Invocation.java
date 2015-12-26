package cn.michaelwang.himock;

public interface Invocation {
    String getMethodName();

    Object[] getParameters();

    StackTraceElement[] getInvocationStackTrace();

    boolean sameMethod(Invocation invocation);
}
