package cn.michaelwang.himock.recorder;

import cn.michaelwang.himock.report.VerificationFailedException;

public class ParametersNotMatchException extends VerificationFailedException {
    private InvocationRecord actuallyInvocation;
    private InvocationRecord expectedInvocation;

    public ParametersNotMatchException(InvocationRecord actuallyInvocation, InvocationRecord expectedInvocation) {
        this.actuallyInvocation = actuallyInvocation;
        this.expectedInvocation = expectedInvocation;
    }

    @Override
    public String getMessage() {
        StringBuilder sb = new StringBuilder();
        String methodName = actuallyInvocation.getMethodName();
        if (methodName.endsWith("()")) {
            methodName = methodName.substring(0, methodName.length()-2);
        }
        sb.append("\tinvocation with unexpected parameters:\n");
        sb.append("\t\tmethod called:\t");
        sb.append(methodName);
        sb.append("\n");

        sb.append("\t\tparameters expected:");
        for (Object parameter: expectedInvocation.getParameters()) {
            sb.append("\t");
            sb.append(parameter);
        }
        sb.append("\n");

        sb.append("\t\tparameters actually:");
        for (Object parameter: actuallyInvocation.getParameters()) {
            sb.append("\t");
            sb.append(parameter);
        }

        return sb.toString();
    }
}
