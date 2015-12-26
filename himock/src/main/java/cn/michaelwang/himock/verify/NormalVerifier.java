package cn.michaelwang.himock.verify;

import cn.michaelwang.himock.Invocation;
import cn.michaelwang.himock.Verification;
import cn.michaelwang.himock.verify.failure.ExpectedInvocationNotHappenedFailure;
import cn.michaelwang.himock.verify.failure.ArgumentsNotMatchFailure;

import java.util.*;
import java.util.stream.Collectors;

public class NormalVerifier implements Verifier {
    private Set<Verification> verifications = new HashSet<>();

    @Override
    public void addVerification(Verification verification) {
        verifications.add(verification);
    }

    @Override
    public void verify(List<Invocation> toBeVerified) {
        List<Verification> notSatisfied = verifications.stream()
                .filter(verification -> !verification.satisfyWith(toBeVerified))
                .collect(Collectors.toList());

        List<VerificationFailure> failures = generateFailures(toBeVerified, notSatisfied);

        if (!failures.isEmpty()) {
            throw new VerificationFailedReporter(failures);
        }
    }

    private List<VerificationFailure> generateFailures(List<Invocation> toBeVerified, List<Verification> notSatisfied) {
        List<VerificationFailure> failures = new ArrayList<>();

        Iterator<Verification> iter = notSatisfied.iterator();
        while (iter.hasNext()) {
            Verification verification = iter.next();
            Optional<Invocation> parameterDiff = toBeVerified.stream()
                    .filter(invocation -> invocation.sameMethod(verification.getInvocation()))
                    .filter(invocation -> !verification.satisfyWith(invocation))
                    .findFirst();

            if (parameterDiff.isPresent()) {
                failures.add(new ArgumentsNotMatchFailure(parameterDiff.get(), verification.getInvocation()));
                iter.remove();
            }
        }

        if (!notSatisfied.isEmpty()) {
            failures.add(new ExpectedInvocationNotHappenedFailure(notSatisfied.stream().map(Verification::getInvocation).collect(Collectors.toList())));
        }

        return failures;
    }
}
