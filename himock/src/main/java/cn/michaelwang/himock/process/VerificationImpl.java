package cn.michaelwang.himock.process;

import cn.michaelwang.himock.Invocation;
import cn.michaelwang.himock.Matcher;

import java.util.List;

public class VerificationImpl implements Verification {
    private final Invocation invocation;
    private final Matchers matchers;

	public VerificationImpl(Invocation invocation, List<Matcher<?>> matchers) {
		this.invocation = invocation;
		this.matchers = new Matchers(matchers);
	}

	@Override
	public Invocation getVerifiedInvocation() {
		return new InvocationWithMatchers(invocation, matchers);
	}

	@Override
	public boolean satisfyWith(Invocation invocation) {
		return this.invocation.sameMethod(invocation)
				&& matchers.match(invocation.getArguments());
	}
}
