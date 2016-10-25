package cn.michaelwang.himock.process;

import java.util.ArrayList;
import java.util.List;

import cn.michaelwang.himock.Invocation;
import cn.michaelwang.himock.Matcher;
import cn.michaelwang.himock.process.exceptions.NoExpectedInvocationException;

public class InvocationRecorder {
	private List<Expectation> expectedInvocations = new ArrayList<>();
	private List<Invocation> actuallyInvocations = new ArrayList<>();

	public Object actuallyCall(Invocation invocation) throws Throwable {
		actuallyInvocations.add(invocation);
		return expectedInvocations.stream()
				.filter(expectation -> expectation.match(invocation))
				.findFirst()
				.orElseThrow(() -> new NoExpectedInvocationException())
				.getReturnValue();
	}

	public Expectation expect(Invocation invocation, List<Matcher<?>> matchers) {
		return expectedInvocations
				.stream()
				.filter(expectation -> expectation.equals(invocation, matchers))
				.findFirst()
				.orElseGet(() -> {
					Expectation expectation = new ExpectationImpl(invocation, matchers);
					expectedInvocations.add(expectation);
					return expectation;
				});
	}

	public List<Invocation> getActuallyInvocations() {
		return actuallyInvocations;
	}
}