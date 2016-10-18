package cn.michaelwang.himock.process;

import cn.michaelwang.himock.Invocation;

import java.util.Collections;

public class NullExpectation extends ExpectationImpl {
    public NullExpectation(Invocation invocation) {
        super(invocation, Collections.emptyList());
    }

    @Override
    public Object getReturnValue() {
        return nullValue();
    }
}