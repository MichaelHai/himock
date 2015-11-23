package cn.michaelwang.himock;

import cn.michaelwang.himock.mockup.MockFactoryImpl;
import cn.michaelwang.himock.process.MockFactory;
import cn.michaelwang.himock.process.MockStateManager;
import cn.michaelwang.himock.record.InvocationRecorder;
import cn.michaelwang.himock.report.VerificationFailedException;
import cn.michaelwang.himock.report.VerificationFailedReporter;
import cn.michaelwang.himock.verify.Verifier;

import java.util.List;

public class HiMock {
    private MockProcessManager mockProcessManager;

    public HiMock() {
        MockFactory mockFactory = MockFactoryImpl.getInstance();
        InvocationRecorder invocationRecorder = new InvocationRecorder();
        Verifier verifier = new Verifier();
        mockProcessManager = new MockStateManager(mockFactory, invocationRecorder, verifier);
    }

    public <T> T mock(Class<T> mockedInterface) {
        return mockProcessManager.mock(mockedInterface);
    }

    public void expect(Expectation expectation) {
        mockProcessManager.toExpectState();
        expectation.expect();
        mockProcessManager.toNormalState();
    }

    public <T> HiMock willReturn(T returnValue) {
        mockProcessManager.lastCallReturn(returnValue, returnValue.getClass());
        return this;
    }

    public HiMock willReturn(boolean returnValue) {
        mockProcessManager.lastCallReturn(returnValue, boolean.class);
        return this;
    }

    @SuppressWarnings("unused") // simple function not tested
    public HiMock willReturn(byte returnValue) {
        mockProcessManager.lastCallReturn(returnValue, byte.class);
        return this;
    }

    @SuppressWarnings("unused") // simple function not tested
    public HiMock willReturn(char returnValue) {
        mockProcessManager.lastCallReturn(returnValue, char.class);
        return this;
    }

    @SuppressWarnings("unused") // simple function not tested
    public HiMock willReturn(short returnValue) {
        mockProcessManager.lastCallReturn(returnValue, short.class);
        return this;
    }

    @SuppressWarnings("unused") // simple function not tested
    public HiMock willReturn(int returnValue) {
        mockProcessManager.lastCallReturn(returnValue, int.class);
        return this;
    }

    @SuppressWarnings("unused") // simple function not tested
    public HiMock willReturn(long returnValue) {
        mockProcessManager.lastCallReturn(returnValue, long.class);
        return this;
    }

    @SuppressWarnings("unused") // simple function not tested
    public HiMock willReturn(float returnValue) {
        mockProcessManager.lastCallReturn(returnValue, float.class);
        return this;
    }

    @SuppressWarnings("unused") // simple function not tested
    public HiMock willReturn(double returnValue) {
        mockProcessManager.lastCallReturn(returnValue, double.class);
        return this;
    }

    public void times(int times) {
        mockProcessManager.lastReturnTimer(times);
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