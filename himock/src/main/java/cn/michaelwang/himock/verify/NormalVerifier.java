package cn.michaelwang.himock.verify;

import cn.michaelwang.himock.Invocation;
import cn.michaelwang.himock.verify.failure.ExpectedInvocationNotHappenedFailure;
import cn.michaelwang.himock.verify.failure.ParametersNotMatchFailure;

import java.util.*;
import java.util.stream.Collectors;

public class NormalVerifier implements Verifier {
    private Set<Verification> verifications = new HashSet<>();

    @Override
    public void addVerification(Verification verification) {
        verifications.add(verification);
    }

    @Override
    public void verify(List<? extends Verifiable> toBeVerified) {
        List<Verification> notSatisfied = verifications.stream()
                .filter(verification -> !verification.satisfyWith(toBeVerified))
                .collect(Collectors.toList());

        List<VerificationFailure> failures = checkParameters(toBeVerified, notSatisfied);

        if (!notSatisfied.isEmpty()) {
            failures.add(new ExpectedInvocationNotHappenedFailure(notSatisfied));
        }

        if (!failures.isEmpty()) {
            throw new VerificationFailedReporter(failures);
        }
    }

    private List<VerificationFailure> checkParameters(List<? extends Verifiable> toBeVerified, List<Verification> notSatisfied) {
        List<VerificationFailure> failures = new ArrayList<>();

        Iterator<Verification> iter = notSatisfied.iterator();
        while (iter.hasNext()) {
            Verification verification = iter.next();
            Optional<? extends Verifiable> parameterDiff = toBeVerified.stream()
                    .filter(invocation -> invocation.sameMethod(verification))
                    .filter(invocation -> !verification.satisfyWith(invocation))
                    .findFirst();

            if (parameterDiff.isPresent()) {
                failures.add(new ParametersNotMatchFailure(parameterDiff.get(), verification));
                iter.remove();
            }
        }

        return failures;
    }
}
