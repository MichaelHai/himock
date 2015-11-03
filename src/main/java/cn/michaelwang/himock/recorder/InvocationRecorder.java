package cn.michaelwang.himock.recorder;

import cn.michaelwang.himock.report.MockProcessErrorException;
import cn.michaelwang.himock.report.MockProcessErrorReporter;
import cn.michaelwang.himock.report.VerificationFailedException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class InvocationRecorder {
    private MockState state = new NormalState();
    private List<InvocationRecord> expectedInvocations = new ArrayList<>();
    private List<InvocationRecord> actuallyInvocations = new ArrayList<>();

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

        actuallyInvocations.forEach((actuallyInvocation) -> {
            if (expectedInvocations.contains(actuallyInvocation)) {
                expectedInvocations.remove(actuallyInvocation);
            } else {
                Optional<InvocationRecord> expectedInvocation = expectedInvocations.stream()
                        .filter((invocation) -> invocation.getMethodName().equals(actuallyInvocation.getMethodName()))
                        .findFirst();

                if (expectedInvocation.isPresent()) {
                    exceptions.add(new ParametersNotMatchException(actuallyInvocation, expectedInvocation.get()));
                    expectedInvocations.remove(expectedInvocation.get());
                } else {
                    exceptions.add(new UnexpectedInvocationHappenedException(actuallyInvocations));
                }
            }
        });
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
            actuallyInvocations.add(new InvocationRecord(method, returnType, args));
            return expectedInvocations.stream()
                    .filter(invocationRecord ->
                                    invocationRecord.getMethodName().equals(method)
                                            && Arrays.equals(invocationRecord.getParameters(), args)
                    )
                    .findFirst().orElse(new InvocationRecord(null, returnType, args))
                    .getReturnValue();
        }

        @Override
        public <T> void lastCallReturn(T returnValue) {
            throw new MockProcessErrorReporter(new ExpectReturnOutsideExpectException());
        }
    }

    private class ExpectState implements MockState {
        @Override
        public Object methodCalled(String method, Class<?> returnType, Class<?>[] parameterTypes, Object[] args) {
            InvocationRecord record = new InvocationRecord(method, returnType, args);
            expectedInvocations.add(record);
            return record.getReturnValue();
        }

        @Override
        public <T> void lastCallReturn(T returnValue) {
            try {
                expectedInvocations.get(expectedInvocations.size() - 1).setReturnValue(returnValue);
            } catch (MockProcessErrorException e) {
                throw new MockProcessErrorReporter(e);
            }
        }
    }
}