package cn.michaelwang.himock.verify;

import cn.michaelwang.himock.Invocation;

public class NullInvocation extends VerificationImpl {
    public NullInvocation(Invocation invocation) {
        super(invocation);
    }

    @Override
    public Object getReturnValue() {
        return nullValue();
    }
}