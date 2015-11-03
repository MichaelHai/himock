package cn.michaelwang.himock;

import cn.michaelwang.himock.recorder.InvocationRecorder;
import cn.michaelwang.himock.report.MockProcessErrorReporter;
import cn.michaelwang.himock.report.VerificationFailedException;
import cn.michaelwang.himock.report.VerificationFailedReporter;

import java.util.List;

public class HiMock {
    private final InvocationRecorder invocationRecorder = new InvocationRecorder();

    public <T> T mock(Class<T> mockedInterface) {
        if (!mockedInterface.isInterface()) {
            throw new MockProcessErrorReporter(new MockNoninterfaceException(mockedInterface));
        }

        return MockFactory.getInstance().createMock(mockedInterface, invocationRecorder);
    }

    public void expectStart() {
        invocationRecorder.expectStart();
    }

    public void expectEnd() {
        invocationRecorder.expectEnd();
    }

    public void expect(Expectation expectation) {
        expectStart();
        expectation.expect();
        expectEnd();
    }

    public <T> void willReturn(T returnValue) {
        invocationRecorder.lastCallReturn(returnValue, returnValue.getClass());
    }

    public void willReturn(boolean returnValue) {
        invocationRecorder.lastCallReturn(returnValue, boolean.class);
    }

    @SuppressWarnings("unused") // simple function not tested
    public void willReturn(byte returnValue) {
        invocationRecorder.lastCallReturn(returnValue, byte.class);
    }

    @SuppressWarnings("unused") // simple function not tested
    public void willReturn(char returnValue) {
        invocationRecorder.lastCallReturn(returnValue, char.class);
    }

    @SuppressWarnings("unused") // simple function not tested
    public void willReturn(short returnValue) {
        invocationRecorder.lastCallReturn(returnValue, short.class);
    }

    @SuppressWarnings("unused") // simple function not tested
    public void willReturn(int returnValue) {
        invocationRecorder.lastCallReturn(returnValue, int.class);
    }

    @SuppressWarnings("unused") // simple function not tested
    public void willReturn(long returnValue) {
        invocationRecorder.lastCallReturn(returnValue, long.class);
    }

    @SuppressWarnings("unused") // simple function not tested
    public void willReturn(float returnValue) {
        invocationRecorder.lastCallReturn(returnValue, float.class);
    }

    @SuppressWarnings("unused") // simple function not tested
    public void willReturn(double returnValue) {
        invocationRecorder.lastCallReturn(returnValue, double.class);
    }

    public void verify() {
        List<VerificationFailedException> reports = invocationRecorder.verify();

        if (!reports.isEmpty()) {
            throw new VerificationFailedReporter(reports);
        }
    }

    @FunctionalInterface
    public interface Expectation {
        void expect();
    }
}