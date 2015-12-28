package cn.michaelwang.himock.verify;

import cn.michaelwang.himock.Invocation;

public interface Verification {
    Invocation getVerifiedInvocation();

    boolean satisfyWith(Invocation invocation);
}