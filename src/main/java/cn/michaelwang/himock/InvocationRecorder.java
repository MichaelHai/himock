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
        Object methodCalled(String method);

        <T> void lastCallReturn(T returnValue);
    }

    private class NormalState implements MockState {
        @Override
        public Object methodCalled(String method) {
            actuallyInvocation.add(method);
            return returnValues.get(method);
        }

        @Override
        public <T> void lastCallReturn(T returnValue) {
            throw new IllegalMockProcessException();
        }
    }

    private class ExpectState implements MockState {
        @Override
        public Object methodCalled(String method) {
            expectedInvocations.add(method);
            return null;
        }

        @Override
        public <T> void lastCallReturn(T returnValue) {
            String lastCall = expectedInvocations.get(expectedInvocations.size()-1);
            returnValues.put(lastCall, returnValue);
        }
    }

    private MockState state = new NormalState();

    private List<String> expectedInvocations = new ArrayList<>();
    private List<String> actuallyInvocation = new ArrayList<>();
    private Map<String, Object> returnValues = new HashMap<>();

    public void expectStart() {
        this.state = new ExpectState();
    }

    public void expectEnd() {
        this.state = new NormalState();
    }

    public Object methodCalled(String method) {
        return state.methodCalled(method);
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
}