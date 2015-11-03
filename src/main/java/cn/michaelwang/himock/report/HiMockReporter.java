package cn.michaelwang.himock.report;

import cn.michaelwang.himock.HiMockException;

public class HiMockReporter extends HiMockException {
    @Override
    public StackTraceElement[] getStackTrace() {
        return new StackTraceElement[0];
    }
}
