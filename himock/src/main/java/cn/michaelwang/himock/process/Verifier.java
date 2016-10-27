package cn.michaelwang.himock.process;

import java.util.List;

import cn.michaelwang.himock.Invocation;

public interface Verifier {
    void addVerification(Verification verification);

    void verify(List<Invocation> actuallyInvocations);

	void addVerificationTimes(int times);
}
