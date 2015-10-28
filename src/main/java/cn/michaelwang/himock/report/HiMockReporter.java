package cn.michaelwang.himock.report;

import java.util.ArrayList;
import java.util.List;

public class HiMockReporter extends RuntimeException {
    protected HiMockReporter() {
        simplifyTheStackTraces();
    }

    private void simplifyTheStackTraces() {
        StackTraceElement[] traces = getStackTrace();
        List<StackTraceElement> usefulTraces = new ArrayList<>();

        for (StackTraceElement trace : traces) {
            if (trace.getClassName().equals("sun.reflect.NativeMethodAccessorImpl")) {
                break;
            }

            // the second condition is intend for the test of the class
            if (!trace.getClassName().startsWith("cn.michaelwang.himock")
                    || trace.getMethodName().startsWith("test")) {
                usefulTraces.add(trace);
            }
        }

        StackTraceElement[] newTraces = new StackTraceElement[usefulTraces.size()];
        usefulTraces.toArray(newTraces);
        setStackTrace(newTraces);
    }
}
