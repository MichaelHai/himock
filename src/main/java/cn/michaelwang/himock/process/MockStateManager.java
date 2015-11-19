package cn.michaelwang.himock.process;

import cn.michaelwang.himock.invocation.Invocation;
import cn.michaelwang.himock.invocation.InvocationListener;
import cn.michaelwang.himock.invocation.NullInvocation;
import cn.michaelwang.himock.record.InvocationRecorder;
import cn.michaelwang.himock.report.MockProcessErrorException;
import cn.michaelwang.himock.report.MockProcessErrorReporter;
import cn.michaelwang.himock.report.VerificationFailedException;
import cn.michaelwang.himock.verify.Verifier;

import java.util.List;

public class MockStateManager implements MockProcessManager, InvocationListener {
    private MockState state = new NormalState();

    private InvocationRecorder invocationRecorder = new InvocationRecorder();
    private Verifier verifier = new Verifier();

    @Override
    public void toVerifyState() {
        this.state = new VerificationState();
    }

    @Override
    public void toNormalState() {
        this.state = new NormalState();
    }

    @Override
    public void toExpectState() {
        this.state = new ExpectState();
    }

    @Override
    public Object methodCalled(Invocation invocation) {
        return state.methodCalled(invocation);
    }

    @Override
    public <T> void lastCallReturn(T returnValue, Class<?> type) {
        state.lastCallReturn(returnValue, type);
    }

    @Override
    public List<VerificationFailedException> doVerify() {
        return verifier.verify(invocationRecorder.getActuallyInvocations());
    }

    private interface MockState {
        Object methodCalled(Invocation invocation);

        <T> void lastCallReturn(T returnValue, Class<?> type);
    }

    private class NormalState implements MockState {
        @Override
        public Object methodCalled(Invocation invocation) {
            return invocationRecorder.actuallyCall(invocation);
        }

        @Override
        public <T> void lastCallReturn(T returnValue, Class<?> type) {
            throw new MockProcessErrorReporter(new ExpectReturnOutsideExpectException());
        }
    }

    private class ExpectState implements MockState {
        private Invocation lastCall;

        @Override
        public Object methodCalled(Invocation invocation) {
            lastCall = invocationRecorder.expect(invocation);
            verifier.addVerification(lastCall);
            return new NullInvocation(invocation.getReturnType()).getReturnValue();
        }

        @Override
        public <T> void lastCallReturn(T returnValue, Class<?> type) {
            try {
                lastCall.addReturnValue(returnValue, type);
            } catch (MockProcessErrorException e) {
                throw new MockProcessErrorReporter(e);
            }
        }
    }

    private class VerificationState implements MockState {

        @Override
        public Object methodCalled(Invocation invocation) {
            verifier.addVerification(invocation);
            return new NullInvocation(invocation.getReturnType()).getReturnValue();
        }

        @Override
        public <T> void lastCallReturn(T returnValue, Class<?> type) {
            throw new MockProcessErrorReporter(new ExpectReturnOutsideExpectException());
        }
    }
}
