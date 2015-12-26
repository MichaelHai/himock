package cn.michaelwang.himock.verify;

import cn.michaelwang.himock.Invocation;

public class NullVerification extends VerificationImpl {
    public NullVerification(Invocation invocation) {
        super(invocation);
    }

    @Override
    public Object getReturnValue() {
        return nullValue();
    }
}