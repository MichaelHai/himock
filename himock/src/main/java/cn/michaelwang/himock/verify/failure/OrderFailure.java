package cn.michaelwang.himock.verify.failure;

import cn.michaelwang.himock.invocation.Invocation;
import cn.michaelwang.himock.report.ReportBuilder;
import cn.michaelwang.himock.verify.VerificationFailure;

import java.util.List;

public class OrderFailure implements VerificationFailure {
    private List<Invocation> expected;
    private List<Invocation> actually;

    public OrderFailure(List<Invocation> expected, List<Invocation> actually) {
        this.expected = expected;
        this.actually = actually;
    }

    @Override
    public void buildReport(ReportBuilder reportBuilder) {
        reportBuilder.appendLine("method called in different order:");
        reportBuilder.buildNextLevel((expectedBuilder) -> {
            expectedBuilder.appendLine("verified order:");
            expectedBuilder.buildNextLevel(invocationListBuilder ->
                    expected.forEach(invocation ->
                            invocationListBuilder.appendInvocationMessage(invocation.getMethodName(), invocation.getParameters())));
        });
        reportBuilder.buildNextLevel((actualBuilder) -> {
            actualBuilder.appendLine("actual order:");
            actualBuilder.buildNextLevel(invocationListBuilder ->
                    actually.forEach(invocation ->
                            invocationListBuilder.appendInvocationMessage(invocation.getMethodName(), invocation.getParameters())));
        });
    }
}
