package cn.michaelwang.himock.verify;

import cn.michaelwang.himock.invocation.Invocation;
import cn.michaelwang.himock.verify.failure.ExpectedInvocationNotHappenedFailure;
import cn.michaelwang.himock.verify.failure.ParametersNotMatchFailure;

import java.util.*;
import java.util.stream.Collectors;

public class Verifier {
    private Set<Invocation> verificationInvocations = new HashSet<>();

    public void verify(List<Invocation> actuallyInvocations) {
        List<VerificationFailure> exceptions = new ArrayList<>();

        List<Invocation> notCalled = verificationInvocations.stream()
                .filter(invocationRecord ->
                        !actuallyInvocations.contains(invocationRecord)
                                || !invocationRecord.isAllReturned())
                .collect(Collectors.toList());

        Iterator<Invocation> iter = notCalled.iterator();

        while (iter.hasNext()) {
            Invocation invocationRecord = iter.next();
            Optional<Invocation> parameterDiff = actuallyInvocations.stream()
                    .filter(actualInvocation -> !actualInvocation.equals(invocationRecord))
                    .filter(actualInvocation -> actualInvocation.getId() == invocationRecord.getId())
                    .filter(actualInvocation -> actualInvocation.getMethodName().equals(invocationRecord.getMethodName()))
                    .findFirst();

            if (parameterDiff.isPresent()) {
                exceptions.add(new ParametersNotMatchFailure(parameterDiff.get(), invocationRecord));
                iter.remove();
            }
        }

        if (!notCalled.isEmpty()) {
            exceptions.add(new ExpectedInvocationNotHappenedFailure(notCalled));
        }

        if (!exceptions.isEmpty()) {
            throw new VerificationFailedReporter(exceptions);
        }
    }

    public void addVerification(Invocation invocation) {
        verificationInvocations.add(invocation);
    }
}
