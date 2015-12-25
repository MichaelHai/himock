package cn.michaelwang.himock;

import cn.michaelwang.himock.matcher.Matcher;
import cn.michaelwang.himock.mockup.MockFactoryImpl;
import cn.michaelwang.himock.process.MockFactory;
import cn.michaelwang.himock.process.MockStateManager;
import cn.michaelwang.himock.record.InvocationRecorder;
import cn.michaelwang.himock.report.HiMockReporter;

public class HiMock {
    private MockProcessManager mockProcessManager;

    public HiMock() {
        MockFactory mockFactory = MockFactoryImpl.getInstance();
        InvocationRecorder invocationRecorder = new InvocationRecorder();
        mockProcessManager = new MockStateManager(mockFactory, invocationRecorder);
    }

    public <T> T mock(Class<T> mockedInterface) {
        return mockProcessManager.mock(mockedInterface);
    }

    public void expect(Expectation expectation) {
        mockProcessManager.toExpectState();
        try {
            expectation.expect();
        } catch (Throwable throwable) {
            if (throwable instanceof HiMockReporter) {
                throw (HiMockReporter) throwable;
            }
        }
        mockProcessManager.toNormalState();
    }

    public <T> HiMock willReturn(T returnValue) {
        mockProcessManager.lastCallReturn(returnValue, returnValue.getClass());
        return this;
    }

    public HiMock willReturn(boolean returnValue) {
        mockProcessManager.lastCallReturn(returnValue, boolean.class);
        return this;
    }

    @SuppressWarnings("unused") // simple function not tested
    public HiMock willReturn(byte returnValue) {
        mockProcessManager.lastCallReturn(returnValue, byte.class);
        return this;
    }

    @SuppressWarnings("unused") // simple function not tested
    public HiMock willReturn(char returnValue) {
        mockProcessManager.lastCallReturn(returnValue, char.class);
        return this;
    }

    @SuppressWarnings("unused") // simple function not tested
    public HiMock willReturn(short returnValue) {
        mockProcessManager.lastCallReturn(returnValue, short.class);
        return this;
    }

    @SuppressWarnings("unused") // simple function not tested
    public HiMock willReturn(int returnValue) {
        mockProcessManager.lastCallReturn(returnValue, int.class);
        return this;
    }

    @SuppressWarnings("unused") // simple function not tested
    public HiMock willReturn(long returnValue) {
        mockProcessManager.lastCallReturn(returnValue, long.class);
        return this;
    }

    @SuppressWarnings("unused") // simple function not tested
    public HiMock willReturn(float returnValue) {
        mockProcessManager.lastCallReturn(returnValue, float.class);
        return this;
    }

    @SuppressWarnings("unused") // simple function not tested
    public HiMock willReturn(double returnValue) {
        mockProcessManager.lastCallReturn(returnValue, double.class);
        return this;
    }

    public HiMock willThrow(Throwable e) {
        mockProcessManager.lastCallThrow(e);
        return this;
    }

    public HiMock times(int times) {
        mockProcessManager.lastReturnTimer(times);
        return this;
    }

    public <T> T match(Matcher<T> matcher) {
        mockProcessManager.addMatcher(matcher);
        return null;
    }

    public boolean matchBoolean(Matcher<Boolean> matcher) {
        mockProcessManager.addMatcher(matcher);
        return false;
    }

    @SuppressWarnings("unused") // simple function not tested
    public byte matchByte(Matcher<Byte> matcher) {
        mockProcessManager.addMatcher(matcher);
        return 0;
    }

    @SuppressWarnings("unused") // simple function not tested
    public char matchChar(Matcher<Character> matcher) {
        mockProcessManager.addMatcher(matcher);
        return 0;
    }

    @SuppressWarnings("unused") // simple function not tested
    public short matchShort(Matcher<Short> matcher) {
        mockProcessManager.addMatcher(matcher);
        return 0;
    }

    public int matchInt(Matcher<Integer> matcher) {
        mockProcessManager.addMatcher(matcher);
        return 0;
    }

    @SuppressWarnings("unused") // simple function not tested
    public long matchLong(Matcher<Long> matcher) {
        mockProcessManager.addMatcher(matcher);
        return 0;
    }

    @SuppressWarnings("unused") // simple function not tested
    public float matchFloat(Matcher<Float> matcher) {
        mockProcessManager.addMatcher(matcher);
        return 0;
    }

    @SuppressWarnings("unused") // simple function not tested
    public double matchDouble(Matcher<Double> matcher) {
        mockProcessManager.addMatcher(matcher);
        return 0;
    }

    public void verify() {
        mockProcessManager.doVerify();
    }

    public void verify(Verification verification) {
        mockProcessManager.toVerifyState();
        verification.record();
        mockProcessManager.toNormalState();
        verify();
    }

    public void verifyInOrder(Verification verification) {
        mockProcessManager.toOrderedVerifyState();
        verification.record();
        mockProcessManager.toNormalState();
        verify();
    }

    @FunctionalInterface
    public interface Expectation {
        void expect() throws Throwable;
    }

    @FunctionalInterface
    public interface Verification {
        void record();
    }
}