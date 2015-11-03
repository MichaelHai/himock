package cn.michaelwang.himock;

public class Utils {
    public static String removeParenthesesInFunctionName(String functionName) {
        if (functionName.endsWith("()")) {
            return functionName.substring(0, functionName.length() - 2);
        } else {
            return functionName;
        }
    }
}
