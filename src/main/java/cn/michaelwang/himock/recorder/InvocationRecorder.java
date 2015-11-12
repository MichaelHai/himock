package cn.michaelwang.himock.recorder;

import cn.michaelwang.himock.report.MockProcessErrorException;
import cn.michaelwang.himock.report.MockProcessErrorReporter;
import cn.michaelwang.himock.report.VerificationFailedException;

import java.util.*;

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

    public Object methodCalled(int id, String method, Class<?> returnType, Class<?>[] parameterTypes, Object[] args) {
        return state.methodCalled(id, method, returnType, parameterTypes, args);
    }

    public <T> void lastCallReturn(T returnValue, Class<?> type) {
        state.lastCallReturn(returnValue, type);
    }

    public List<VerificationFailedException> verify() {
        List<VerificationFailedException> exceptions = new ArrayList<>();

        Iterator<InvocationRecord> iterator = actuallyInvocations.iterator();
        while(iterator.hasNext()) {
            InvocationRecord actuallyInvocation = iterator.next();

            if (expectedInvocations.contains(actuallyInvocation)) {
                expectedInvocations.remove(actuallyInvocation);
                iterator.remove();
            } else {
                Optional<InvocationRecord> expectedInvocation = expectedInvocations.stream()
                        .filter((invocation) -> invocation.getMethodName().equals(actuallyInvocation.getMethodName()))
                        .filter((invocation) -> invocation.getId() == actuallyInvocation.getId())
                        .findFirst();

                if (expectedInvocation.isPresent()) {
                    exceptions.add(new ParametersNotMatchException(actuallyInvocation, expectedInvocation.get()));
                    expectedInvocations.remove(expectedInvocation.get());
                    iterator.remove();
                }
            }
        }

        if (!expectedInvocations.isEmpty()) {
            exceptions.add(new ExpectedInvocationNotHappenedException(expectedInvocations));
        }

        return exceptions;
    }

    private interface MockState {
        Object methodCalled(int id, String method, Class<?> returnType, Class<?>[] parameterTypes, Object[] args);

        <T> void lastCallReturn(T returnValue, Class<?> type);
    }

    private class NormalState implements MockState {
        @Override
        public Object methodCalled(int id, String method, Class<?> returnType, Class<?>[] parameterTypes, Object[] args) {
            actuallyInvocations.add(new InvocationRecord(id, method, returnType, args));
            return expectedInvocations.stream()
                    .filter(invocationRecord ->
                                    invocationRecord.getMethodName().equals(method)
                                            && Arrays.equals(invocationRecord.getParameters(), args)
                    )
                    .findFirst().orElse(new InvocationRecord(id, null, returnType, args))
                    .getReturnValue();
        }

        @Override
        public <T> void lastCallReturn(T returnValue, Class<?> type) {
            throw new MockProcessErrorReporter(new ExpectReturnOutsideExpectException());
        }
    }

    private class ExpectState implements MockState {
        @Override
        public Object methodCalled(int id, String method, Class<?> returnType, Class<?>[] parameterTypes, Object[] args) {
            InvocationRecord record = new InvocationRecord(id, method, returnType, args);
            expectedInvocations.add(record);
            return record.getReturnValue();
        }

        @Override
        public <T> void lastCallReturn(T returnValue, Class<?> type) {
            try {
                expectedInvocations.get(expectedInvocations.size() - 1).setReturnValue(returnValue, type);
            } catch (MockProcessErrorException e) {
                throw new MockProcessErrorReporter(e);
            }
        }
    }
}