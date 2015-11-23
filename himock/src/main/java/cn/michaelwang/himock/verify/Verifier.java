package cn.michaelwang.himock.verify;

import cn.michaelwang.himock.invocation.Invocation;
import cn.michaelwang.himock.report.VerificationFailedException;

import java.util.*;
import java.util.stream.Collectors;

public class Verifier {
    private Set<Invocation> verificationInvocations = new HashSet<>();

    public List<VerificationFailedException> verify(List<Invocation> actuallyInvocations) {
        List<VerificationFailedException> exceptions = new ArrayList<>();

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
                exceptions.add(new ParametersNotMatchException(parameterDiff.get(), invocationRecord));
                iter.remove();
            }
        }

        if (!notCalled.isEmpty()) {
            exceptions.add(new ExpectedInvocationNotHappenedException(notCalled));
        }

        return exceptions;
    }

    public void addVerification(Invocation invocation) {
        verificationInvocations.add(invocation);
    }
}
