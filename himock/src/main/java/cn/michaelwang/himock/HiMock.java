package cn.michaelwang.himock;

import cn.michaelwang.himock.annotations.AnnotationHandler;
import cn.michaelwang.himock.annotations.DefaultAnnotationHandler;
import cn.michaelwang.himock.preprocess.Preprocessor;
import cn.michaelwang.himock.process.InvocationRecorder;
import cn.michaelwang.himock.process.MockStateManager;
import cn.michaelwang.himock.report.HiMockReporter;

public class HiMock {
    private static MockProcessManager mockProcessManager;

    private HiMock() {
    }

    public static void setup(Class<?> testSuits) throws IllegalArgumentException, IllegalAccessException {
        Preprocessor preprocessor = new Preprocessor(testSuits);
        preprocessor.doPreprocess();

        IMatcherIndex matcherIndex = preprocessor.getMatcherIndex();
        InvocationRecorder invocationRecorder = new InvocationRecorder();
        mockProcessManager = new MockStateManager(invocationRecorder, matcherIndex);
    }
    
    protected static void injectFields(Object testSuits) throws IllegalArgumentException, IllegalAccessException {
        AnnotationHandler annotationHandler = new DefaultAnnotationHandler();
        annotationHandler.process(testSuits);
    }

    public static <T> T mock(Class<T> mockedType) {
        return mockProcessManager.mock(mockedType);
    }
    
    public static <T> T mock(Class<T> mockedType, Object... constructorParameters) {
    	return mockProcessManager.mock(mockedType, constructorParameters);
    }

    public static void expect(Expectation expectation) {
        mockProcessManager.toExpectState();
        try {
            expectation.expect();
        } catch (HiMockReporter reporter) {
            throw reporter;
        } catch (Throwable throwable) {
            // do nothing
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

    public static void willAnswer(Answer answer) {
        mockProcessManager.lastCallAnswer(answer);
    }

    public static void times(int times) {
        mockProcessManager.lastReturnTimer(times);
    }

    public static <T> T match(MatcherCondition<T> matcher) {
        mockProcessManager.addMatcher(new ConditionMatcher<>(matcher));
        return null;
    }

    public static boolean matchBoolean(MatcherCondition<Boolean> matcher) {
        mockProcessManager.addMatcher(new ConditionMatcher<>(matcher));
        return false;
    }

    @SuppressWarnings("unused") // simple function not tested
    public static byte matchByte(MatcherCondition<Byte> matcher) {
        mockProcessManager.addMatcher(new ConditionMatcher<>(matcher));
        return 0;
    }

    @SuppressWarnings("unused") // simple function not tested
    public static char matchChar(MatcherCondition<Character> matcher) {
        mockProcessManager.addMatcher(new ConditionMatcher<>(matcher));
        return 0;
    }

    @SuppressWarnings("unused") // simple function not tested
    public static short matchShort(MatcherCondition<Short> matcher) {
        mockProcessManager.addMatcher(new ConditionMatcher<>(matcher));
        return 0;
    }

    public static int matchInt(MatcherCondition<Integer> matcher) {
        mockProcessManager.addMatcher(new ConditionMatcher<>(matcher));
        return 0;
    }

    @SuppressWarnings("unused") // simple function not tested
    public static long matchLong(MatcherCondition<Long> matcher) {
        mockProcessManager.addMatcher(new ConditionMatcher<>(matcher));
        return 0;
    }

    @SuppressWarnings("unused") // simple function not tested
    public static float matchFloat(MatcherCondition<Float> matcher) {
        mockProcessManager.addMatcher(new ConditionMatcher<>(matcher));
        return 0;
    }

    @SuppressWarnings("unused") // simple function not tested
    public static double matchDouble(MatcherCondition<Double> matcher) {
        mockProcessManager.addMatcher(new ConditionMatcher<>(matcher));
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

    @FunctionalInterface
    public interface MatcherCondition<T> {
        boolean isMatch(T actual);
    }

    private static class ConditionMatcher<T> implements Matcher<T> {
        private MatcherCondition<T> condition;

        ConditionMatcher(MatcherCondition<T> condition) {
            this.condition = condition;
        }

        @Override
        public boolean isMatch(T actual) {
            return condition.isMatch(actual);
        }

        @Override
        public String toString() {
            return "'matcher'";
        }
    }
}