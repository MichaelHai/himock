package cn.michaelwang.himock.process.verifiers;

import java.util.*;
import java.util.stream.Collectors;

import cn.michaelwang.himock.Invocation;
import cn.michaelwang.himock.process.Verification;
import cn.michaelwang.himock.process.Verifier;
import cn.michaelwang.himock.process.Timer;
import cn.michaelwang.himock.process.TimerChecker;
import cn.michaelwang.himock.process.timer.TimerCheckerImpl;
import cn.michaelwang.himock.process.verifiers.failures.ArgumentsNotMatchFailure;
import cn.michaelwang.himock.process.verifiers.failures.ExpectedInvocationNotHappenedFailure;
import cn.michaelwang.himock.process.verifiers.failures.ExpectedTimesNotSatisfiedFailure;

public class NormalVerifier implements Verifier {
    private List<Verification> verifications = new ArrayList<>();
    private Map<Verification, Integer> verificationCount = new HashMap<>();
    private Map<Verification, TimerChecker> verificationTimerCheckerMap = new HashMap<>();
    private Verification lastVerification;

    @Override
    public void addVerification(Verification verification) {
        verifications.add(verification);
        verificationCount.put(verification, null);
        verificationTimerCheckerMap.put(verification, new TimerCheckerImpl());
        this.lastVerification = verification;
    }

    @Override
    public void verify(List<Invocation> toBeVerified) {
        List<VerificationFailure> failures = new ArrayList<>();

        toBeVerified.forEach(invocation -> verifications.stream()
                .filter(verification -> verification.satisfyWith(invocation))
                .filter(verification -> !verificationTimerCheckerMap.get(verification).check())
                .findFirst()
                .ifPresent(verification ->verificationTimerCheckerMap.get(verification).hit()));

        verificationTimerCheckerMap.forEach((verification, checker) -> {
            if (!checker.check()) {
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
