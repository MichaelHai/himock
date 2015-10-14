package cn.michaelwang.himock.recorder;

import cn.michaelwang.himock.IllegalMockProcessException;
import cn.michaelwang.himock.report.ExpectedInvocationNotHappenedException;
import cn.michaelwang.himock.report.UnexpectedInvocationHappenedException;
import cn.michaelwang.himock.report.VerificationFailedException;
import cn.michaelwang.himock.report.VerificationFailedReporter;

import java.util.ArrayList;
import java.util.List;

public class InvocationRecorder {
    private MockState state = new NormalState();
    private List<InvocationRecord> expectedInvocations = new ArrayList<>();
    private List<InvocationRecord> actuallyInvocation = new ArrayList<>();

    public void expectStart() {
        this.state = new ExpectState();
    }

    public void expectEnd() {
        this.state = new NormalState();
    }

    public Object methodCalled(String method, Class<?> returnType) {
        return state.methodCalled(method, returnType);
    }

    public <T> void lastCallReturn(T returnValue) {
        state.lastCallReturn(returnValue);
    }

    public void verify() {
        List<VerificationFailedException> exceptions = new ArrayList<>();

        for (InvocationRecord invocation : actuallyInvocation) {
            if (expectedInvocations.contains(invocation)) {
                expectedInvocations.remove(invocation);
            } else {
                exceptions.add(new UnexpectedInvocationHappenedException(actuallyInvocation));
            }
        }

        if (!expectedInvocations.isEmpty()) {
            exceptions.add(new ExpectedInvocationNotHappenedException(expectedInvocations));
        }

        if (!exceptions.isEmpty()) {
            throw new VerificationFailedReporter(exceptions);
        }
    }

    private interface MockState {
        Object methodCalled(String method, Class<?> returnType);

        <T> void lastCallReturn(T returnValue);
    }

    private class NormalState implements MockState {
        @Override
        public Object methodCalled(String method, Class<?> returnType) {
            actuallyInvocation.add(new InvocationRecord(method, returnType));
            return expectedInvocations.stream()
                    .filter(invocationRecord -> invocationRecord.getInvocation().equals(method))
                    .findFirst().orElse(new InvocationRecord(null, returnType))
                    .getReturnValue();
        }

        @Override
        public <T> void lastCallReturn(T returnValue) {
            throw new IllegalMockProcessException();
        }
    }

    private class ExpectState implements MockState {
        @Override
        public Object methodCalled(String method, Class<?> returnType) {
            InvocationRecord record = new InvocationRecord(method, returnType);
            expectedInvocations.add(record);
            return record.getReturnValue();
        }

        @Override
        public <T> void lastCallReturn(T returnValue) {
            try {
                expectedInvocations.get(expectedInvocations.size() - 1).setReturnValue(returnValue);
            } catch (ReturnValueAlreadySetException | ReturnTypeIsNotSuitableException e) {
                throw new IllegalMockProcessException();
            }
        }
    }
}