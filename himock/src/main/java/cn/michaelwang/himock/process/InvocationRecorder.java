package cn.michaelwang.himock.process;

import cn.michaelwang.himock.Invocation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InvocationRecorder {
    private List<Expectation> expectedInvocations = new ArrayList<>();
    private List<Invocation> actuallyInvocations = new ArrayList<>();

    public Object actuallyCall(Invocation invocation) throws Throwable {
        actuallyInvocations.add(invocation);
        return findExistExpectation(invocation)
                .orElse(new NullExpectation(invocation))
                .getReturnValue();
    }

    public Expectation expect(Invocation invocation) {
        Optional<Expectation> exist = findExistExpectation(invocation);

        if (exist.isPresent()) {
            return exist.get();
        } else {
            Expectation expectation = new ExpectationImpl(invocation);
            expectedInvocations.add(expectation);
            return expectation;
        }
    }

    private Optional<Expectation> findExistExpectation(Invocation invocation) {
        return expectedInvocations.stream()
                .filter(expectation -> expectation.match(invocation))
                .findFirst();
    }

    public List<Invocation> getActuallyInvocations() {
        return actuallyInvocations;
    }
}