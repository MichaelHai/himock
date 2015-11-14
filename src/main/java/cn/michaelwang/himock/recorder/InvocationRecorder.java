package cn.michaelwang.himock.recorder;

import cn.michaelwang.himock.report.MockProcessErrorException;
import cn.michaelwang.himock.report.MockProcessErrorReporter;
import cn.michaelwang.himock.report.VerificationFailedException;

import java.util.*;
import java.util.stream.Collectors;

public class InvocationRecorder {
    private MockState state = new NormalState();
    private List<InvocationRecord> expectedInvocations = new ArrayList<>();
    private List<InvocationRecord> actuallyInvocations = new ArrayList<>();
    private List<InvocationRecord> verificationInvocations = new ArrayList<>();

    public void expectStart() {
        this.state = new ExpectState();
    }

    public void expectEnd() {
        this.state = new NormalState();
    }

    public void verificationStart() {
        this.state = new VerificationState();
    }

    public void verificationEnd() {
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

        expectedInvocations.stream()
                .filter(invocation -> !verificationInvocations.contains(invocation))
                .forEach(verificationInvocations::add);

        List<InvocationRecord> notCalled = verificationInvocations.stream()
                .filter(invocationRecord ->
                        !actuallyInvocations.contains(invocationRecord)
                                || !invocationRecord.isAllReturned())
                .collect(Collectors.toList());

        Iterator<InvocationRecord> iter = notCalled.iterator();

        while (iter.hasNext()) {
            InvocationRecord invocationRecord = iter.next();
            Optional<InvocationRecord> parameterDiff = actuallyInvocations.stream()
                    .filter(actualInvocation -> !actualInvocation.equals(invocationRecord))
                    .filter(actualInvocation -> actualInvocation.getId() == invocationRecord.getId())
                    .filter(actualInvocation -> actualInvocation.getMethodName().equals(invocationRecord.getMethodName()))
                    .findFirst();

            if (parameterDiff.isPresent()) {
                exceptions.add(new ParametersNotMatchException(parameterDiff.get(), invocationRecord));
                iter.remove();
            }
        }

        if (!notCalled.isEmpty()) {
            exceptions.add(new ExpectedInvocationNotHappenedException(notCalled));
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
        private InvocationRecord lastCall;

        @Override
        public Object methodCalled(int id, String method, Class<?> returnType, Class<?>[] parameterTypes, Object[] args) {
            InvocationRecord record = new InvocationRecord(id, method, returnType, args);
            Optional<InvocationRecord> exist = expectedInvocations.stream().filter(record::equals).findFirst();

            if (exist.isPresent()) {
                lastCall = exist.get();
            } else {
                expectedInvocations.add(record);
                lastCall = record;
            }

            return record.getReturnValue();
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
        public Object methodCalled(int id, String method, Class<?> returnType, Class<?>[] parameterTypes, Object[] args) {
            InvocationRecord record = new InvocationRecord(id, method, returnType, args);
            verificationInvocations.add(record);
            return record.getReturnValue();
        }

        @Override
        public <T> void lastCallReturn(T returnValue, Class<?> type) {
            throw new MockProcessErrorReporter(new ExpectReturnOutsideExpectException());
        }
    }
}