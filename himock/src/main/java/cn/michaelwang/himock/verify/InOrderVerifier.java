package cn.michaelwang.himock.verify;

import cn.michaelwang.himock.Invocation;
import cn.michaelwang.himock.Verification;
import cn.michaelwang.himock.verify.failure.OrderFailure;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class InOrderVerifier implements Verifier {
    private List<Verification> orderedVerifications = new ArrayList<>();

    @Override
    public void addVerification(Verification verification) {
        orderedVerifications.add(verification);
    }

    @Override
    public void verify(List<Invocation> toBeVerified) {
        int index = -1;
        Set<Integer> foundIndexes = new HashSet<>();
        for (int i = 0; i < orderedVerifications.size(); i++) {
            Verification verification = orderedVerifications.get(i);
            index = findAfter(index, toBeVerified, verification);
            if (index != -1) {
                foundIndexes.add(index);
            } else {
                int outOfOrderIndex = -1;
                do {
                    outOfOrderIndex = findAfter(outOfOrderIndex, toBeVerified, verification);
                } while (foundIndexes.contains(outOfOrderIndex));
                foundIndexes.add(outOfOrderIndex);

                List<Invocation> actuallyOrder = foundIndexes.stream().sorted().map(toBeVerified::get).collect(Collectors.toList());
                throw new VerificationFailedReporter(
                        new OrderFailure(orderedVerifications.subList(0, i + 1).stream().map(Verification::getInvocation).collect(Collectors.toList()),
                                actuallyOrder));
            }
        }
    }

    private int findAfter(int index, List<Invocation> toBeVerified, Verification verification) {
        for (int i = index + 1; i < toBeVerified.size(); i++) {
            if (verification.satisfyWith(toBeVerified.get(i))) {
                return i;
            }
        }

        return -1;
    }
}
