package cn.michaelwang.himock;

import cn.michaelwang.himock.report.ExpectedInvocationNotHappenedException;
import cn.michaelwang.himock.report.UnexpectedInvocationHappenedException;
import cn.michaelwang.himock.report.VerificationFailedException;
import cn.michaelwang.himock.report.VerificationFailedReporter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InvocationRecorder {
    private interface MockState {
        Object methodCalled(String method, Class<?> returnType);

        <T> void lastCallReturn(T returnValue);
    }

    private class NormalState implements MockState {
        @Override
        public Object methodCalled(String method, Class<?> returnType) {
            actuallyInvocation.add(method);
            Object returnValue = returnValues.get(method);
            if (returnValue == null) {
                return nullValue(returnType);
            } else {
                return returnValue;
            }
        }

        @Override
        public <T> void lastCallReturn(T returnValue) {
            throw new IllegalMockProcessException();
        }
    }

    private class ExpectState implements MockState {
        @Override
        public Object methodCalled(String method, Class<?> returnType) {
            expectedInvocations.add(method);
            returnTypes.put(method, returnType);
            return nullValue(returnType);
        }

        @Override
        public <T> void lastCallReturn(T returnValue) {
            String lastCall = expectedInvocations.get(expectedInvocations.size()-1);
            Class<?> returnType = returnTypes.get(lastCall);
            if (isSuitableType(returnValue.getClass(), returnType)) {
                returnValues.put(lastCall, returnValue);
            } else {
                throw new IllegalMockProcessException();
            }
        }
    }

    private MockState state = new NormalState();

    private List<String> expectedInvocations = new ArrayList<>();
    private List<String> actuallyInvocation = new ArrayList<>();
    private Map<String, Object> returnValues = new HashMap<>();
    private Map<String, Class<?>> returnTypes = new HashMap<>();

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

        for (String invocation: actuallyInvocation) {
            if (expectedInvocations.contains(invocation)) {
                expectedInvocations.remove(invocation);
                returnValues.remove(invocation);
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

    private Object nullValue(Class<?> returnType) {
        if (returnType.isPrimitive()) {
            if (returnType.getName().equals("boolean")) {
                return false;
            }
            return 0;
        }

        return null;
    }

    private boolean isSuitableType(Class<?> thisType, Class<?> targetType) {
        if (thisType.isInstance(targetType)) {
            return true;
        } else if (targetType.isPrimitive()) {
            String thisTypeName = thisType.getName();
            String targetTypeName = targetType.getName();

            if (thisTypeName.startsWith("java.lang.")) {
                thisTypeName = thisTypeName.substring("java.lang.".length());
                if (thisTypeName.toLowerCase().equals(targetTypeName)
                        || (thisTypeName.equals("Integer") && targetTypeName.equals("int"))) {
                    return true;
                }
            }
        }

        return false;
    }
}