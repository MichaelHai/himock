package cn.michaelwang.himock.record;

import cn.michaelwang.himock.invocation.Invocation;
import cn.michaelwang.himock.invocation.NullInvocation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InvocationRecorder {
    private List<Invocation> expectedInvocations = new ArrayList<>();
    private List<Invocation> actuallyInvocations = new ArrayList<>();

    public Object actuallyCall(Invocation invocation) {
        actuallyInvocations.add(invocation);
        return expectedInvocations.stream()
                .filter(invocation::equals)
                .findFirst().orElse(new NullInvocation(invocation.getReturnType()))
                .getReturnValue();
    }

    public Invocation expect(Invocation invocation) {
        Optional<Invocation> exist = expectedInvocations.stream().filter(invocation::equals).findFirst();

        if (exist.isPresent()) {
            return exist.get();
        } else {
            expectedInvocations.add(invocation);
            return invocation;
        }
    }

    public List<Invocation> getActuallyInvocations() {
        return actuallyInvocations;
    }
}