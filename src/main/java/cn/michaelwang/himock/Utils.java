package cn.michaelwang.himock;

import java.util.ArrayList;
import java.util.List;

public class Utils {
    public static String removeParenthesesInFunctionName(String functionName) {
        if (functionName.endsWith("()")) {
            return functionName.substring(0, functionName.length() - 2);
        } else {
            return functionName;
        }
    }

    public static StackTraceElement[] simplifyTheStackTraces(StackTraceElement[] traces) {
        List<StackTraceElement> usefulTraces = new ArrayList<>();

        for (StackTraceElement trace : traces) {
            if (trace.getClassName().equals("sun.reflect.NativeMethodAccessorImpl")) {
                break;
            }

            // the second and third condition is intend for the test of the class
            if (!(trace.getClassName().startsWith("cn.michaelwang.himock") || trace.getFileName() == null)
                    || trace.getMethodName().startsWith("test")
                    || trace.getMethodName().startsWith("lambda$test")) {
                usefulTraces.add(trace);
            }
        }

        StackTraceElement[] newTraces = new StackTraceElement[usefulTraces.size()];
        usefulTraces.toArray(newTraces);
        return newTraces;
    }

}
