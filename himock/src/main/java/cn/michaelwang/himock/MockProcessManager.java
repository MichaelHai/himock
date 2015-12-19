package cn.michaelwang.himock;

public interface MockProcessManager {

    void toVerifyState();

    void toNormalState();

    void toExpectState();

    <T> T mock(Class<T> mockedInterface);

    <T> void lastCallReturn(T returnValue, Class<?> type);

    void lastReturnTimer(int times);

    void doVerify();

    void lastCallThrow(Throwable e);
}
