package cn.michaelwang.himock.process.timer;

import org.junit.Test;

import static org.junit.Assert.*;

public class TimerCheckerTest {
    @Test
    public void testNoTimerShouldBeHitOnce() {
        TimerChecker checker = new TimerChecker();

        assertFalse(checker.check());

        checker.hit();
        assertTrue(checker.check());
    }

    @Test
    public void testTimerCheckerWithExactTimer() {
        TimerChecker checker = new TimerChecker();
        checker.addTimer(new ExactTimer(2));

        assertFalse(checker.check());

        checker.hit();
        assertFalse(checker.check());

        checker.hit();
        assertTrue(checker.check());

        assertHitMore(checker, "The timer(s) are hit more than expected times.");
    }

    @Test
    public void testTimerCheckerWithNewAnswerTimer() {
        TimerChecker checker = new TimerChecker();
        checker.addTimer(new NewAnswerTimer());

        assertFalse(checker.check());

        checker.hit();
        assertTrue(checker.check());
    }

    @Test
    public void testTimerCheckerWithNewAnswerTimerAndExactTimer() {
        TimerChecker checker = new TimerChecker();
        checker.addTimer(new NewAnswerTimer());
        checker.addTimer(new ExactTimer(2));

        assertFalse(checker.check());

        checker.hit();
        assertFalse(checker.check());

        checker.hit();
        assertTrue(checker.check());
    }

    @Test
    public void testTimerCheckerWithContinuesMultipleNewAnswerTimer() {
        TimerChecker checker = new TimerChecker();
        checker.addTimer(new NewAnswerTimer());
        checker.addTimer(new NewAnswerTimer());
        checker.addTimer(new ExactTimer(2));
        checker.addTimer(new NewAnswerTimer());

        assertFalse(checker.check());

        checker.hit();
        assertFalse(checker.check());

        checker.hit();
        assertFalse(checker.check());

        checker.hit();
        assertFalse(checker.check());

        checker.hit();
        assertTrue(checker.check());
    }

    @Test
    public void testTimerCheckerWithContinuesMultipleNewAnswerTimerWithExactTimerBetween() {
        TimerChecker checker = new TimerChecker();
        checker.addTimer(new NewAnswerTimer());
        checker.addTimer(new NewAnswerTimer());
        checker.addTimer(new NewAnswerTimer());

        assertFalse(checker.check());

        checker.hit();
        assertFalse(checker.check());

        checker.hit();
        assertFalse(checker.check());

        checker.hit();
        assertTrue(checker.check());
    }

    private void assertHitMore(TimerChecker checker, String message) {
        try {
            checker.hit();
            fail("HitMoreThanExpectedTimesException should be thrown");
        } catch (HitMoreThanExpectedTimesException e) {
            assertEquals(message, e.getMessage());
        }
    }

//    @Test
//    public void testCompoundTimerWithExactTimerAndDefaultTimerShouldThrowException() {
//        ExactTimer exactTimer = new ExactTimer(2);
//        DefaultTimer defaultTimer = new DefaultTimer();
//
//        try {
//            new CompoundTimer(exactTimer, defaultTimer);
//            fail("InvalidTimerCompoundException should be thrown");
//        } catch (InvalidTimerCompoundException ex) {
//            assertEquals("Cannot compound ExactTimer and DefaultTimer.", ex.getMessage());
//        }
//    }

}
