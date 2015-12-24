package cn.michaelwang.himock.process;

import cn.michaelwang.himock.MockProcessManager;
import cn.michaelwang.himock.NullObjectPlaceHolder;
import cn.michaelwang.himock.invocation.*;
import cn.michaelwang.himock.matcher.Matcher;
import cn.michaelwang.himock.process.reporters.*;
import cn.michaelwang.himock.record.InvocationRecorder;
import cn.michaelwang.himock.verify.InOrderVerifier;
import cn.michaelwang.himock.verify.NormalVerifier;
import cn.michaelwang.himock.verify.Verifier;

import java.util.*;

public class MockStateManager implements MockProcessManager, InvocationListener {
    private MockState state = new NormalState();

    private MockFactory mockFactory;
    private InvocationRecorder invocationRecorder;
    private List<Verifier> verifiers = new ArrayList<>();
    private Verifier verifier;

    private Queue<Matcher<?>> matchers = new LinkedList<>();

    public MockStateManager(MockFactory mockFactory, InvocationRecorder invocationRecorder) {
        this.mockFactory = mockFactory;
        this.invocationRecorder = invocationRecorder;
        this.verifier = new NormalVerifier();
        verifiers.add(verifier);
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
    public void toOrderedVerifyState() {
        this.state = new VerificationInOrderState();
        verifiers.add(((VerificationInOrderState) this.state).getVerifier());
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
    public <T> void addMatcher(Matcher<T> matcher) {
        matchers.offer(matcher);
    }

    @Override
    public void lastReturnTimer(int times) {
        state.lastReturnTimer(times);
    }

    @Override
    public void doVerify() {
        verifiers.forEach((verifier) -> verifier.verify(invocationRecorder.getActuallyInvocations()));
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
            Object[] parameters = invocation.getParameters();
            for (int i = 0; i < parameters.length; i++) {
                Object param = parameters[i];
                if (param == null) {
                    parameters[i] = NullObjectPlaceHolder.getInstance();
                }
            }
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
                if (returnValue instanceof Throwable) {
                    lastCallThrow((Throwable) returnValue);
                } else {
                    throw new ReturnTypeIsNotSuitableReporter(e.getInvocation(), e.getToSetType());
                }
            }
        }

        @Override
        public void lastCallThrow(Throwable toThrow) {
            if (lastCall == null) {
                throw new ExpectExceptionBeforeInvocationReporter();
            }

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
            invocation.addArgumentMatchers(matchers);
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

    private class VerificationInOrderState extends VerificationState {
        private InOrderVerifier verifier = new InOrderVerifier();

        @Override
        public Object methodCalled(Invocation invocation) {
            this.verifier.addVerification(invocation);
            return super.methodCalled(invocation);
        }

        public InOrderVerifier getVerifier() {
            return verifier;
        }
    }
}
