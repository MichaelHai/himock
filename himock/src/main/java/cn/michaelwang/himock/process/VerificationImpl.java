package cn.michaelwang.himock.process;

import java.util.List;

import cn.michaelwang.himock.Invocation;
import cn.michaelwang.himock.Matcher;
import cn.michaelwang.himock.verify.Verification;

public class VerificationImpl implements Verification {
	private Invocation invocation;
	private Matchers matchers;

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
