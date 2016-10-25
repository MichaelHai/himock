package cn.michaelwang.himock.process;

import java.util.Collections;

import cn.michaelwang.himock.Invocation;

public class NullExpectation extends ExpectationImpl {
    public NullExpectation(Invocation invocation) {
        super(invocation, Collections.emptyList());
    }

    @Override
    public Object getReturnValue() {
        return nullValue();
    }
}