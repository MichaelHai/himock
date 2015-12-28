package cn.michaelwang.himock.process;

import cn.michaelwang.himock.Invocation;
import cn.michaelwang.himock.verify.Verification;

public interface Expectation {
    boolean match(Invocation invocation);

    void addReturnValue(Object returnValue, Class<?> type);

    void addException(Throwable toThrow);

    void answerMore(int i);

    Object getReturnValue() throws Throwable;

    Verification generateVerification();
}
