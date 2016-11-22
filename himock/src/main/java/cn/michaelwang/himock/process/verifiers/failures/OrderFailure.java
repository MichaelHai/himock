package cn.michaelwang.himock.process.verifiers.failures;

import cn.michaelwang.himock.Invocation;
import cn.michaelwang.himock.process.verifiers.VerificationFailure;
import cn.michaelwang.himock.report.ReportBuilder;

import java.util.List;

public class OrderFailure implements VerificationFailure {
    private final List<Invocation> expected;
    private final List<Invocation> actually;

    public OrderFailure(List<Invocation> expected, List<Invocation> actually) {
        this.expected = expected;
        this.actually = actually;
    }

    @Override
    public void buildReport(ReportBuilder reportBuilder) {
        reportBuilder.appendLine("method called in different order:");
        reportBuilder.buildNextLevel((actualBuilder) -> {
            actualBuilder.appendLine("actual order:");
            actualBuilder.buildNextLevel(invocationListBuilder ->
                    actually.forEach(actuallyInvocation ->
                            invocationListBuilder.appendInvocationMessage(actuallyInvocation.getMethodName(), actuallyInvocation.getArguments())));
        });
        reportBuilder.buildNextLevel((expectedBuilder) -> {
            expectedBuilder.appendLine("verified order:");
            expectedBuilder.buildNextLevel(invocationListBuilder ->
                    expected.forEach(expectedInvocation ->
                            invocationListBuilder.appendInvocationMessage(expectedInvocation.getMethodName(), expectedInvocation.getArguments())));
        });
        reportBuilder.buildNextLevel((locationBuilder) -> locationBuilder.appendStackTrace(expected.get(0).getInvocationStackTrace()));
    }
}
