package cn.michaelwang.himock.process.verifiers;

import cn.michaelwang.himock.Invocation;
import cn.michaelwang.himock.process.Verification;
import cn.michaelwang.himock.process.Verifier;
import cn.michaelwang.himock.process.verifiers.failures.OrderFailure;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class InOrderVerifier implements Verifier {
	private List<Verification> orderedVerifications = new ArrayList<>();
	private Verification lastVerification;

	@Override
	public void addVerification(Verification verification) {
		orderedVerifications.add(verification);
		this.lastVerification = verification;
	}

	@Override
	public void verify(List<Invocation> toBeVerified) {
		int index = -1;
		Set<Integer> foundIndexes = new HashSet<>();
		for (int i = 0; i < orderedVerifications.size(); i++) {
			Verification verification = orderedVerifications.get(i);

			index = findAfter(index, toBeVerified, verification);
			if (index == -1) {
				OrderFailure failure = generateFailure(toBeVerified, foundIndexes, i, verification);
				throw new VerificationFailedReporter(failure);
			} else {
				foundIndexes.add(index);
			}
		}
	}

	private OrderFailure generateFailure(List<Invocation> toBeVerified, Set<Integer> foundIndexes,
			int verificationsEndIndex, Verification verification) {
		int outOfOrderIndex = -1;
		do {
			outOfOrderIndex = findAfter(outOfOrderIndex, toBeVerified, verification);
		} while (foundIndexes.contains(outOfOrderIndex));
		foundIndexes.add(outOfOrderIndex);

		List<Invocation> expectedOrder = orderedVerifications
				.subList(0, verificationsEndIndex + 1).stream()
				.map(Verification::getVerifiedInvocation)
				.collect(Collectors.toList());
		List<Invocation> actuallyOrder = foundIndexes.stream()
				.sorted()
				.map(toBeVerified::get)
				.collect(Collectors.toList());
		return new OrderFailure(expectedOrder, actuallyOrder);
	}

	private int findAfter(int index, List<Invocation> toBeVerified, Verification verification) {
		for (int i = index + 1; i < toBeVerified.size(); i++) {
			if (verification.satisfyWith(toBeVerified.get(i))) {
				return i;
			}
		}

		return -1;
	}

	@Override
	public void addVerificationTimes(int times) {
		for (int i = 0; i < times - 1; i++) {
			this.addVerification(lastVerification);
		}
	}
}
