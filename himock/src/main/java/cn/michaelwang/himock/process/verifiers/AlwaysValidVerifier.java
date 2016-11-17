package cn.michaelwang.himock.process.verifiers;

import cn.michaelwang.himock.Invocation;
import cn.michaelwang.himock.process.Verification;
import cn.michaelwang.himock.process.Verifier;
import cn.michaelwang.himock.process.Timer;

import java.util.List;

/**
 * This verifier is used to ignore all the given verification.
 * It is used in the places like weakExpect so that the invocation in the expect state will be ignored.
 */
public class AlwaysValidVerifier implements Verifier {
    @Override
    public void addVerification(Verification verification) {
        // do nothing
    }

    @Override
    public void verify(List<Invocation> actuallyInvocations) {
        // do nothing
    }

    @Override
    public void addVerificationTimes(Timer timer) {
        // do nothing
    }
}
