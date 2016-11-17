package cn.michaelwang.himock;

import cn.michaelwang.himock.process.Timer;

public interface MockProcessManager {
    void toNormalState();

    void toExpectState();

    void toWeakExpectState();

    void toVerifyState();

    void toOrderedVerifyState();

    <T> T mock(Class<T> mockedType);

    <T> T mock(Class<T> mockedType, Object[] constructorParameters);

    <T> void lastCallReturn(T returnValue, Class<?> type);

    void lastCallThrow(Throwable e);

    void lastCallAnswer(Answer answer);

    void lastReturnTimer(Timer timer);

    void doVerify();

    <T> void addMatcher(Matcher<T> matcher);
}
