package cn.michaelwang.himock.process;

import cn.michaelwang.himock.Invocation;

public class NullExpectation extends ExpectationImpl {
    public NullExpectation(Invocation invocation) {
        super(invocation);
    }

    @Override
    public Object getReturnValue() {
        return nullValue();
    }
}