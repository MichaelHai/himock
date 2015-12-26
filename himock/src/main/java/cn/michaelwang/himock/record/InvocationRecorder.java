package cn.michaelwang.himock.record;

import cn.michaelwang.himock.invocation.InvocationImpl;
import cn.michaelwang.himock.invocation.NullInvocation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InvocationRecorder {
    private List<InvocationImpl> expectedInvocations = new ArrayList<>();
    private List<InvocationImpl> actuallyInvocations = new ArrayList<>();

    public Object actuallyCall(InvocationImpl invocation) throws Throwable {
        actuallyInvocations.add(invocation);
        return expectedInvocations.stream()
                .filter(invocation::equals)
                .findFirst().orElse(new NullInvocation(invocation.getReturnType()))
                .getReturnValue();
    }

    public InvocationImpl expect(InvocationImpl invocation) {
        Optional<InvocationImpl> exist = expectedInvocations.stream().filter(invocation::equals).findFirst();

        if (exist.isPresent()) {
            return exist.get();
        } else {
            expectedInvocations.add(invocation);
            return invocation;
        }
    }

    public List<InvocationImpl> getActuallyInvocations() {
        return actuallyInvocations;
    }
}