package cn.michaelwang.himock.invocation;

public interface InvocationListener {
    Object methodCalled(Invocation invocation);
}
