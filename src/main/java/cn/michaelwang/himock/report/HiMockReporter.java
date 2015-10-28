package cn.michaelwang.himock.report;

import cn.michaelwang.himock.HiMockException;
import cn.michaelwang.himock.Utils;

public class HiMockReporter extends HiMockException {
    protected HiMockReporter() {
        StackTraceElement[] newTraces = Utils.simplifyTheStackTraces(this.getStackTrace());
        this.setStackTrace(newTraces);
    }
}
