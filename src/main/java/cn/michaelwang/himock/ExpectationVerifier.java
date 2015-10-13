package cn.michaelwang.himock;

import cn.michaelwang.himock.report.ExpectedInvocationNotSatisfiedException;
import cn.michaelwang.himock.report.UnexpectedInvocationCalledException;
import cn.michaelwang.himock.report.VerificationFailedException;
import cn.michaelwang.himock.report.VerificationFailedReporter;

import java.util.ArrayList;
import java.util.List;

public class ExpectationVerifier {
    private interface MockState {
        void handle(String method);
    }

    private class NormalState implements MockState {
        @Override
        public void handle(String method) {
            actuallyInvocation.add(method);
        }
    }

    private class ExpectState implements MockState {

        @Override
        public void handle(String method) {
            expectedInvocations.add(method);
        }
    }

    private MockState state = new NormalState();

    private List<String> expectedInvocations = new ArrayList<>();
    private List<String> actuallyInvocation = new ArrayList<>();

    public void beginExpect() {
        this.state = new ExpectState();
    }

    public void endExpect() {
        this.state = new NormalState();
    }

    public void methodCalled(String method) {
        state.handle(method);
    }

    public void verify() {
        List<VerificationFailedException> exceptions = new ArrayList<>();

        for (String invocation: actuallyInvocation) {
            if (expectedInvocations.contains(invocation)) {
                expectedInvocations.remove(invocation);
            } else {
                exceptions.add(new UnexpectedInvocationCalledException(actuallyInvocation));
            }
        }

        if (!expectedInvocations.isEmpty()) {
            exceptions.add(new ExpectedInvocationNotSatisfiedException(expectedInvocations));
        }

        if (!exceptions.isEmpty()) {
            throw new VerificationFailedReporter(exceptions);
        }
    }
}