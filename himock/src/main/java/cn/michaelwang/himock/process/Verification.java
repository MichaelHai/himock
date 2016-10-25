package cn.michaelwang.himock.process;

import cn.michaelwang.himock.Invocation;

public interface Verification {
    Invocation getVerifiedInvocation();

    boolean satisfyWith(Invocation invocation);
}