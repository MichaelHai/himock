package cn.michaelwang.himock;

import java.util.List;

public interface Verification {
    Invocation getVerifiedInvocation();

    void addReturnValue(Object returnValue, Class<?> type);
    void addException(Throwable toThrow);
    void answerMore(int i);
    Object getReturnValue() throws Throwable;

    boolean satisfyWith(Invocation invocation);
    boolean satisfyWith(List<Invocation> toBeVerified);
}
