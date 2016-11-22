package cn.michaelwang.himock.process.verifiers;

import cn.michaelwang.himock.HitNeverTimerException;
import cn.michaelwang.himock.Invocation;
import cn.michaelwang.himock.process.Timer;
import cn.michaelwang.himock.process.TimerChecker;
import cn.michaelwang.himock.process.Verification;
import cn.michaelwang.himock.process.Verifier;
import cn.michaelwang.himock.process.timer.TimerCheckerImpl;
import cn.michaelwang.himock.process.verifiers.failures.ArgumentsNotMatchFailure;
import cn.michaelwang.himock.process.verifiers.failures.ExpectedInvocationNotHappenedFailure;
import cn.michaelwang.himock.process.verifiers.failures.ExpectedTimesNotSatisfiedFailure;
import cn.michaelwang.himock.process.verifiers.failures.UnexpectedInvocationHappenedFailure;

import java.util.*;

public class NormalVerifier implements Verifier {
    private final List<Verification> verifications = new ArrayList<>();
    private final Map<Verification, TimerChecker> verificationTimerCheckerMap = new HashMap<>();
    private Verification lastVerification;

    @Override
    public void addVerification(Verification verification) {
        verifications.add(verification);
        verificationTimerCheckerMap.put(verification, new TimerCheckerImpl());
        this.lastVerification = verification;
    }

    @Override
    public void verify(List<Invocation> toBeVerified) {
        List<VerificationFailure> failures = new ArrayList<>();

        List<Verification> failedVerifications = new ArrayList<>();
        toBeVerified.forEach(invocation -> verifications.stream()
                .filter(verification -> verification.satisfyWith(invocation))
                .filter(verification -> verificationTimerCheckerMap.get(verification).hitMore())
                .findFirst()
                .ifPresent(verification -> {
                    try {
                        verificationTimerCheckerMap.get(verification).hit();
                    } catch (HitNeverTimerException e) {
                        failedVerifications.add(verification);
                        failures.add(new UnexpectedInvocationHappenedFailure(invocation));
                    }
                }));

        verificationTimerCheckerMap.forEach((verification, checker) -> {
            if (!failedVerifications.contains(verification) && !checker.check()) {
                int hitTimes = checker.getHitTimes();
                if (hitTimes != 0) {
                    failures.add(new ExpectedTimesNotSatisfiedFailure(verification.getVerifiedInvocation(), checker.getExpect(), hitTimes));
                } else {
                    Optional<Invocation> argumentNotMatchInvocation = toBeVerified.stream()
                            .filter(verification.getVerifiedInvocation()::sameMethod)
                            .filter(invocation -> !verification.satisfyWith(invocation))
                            .findFirst();

                    if (argumentNotMatchInvocation.isPresent()) {
                        failures.add(new ArgumentsNotMatchFailure(argumentNotMatchInvocation.get(), verification.getVerifiedInvocation()));
                    } else {
                        failures.add(new ExpectedInvocationNotHappenedFailure(verification.getVerifiedInvocation()));
                    }
                }
            }
        });

        if (!failures.isEmpty()) {
            throw new VerificationFailedReporter(failures);
        }
    }

    @Override
    public void addVerificationTimes(Timer timer) {
        verificationTimerCheckerMap.get(lastVerification).addTimer(timer);
    }
}
