package cn.michaelwang.himock.verify;

import java.util.List;

public interface Verifier {
    void addVerification(Verification verification);

    void verify(List<? extends Verifiable> actuallyInvocations);
}
