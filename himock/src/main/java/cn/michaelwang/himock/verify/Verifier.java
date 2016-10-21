package cn.michaelwang.himock.verify;

import cn.michaelwang.himock.Invocation;

import java.util.List;

public interface Verifier {
    void addVerification(Verification verification);

    void verify(List<Invocation> actuallyInvocations);
}
