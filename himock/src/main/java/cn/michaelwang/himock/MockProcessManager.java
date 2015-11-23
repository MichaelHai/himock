package cn.michaelwang.himock;

import cn.michaelwang.himock.report.VerificationFailedException;

import java.util.List;

public interface MockProcessManager {

    void toVerifyState();

    void toNormalState();

    void toExpectState();

    <T> T mock(Class<T> mockedInterface);

    <T> void lastCallReturn(T returnValue, Class<?> type);

    List<VerificationFailedException> doVerify();
}
