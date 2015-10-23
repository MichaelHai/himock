package cn.michaelwang.himock;

import cn.michaelwang.himock.recorder.InvocationRecorder;
import cn.michaelwang.himock.report.VerificationFailedException;
import cn.michaelwang.himock.report.VerificationFailedReporter;

import java.util.List;

public class HiMock {
    private final InvocationRecorder invocationRecorder = new InvocationRecorder();

    public <T> T mock(Class<T> mockedInterface) {
        if (!mockedInterface.isInterface()) {
            throw new MockNoninterfaceException(mockedInterface);
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
        invocationRecorder.lastCallReturn(returnValue);
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