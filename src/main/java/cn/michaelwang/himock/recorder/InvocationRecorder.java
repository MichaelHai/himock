package cn.michaelwang.himock.recorder;

import cn.michaelwang.himock.report.VerificationFailedException;

import java.util.ArrayList;
import java.util.Arrays;
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

    public Object methodCalled(String method, Class<?> returnType, Class<?>[] parameterTypes, Object[] args) {
        return state.methodCalled(method, returnType, parameterTypes, args);
    }

    public <T> void lastCallReturn(T returnValue) {
        state.lastCallReturn(returnValue);
    }

    public List<VerificationFailedException> verify() {
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

        return exceptions;
    }

    private interface MockState {
        Object methodCalled(String method, Class<?> returnType, Class<?>[] parameterTypes, Object[] args);

        <T> void lastCallReturn(T returnValue);
    }

    private class NormalState implements MockState {
        @Override
        public Object methodCalled(String method, Class<?> returnType, Class<?>[] parameterTypes, Object[] args) {
            actuallyInvocation.add(new InvocationRecord(method, returnType, parameterTypes, args));
            return expectedInvocations.stream()
                    .filter(invocationRecord ->
                                    invocationRecord.getInvocation().equals(method)
                                            && Arrays.equals(invocationRecord.getParameters(), args)
                    )
                    .findFirst().orElse(new InvocationRecord(null, returnType, parameterTypes, args))
                    .getReturnValue();
        }

        @Override
        public <T> void lastCallReturn(T returnValue) {
            throw new IllegalMockProcessException();
        }
    }

    private class ExpectState implements MockState {
        @Override
        public Object methodCalled(String method, Class<?> returnType, Class<?>[] parameterTypes, Object[] args) {
            InvocationRecord record = new InvocationRecord(method, returnType, parameterTypes, args);
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