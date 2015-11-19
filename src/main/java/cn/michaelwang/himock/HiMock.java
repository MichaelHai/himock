package cn.michaelwang.himock;

import cn.michaelwang.himock.process.MockStateManager;
import cn.michaelwang.himock.report.MockProcessErrorReporter;
import cn.michaelwang.himock.report.VerificationFailedException;
import cn.michaelwang.himock.report.VerificationFailedReporter;

import java.util.List;

public class HiMock {
    private MockStateManager mockProcessManager = new MockStateManager();
    private MockFactory mockFactory = MockFactoryImpl.getInstance();

    public <T> T mock(Class<T> mockedInterface) {
        if (!mockedInterface.isInterface()) {
            throw new MockProcessErrorReporter(new MockNoninterfaceException(mockedInterface));
        }

        return mockFactory.createMock(mockedInterface, mockProcessManager);
    }

    public void expect(Expectation expectation) {
        mockProcessManager.toExpectState();
        expectation.expect();
        mockProcessManager.toNormalState();
    }

    public <T> void willReturn(T returnValue) {
        mockProcessManager.lastCallReturn(returnValue, returnValue.getClass());
    }

    public void willReturn(boolean returnValue) {
        mockProcessManager.lastCallReturn(returnValue, boolean.class);
    }

    @SuppressWarnings("unused") // simple function not tested
    public void willReturn(byte returnValue) {
        mockProcessManager.lastCallReturn(returnValue, byte.class);
    }

    @SuppressWarnings("unused") // simple function not tested
    public void willReturn(char returnValue) {
        mockProcessManager.lastCallReturn(returnValue, char.class);
    }

    @SuppressWarnings("unused") // simple function not tested
    public void willReturn(short returnValue) {
        mockProcessManager.lastCallReturn(returnValue, short.class);
    }

    @SuppressWarnings("unused") // simple function not tested
    public void willReturn(int returnValue) {
        mockProcessManager.lastCallReturn(returnValue, int.class);
    }

    @SuppressWarnings("unused") // simple function not tested
    public void willReturn(long returnValue) {
        mockProcessManager.lastCallReturn(returnValue, long.class);
    }

    @SuppressWarnings("unused") // simple function not tested
    public void willReturn(float returnValue) {
        mockProcessManager.lastCallReturn(returnValue, float.class);
    }

    @SuppressWarnings("unused") // simple function not tested
    public void willReturn(double returnValue) {
        mockProcessManager.lastCallReturn(returnValue, double.class);
    }

    public void verify() {
        List<VerificationFailedException> reports = mockProcessManager.doVerify();

        if (!reports.isEmpty()) {
            throw new VerificationFailedReporter(reports);
        }
    }

    public void verify(Verification verify) {
        mockProcessManager.toVerifyState();
        verify.verify();
        mockProcessManager.toNormalState();
        verify();
    }

    @FunctionalInterface
    public interface Expectation {
        void expect();
    }

    @FunctionalInterface
    public interface Verification {
        void verify();
    }
}