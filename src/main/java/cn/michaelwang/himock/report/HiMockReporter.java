package cn.michaelwang.himock.report;

import cn.michaelwang.himock.HiMockException;

public class HiMockReporter extends HiMockException {
    protected HiMockReporter() {
        StackTraceElement[] newTraces = simplifyTheStackTraces(this.getStackTrace());
        this.setStackTrace(newTraces);
    }
}
