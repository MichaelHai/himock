package cn.michaelwang.himock;

import java.util.List;

public class ExpectationInvocationNotSatisfiedException extends MockExpectationFailedException {
    private List<String> functionName;

    public ExpectationInvocationNotSatisfiedException(List<String> functionName) {
        this.functionName = functionName;
    }

    @Override
    public String getMessage() {
        String message = super.getMessage() +
                "\texpectation not satisfied: ";
        for (String name : functionName) {
            message += "\n\t\t" + name;
        }

        return message;
    }
}
