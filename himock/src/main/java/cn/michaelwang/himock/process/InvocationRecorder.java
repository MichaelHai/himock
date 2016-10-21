package cn.michaelwang.himock.process;

import cn.michaelwang.himock.Invocation;
import cn.michaelwang.himock.Matcher;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InvocationRecorder {
    private List<Expectation> expectedInvocations = new ArrayList<>();
    private List<Invocation> actuallyInvocations = new ArrayList<>();

    public Object actuallyCall(Invocation invocation) throws Throwable {
        actuallyInvocations.add(invocation);
        return expectedInvocations.stream()
                .filter(expectation -> expectation.match(invocation))
                .findFirst()
                .orElse(new NullExpectation(invocation))
                .getReturnValue();
    }

    public Expectation expect(Invocation invocation, List<Matcher<?>> matchers) {
        Optional<Expectation> exist = expectedInvocations.stream()
                .filter(expectation -> expectation.equals(invocation, matchers))
                .findFirst();

        if (exist.isPresent()) {
            return exist.get();
        } else {
            Expectation expectation = new ExpectationImpl(invocation, matchers);
            expectedInvocations.add(expectation);
            return expectation;
        }
    }

    public List<Invocation> getActuallyInvocations() {
        return actuallyInvocations;
    }
}