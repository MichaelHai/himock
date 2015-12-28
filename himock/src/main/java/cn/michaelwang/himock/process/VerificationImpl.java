package cn.michaelwang.himock.process;

import cn.michaelwang.himock.Invocation;
import cn.michaelwang.himock.Matcher;
import cn.michaelwang.himock.verify.Verification;

import java.util.List;

public class VerificationImpl implements Verification {
    private Invocation invocation;
    private Matchers matchers;

    public VerificationImpl(Invocation invocation, List<Matcher<?>> matchers) {
        this.invocation = invocation;
        this.matchers = new Matchers(matchers, invocation.getArguments());
    }

    @Override
    public Invocation getVerifiedInvocation() {
        return invocation;
    }

    @Override
    public boolean satisfyWith(Invocation invocation) {
        return this.invocation.sameMethod(invocation)
                && matchers.match(invocation.getArguments());
    }
}
