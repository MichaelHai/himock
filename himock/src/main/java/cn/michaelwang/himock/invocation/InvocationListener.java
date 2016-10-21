package cn.michaelwang.himock.invocation;

import cn.michaelwang.himock.Invocation;

public interface InvocationListener {
    Object methodCalled(Invocation invocation) throws Throwable;
}
