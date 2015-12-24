package cn.michaelwang.himock;

import cn.michaelwang.himock.matcher.Matcher;

public interface MockProcessManager {

    void toVerifyState();

    void toNormalState();

    void toExpectState();

    void toOrderedVerifyState();

    <T> T mock(Class<T> mockedInterface);

    <T> void lastCallReturn(T returnValue, Class<?> type);

    void lastReturnTimer(int times);

    void doVerify();

    void lastCallThrow(Throwable e);

    <T> void addMatcher(Matcher<T> matcher);
}
