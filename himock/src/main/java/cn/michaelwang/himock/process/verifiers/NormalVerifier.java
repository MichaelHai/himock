package cn.michaelwang.himock.process.verifiers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import cn.michaelwang.himock.Invocation;
import cn.michaelwang.himock.process.Verification;
import cn.michaelwang.himock.process.Verifier;
import cn.michaelwang.himock.process.verifiers.failures.ArgumentsNotMatchFailure;
import cn.michaelwang.himock.process.verifiers.failures.ExpectedInvocationNotHappenedFailure;
import cn.michaelwang.himock.process.verifiers.failures.ExpectedTimesNotSatisfiedFailure;

public class NormalVerifier implements Verifier {
	private List<Verification> verifications = new ArrayList<>();
	private Map<Verification, Integer> verificationCount = new HashMap<>();
	private Verification lastVerification;

	@Override
	public void addVerification(Verification verification) {
		verifications.add(verification);
		verificationCount.put(verification, 0);
		this.lastVerification = verification;
	}

	@Override
	public void verify(List<Invocation> toBeVerified) {
		verificationCount.replaceAll((key, value) -> value == 0 ? 1 : value);

		Map<Verification, Integer> actuallyCounts = new HashMap<>();
		toBeVerified.forEach(invocation -> verifications.stream()
				.filter(verification -> verification.satisfyWith(invocation))
				.findFirst()
				.ifPresent(verification -> {
					actuallyCounts.merge(verification, 1, Math::addExact);
				}));

		Map<Invocation, int[]> invocationDiff = new HashMap<>();
		verificationCount.forEach((verification, expectedCount) -> {
			Integer actuallyCount = actuallyCounts.get(verification);

			if (actuallyCount == null) {
				return;
			}

			if (actuallyCount >= 1) {
				verifications.remove(verification);
			}

			if (expectedCount != 0 && expectedCount != actuallyCount) {
				invocationDiff.put(verification.getVerifiedInvocation(), new int[] { expectedCount, actuallyCount });
			}
		});

		List<Verification> notSatisfied = verifications.stream().collect(Collectors.toList());

		List<VerificationFailure> failures = generateFailures(toBeVerified, notSatisfied, invocationDiff);

		if (!failures.isEmpty()) {
			throw new VerificationFailedReporter(failures);
		}
	}

	private List<VerificationFailure> generateFailures(List<Invocation> toBeVerified, List<Verification> notSatisfied,
			Map<Invocation, int[]> invocationDiff) {
		List<VerificationFailure> failures = new ArrayList<>();

		invocationDiff.forEach((invocation, count) -> {
			failures.add(new ExpectedTimesNotSatisfiedFailure(invocation, count[0], count[1]));
		});

		Iterator<Verification> iter = notSatisfied.iterator();
		while (iter.hasNext()) {
			Verification verification = iter.next();
			toBeVerified.stream()
					.filter(verification.getVerifiedInvocation()::sameMethod)
					.filter(invocation -> !verification.satisfyWith(invocation))
					.findFirst()
					.ifPresent((invocation) -> {
						failures.add(new ArgumentsNotMatchFailure(invocation, verification.getVerifiedInvocation()));
						iter.remove();
					});
		}

		if (!notSatisfied.isEmpty()) {
			List<Invocation> missingInvocations = notSatisfied.stream()
					.map(Verification::getVerifiedInvocation)
					.collect(Collectors.toList());
			failures.add(new ExpectedInvocationNotHappenedFailure(missingInvocations));
		}

		return failures;
	}

	@Override
	public void addVerificationTimes(int times) {
		int count = verificationCount.get(lastVerification);
		count += times;
		verificationCount.put(lastVerification, count);
	}
}
