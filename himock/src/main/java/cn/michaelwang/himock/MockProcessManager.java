package cn.michaelwang.himock;

public interface MockProcessManager {
    void toNormalState();

    void toExpectState();

    void toVerifyState();

    void toOrderedVerifyState();

    <T> T mock(Class<T> mockedType);

    <T> T mock(Class<T> mockedType, Object[] constructorParameters);

    <T> void lastCallReturn(T returnValue, Class<?> type);

    void lastCallThrow(Throwable e);

    void lastReturnTimer(int times);

    void doVerify();

    <T> void addMatcher(Matcher<T> matcher);
}
