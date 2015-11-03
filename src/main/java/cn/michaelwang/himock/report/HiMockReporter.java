package cn.michaelwang.himock.report;

import cn.michaelwang.himock.HiMockException;

public class HiMockReporter extends HiMockException {
    protected HiMockReporter() {
        setStackTrace(new StackTraceElement[0]);
    }
}
