package cn.michaelwang.himock.invocation;

public interface InvocationListener {
    Object methodCalled(InvocationImpl invocation) throws Throwable;
}
