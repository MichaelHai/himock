package cn.michaelwang.himock.verify;

import cn.michaelwang.himock.invocation.Invocation;
import cn.michaelwang.himock.verify.failure.OrderFailure;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class InOrderVerifier implements Verifier {
    private List<Invocation> expectedOrders = new ArrayList<>();

    @Override
    public void addVerification(Invocation invocation) {
        expectedOrders.add(invocation);
    }

    @Override
    public void verify(List<Invocation> actuallyInvocations) {
        int index = -1;
        Set<Integer> findIndexes = new HashSet<>();
        for (int i = 0; i < expectedOrders.size(); i++) {
            Invocation invocation = expectedOrders.get(i);
            index = findAfter(index, actuallyInvocations, invocation);
            if (index != -1) {
                findIndexes.add(index);
            } else {
                int outOfOrderIndex;
                do {
                    outOfOrderIndex = actuallyInvocations.indexOf(invocation);
                } while (findIndexes.contains(outOfOrderIndex));
                findIndexes.add(outOfOrderIndex);
                List<Invocation> actuallyOrder = findIndexes.stream().sorted().map(actuallyInvocations::get).collect(Collectors.toList());
                throw new VerificationFailedReporter(new OrderFailure(expectedOrders.subList(0, i+1), actuallyOrder));
            }
        }
    }

    private int findAfter(int index, List<Invocation> actuallyInvocations, Invocation invocation) {
        for (int i = index+1; i < actuallyInvocations.size(); i++) {
            if (actuallyInvocations.get(i).equals(invocation)) {
                return i;
            }
        }

        return -1;
    }
}
