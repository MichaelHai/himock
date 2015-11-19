package cn.michaelwang.himock.process;

import cn.michaelwang.himock.report.VerificationFailedException;

import java.util.List;

public interface MockProcessManager {

    void toVerifyState();

    void toNormalState();

    void toExpectState();

    <T> void lastCallReturn(T returnValue, Class<?> type);

    List<VerificationFailedException> doVerify();
}
