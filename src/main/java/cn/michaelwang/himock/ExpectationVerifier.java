package cn.michaelwang.himock;

import cn.michaelwang.himock.report.ExpectedInvocationNotSatisfiedException;
import cn.michaelwang.himock.report.UnexpectedInvocationCalledException;

import java.util.ArrayList;
import java.util.List;

public class ExpectationVerifier {
    private interface MockState {
        void handle(String method);
    }

    private class NormalState implements MockState {
        private ExpectationVerifier context;

        public NormalState(ExpectationVerifier expectationVerifier) {
            this.context = expectationVerifier;
        }

        @Override
        public void handle(String method) {
            context.actuallyInvocation.add(method);
        }
    }

    private class ExpectState implements MockState {
        private ExpectationVerifier context;

        public ExpectState(ExpectationVerifier expectationVerifier) {
            this.context = expectationVerifier;
        }

        @Override
        public void handle(String method) {
            expectedInvocations.add(method);
            state = new NormalState(context);
        }
    }

    private MockState state = new NormalState(this);

    private List<String> expectedInvocations = new ArrayList<>();
    private List<String> actuallyInvocation = new ArrayList<>();

    public void beginExpect() {
        this.state = new ExpectState(this);
    }

    public void methodCalled(String method) {
        state.handle(method);
    }

    public void verify() {
        actuallyInvocation.forEach((invocation) -> {
            if (expectedInvocations.contains(invocation)) {
                expectedInvocations.remove(invocation);
            } else {
                throw new UnexpectedInvocationCalledException(actuallyInvocation);
            }
        });
        if (!expectedInvocations.isEmpty()) {
            throw new ExpectedInvocationNotSatisfiedException(expectedInvocations);
        }
    }
}