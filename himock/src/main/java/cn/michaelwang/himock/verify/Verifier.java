package cn.michaelwang.himock.verify;

import cn.michaelwang.himock.invocation.Invocation;

import java.util.List;

public interface Verifier {
    void addVerification(Invocation invocation);

    void verify(List<Invocation> actuallyInvocations);
}
