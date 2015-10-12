package cn.michaelwang.himock;

import cn.michaelwang.himock.report.ExpectedInvocationNotSatisfiedException;
import cn.michaelwang.himock.report.UnexpectedInvocationCalledException;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ExpectationVerifier {
    static final int NORMAL = 0;
    static final int EXPECT = 1;

    private List<String> expectedInvocations = new ArrayList<>();
    private List<String> actuallyInvocation = new ArrayList<>();

    private int state = NORMAL;

    public void beginExpect() {
        this.state = EXPECT;
    }

    public void methodCalled(String method) {
        switch (state) {
            case NORMAL:
                actuallyInvocation.add(method);
                break;
            case EXPECT:
                expectedInvocations.add(method);
                state = NORMAL;
                break;
        }
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