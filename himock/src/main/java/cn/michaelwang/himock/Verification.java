package cn.michaelwang.himock;

import java.util.List;

public interface Verification {
    boolean satisfyWith(Invocation invocation);

    boolean satisfyWith(List<Invocation> toBeVerified);

    Object getReturnValue() throws Throwable;

    void addReturnValue(Object returnValue, Class<?> type);

    void addException(Throwable toThrow);

    void answerMore(int i);

    Invocation getInvocation();
}
