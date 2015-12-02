package cn.michaelwang.himock.process;

import cn.michaelwang.himock.MockProcessManager;
import cn.michaelwang.himock.invocation.*;
import cn.michaelwang.himock.process.reporters.*;
import cn.michaelwang.himock.record.InvocationRecorder;
import cn.michaelwang.himock.verify.Verifier;

public class MockStateManager implements MockProcessManager, InvocationListener {
    private MockState state = new NormalState();

    private MockFactory mockFactory;
    private InvocationRecorder invocationRecorder;
    private Verifier verifier;

    public MockStateManager(MockFactory mockFactory, InvocationRecorder invocationRecorder, Verifier verifier) {
        this.mockFactory = mockFactory;
        this.invocationRecorder = invocationRecorder;
        this.verifier = verifier;
    }

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
    public <T> T mock(Class<T> mockedInterface) {
        if (!mockedInterface.isInterface()) {
            throw new MockNoninterfaceReporter(mockedInterface);
        }

        return mockFactory.createMock(mockedInterface, this);
    }

    @Override
    public <T> void lastCallReturn(T returnValue, Class<?> type) {
        state.lastCallReturn(returnValue, type);
    }

    @Override
    public void lastCallThrow(Throwable e) {
        state.lastCallThrow(e);
    }

    @Override
    public void lastReturnTimer(int times) {
        state.lastReturnTimer(times);
    }

    @Override
    public void doVerify() {
        verifier.verify(invocationRecorder.getActuallyInvocations());
    }

    @Override
    public Object methodCalled(Invocation invocation) throws Throwable {
        return state.methodCalled(invocation);
    }

    private interface MockState {
        Object methodCalled(Invocation invocation) throws Throwable;

        <T> void lastCallReturn(T returnValue, Class<?> type);

        void lastReturnTimer(int times);

        void lastCallThrow(Throwable e);
    }

    private class NormalState implements MockState {
        @Override
        public Object methodCalled(Invocation invocation) throws Throwable {
            return invocationRecorder.actuallyCall(invocation);
        }

        @Override
        public <T> void lastCallReturn(T returnValue, Class<?> type) {
            throw new ExpectReturnOutsideExpectReporter();
        }

        @Override
        public void lastCallThrow(Throwable e) {
            throw new ExpectThrowOutsideExpectReporter();
        }

        @Override
        public void lastReturnTimer(int times) {
            throw new TimerOutsideExpectReporter();
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
            if (lastCall == null) {
                throw new ExpectReturnBeforeInvocationReporter();
            }

            try {
                lastCall.addReturnValue(returnValue, type);
            } catch (NoReturnTypeException e) {
                throw new NoReturnTypeReporter(e.getInvocation());
            } catch (ReturnTypeIsNotSuitableException e) {
                throw new ReturnTypeIsNotSuitableReporter(e.getInvocation(), e.getToSetType());
            }
        }

        @Override
        public void lastCallThrow(Throwable toThrow) {
            try {
                lastCall.addException(toThrow);
            } catch (ExceptionTypeIsNotSuitableException e) {
                throw new ExceptionTypeIsNotSuitableReporter(e.getInvocation(), e.getToThrow());
            }
        }

        @Override
        public void lastReturnTimer(int times) {
            lastCall.answerMore(times - 1);
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
            throw new ExpectReturnOutsideExpectReporter();
        }

        @Override
        public void lastCallThrow(Throwable e) {
            throw new ExpectThrowOutsideExpectReporter();
        }

        @Override
        public void lastReturnTimer(int times) {
            throw new TimerOutsideExpectReporter();
        }
    }
}
