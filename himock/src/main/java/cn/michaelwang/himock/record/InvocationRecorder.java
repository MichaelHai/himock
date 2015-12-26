package cn.michaelwang.himock.record;

import cn.michaelwang.himock.Invocation;
import cn.michaelwang.himock.verify.NullVerification;
import cn.michaelwang.himock.Verification;
import cn.michaelwang.himock.verify.VerificationImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InvocationRecorder {
    private List<Verification> expectedInvocations = new ArrayList<>();
    private List<Invocation> actuallyInvocations = new ArrayList<>();

    public Object actuallyCall(Invocation invocation) throws Throwable {
        actuallyInvocations.add(invocation);
        return expectedInvocations.stream()
                .filter(verification -> verification.satisfyWith(invocation))
                .findFirst().orElse(new NullVerification(invocation))
                .getReturnValue();
    }

    public Verification expect(Invocation invocation) {
        Optional<Verification> exist = expectedInvocations.stream().filter(verification -> verification.satisfyWith(invocation)).findFirst();

        if (exist.isPresent()) {
            return exist.get();
        } else {
            Verification verification = new VerificationImpl(invocation);
            expectedInvocations.add(verification);
            return verification;
        }
    }

    public List<Invocation> getActuallyInvocations() {
        return actuallyInvocations;
    }
}