package cn.michaelwang.himock;

import cn.michaelwang.himock.mockup.MockFactoryImpl;
import cn.michaelwang.himock.process.InvocationRecorder;
import cn.michaelwang.himock.process.MockFactory;
import cn.michaelwang.himock.process.MockStateManager;
import cn.michaelwang.himock.report.HiMockReporter;

public class HiMock {
    private static MockProcessManager mockProcessManager;

    private HiMock() {
    }

    public static void setup() {
        MockFactory mockFactory = MockFactoryImpl.getInstance();
        InvocationRecorder invocationRecorder = new InvocationRecorder();
        mockProcessManager = new MockStateManager(mockFactory, invocationRecorder);
    }

    public static <T> T mock(Class<T> mockedInterface) {
        return mockProcessManager.mock(mockedInterface);
    }

    public static void expect(Expectation expectation) {
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

    public static <T> void willReturn(T returnValue) {
        mockProcessManager.lastCallReturn(returnValue, returnValue.getClass());
    }

    public static void willReturn(boolean returnValue) {
        mockProcessManager.lastCallReturn(returnValue, boolean.class);
    }

    @SuppressWarnings("unused") // simple function not tested
    public static void willReturn(byte returnValue) {
        mockProcessManager.lastCallReturn(returnValue, byte.class);
    }

    @SuppressWarnings("unused") // simple function not tested
    public static void willReturn(char returnValue) {
        mockProcessManager.lastCallReturn(returnValue, char.class);
    }

    @SuppressWarnings("unused") // simple function not tested
    public static void willReturn(short returnValue) {
        mockProcessManager.lastCallReturn(returnValue, short.class);
    }

    @SuppressWarnings("unused") // simple function not tested
    public static void willReturn(int returnValue) {
        mockProcessManager.lastCallReturn(returnValue, int.class);
    }

    @SuppressWarnings("unused") // simple function not tested
    public static void willReturn(long returnValue) {
        mockProcessManager.lastCallReturn(returnValue, long.class);
    }

    @SuppressWarnings("unused") // simple function not tested
    public static void willReturn(float returnValue) {
        mockProcessManager.lastCallReturn(returnValue, float.class);
    }

    @SuppressWarnings("unused") // simple function not tested
    public static void willReturn(double returnValue) {
        mockProcessManager.lastCallReturn(returnValue, double.class);
    }

    public static void willThrow(Throwable e) {
        mockProcessManager.lastCallThrow(e);
    }

    public static void times(int times) {
        mockProcessManager.lastReturnTimer(times);
    }

    public static <T> T match(Matcher<T> matcher) {
        mockProcessManager.addMatcher(matcher);
        return null;
    }

    public static boolean matchBoolean(Matcher<Boolean> matcher) {
        mockProcessManager.addMatcher(matcher);
        return false;
    }

    @SuppressWarnings("unused") // simple function not tested
    public static byte matchByte(Matcher<Byte> matcher) {
        mockProcessManager.addMatcher(matcher);
        return 0;
    }

    @SuppressWarnings("unused") // simple function not tested
    public static char matchChar(Matcher<Character> matcher) {
        mockProcessManager.addMatcher(matcher);
        return 0;
    }

    @SuppressWarnings("unused") // simple function not tested
    public static short matchShort(Matcher<Short> matcher) {
        mockProcessManager.addMatcher(matcher);
        return 0;
    }

    public static int matchInt(Matcher<Integer> matcher) {
        mockProcessManager.addMatcher(matcher);
        return 0;
    }

    @SuppressWarnings("unused") // simple function not tested
    public static long matchLong(Matcher<Long> matcher) {
        mockProcessManager.addMatcher(matcher);
        return 0;
    }

    @SuppressWarnings("unused") // simple function not tested
    public static float matchFloat(Matcher<Float> matcher) {
        mockProcessManager.addMatcher(matcher);
        return 0;
    }

    @SuppressWarnings("unused") // simple function not tested
    public static double matchDouble(Matcher<Double> matcher) {
        mockProcessManager.addMatcher(matcher);
        return 0;
    }

    public static void verify() {
        mockProcessManager.doVerify();
    }

    public static void verify(Verification verification) {
        mockProcessManager.toVerifyState();
        verification.record();
        mockProcessManager.toNormalState();
        verify();
    }

    public static void verifyInOrder(Verification verification) {
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