package cn.michaelwang.himock.process.timer;

import cn.michaelwang.himock.process.TimerChecker;
import org.junit.Test;

import static org.junit.Assert.*;

public class TimerCheckerTest {
    @Test
    public void testNoTimerShouldBeHitOnce() {
        TimerChecker checker = new TimerCheckerImpl();

        assertFalse(checker.check());

        checker.hit();
        assertTrue(checker.check());

        assertEquals(1, checker.getHitTimes());
    }

    @Test
    public void testTimerCheckerWithExactTimer() {
        TimerChecker checker = new TimerCheckerImpl();
        checker.addTimer(new ExactTimer(2));

        assertFalse(checker.check());

        checker.hit();
        assertFalse(checker.check());

        checker.hit();
        assertTrue(checker.check());

        assertHitMore(checker, "The timer(s) are hit more than expected times.");

        assertEquals(2, checker.getHitTimes());
    }

    @Test
    public void testTimerCheckerWithNewAnswerTimer() {
        TimerChecker checker = new TimerCheckerImpl();
        checker.addTimer(new NewAnswerTimer());

        assertFalse(checker.check());

        checker.hit();
        assertTrue(checker.check());

        assertEquals(1, checker.getHitTimes());
    }

    @Test
    public void testTimerCheckerWithNewAnswerTimerAndExactTimer() {
        TimerChecker checker = new TimerCheckerImpl();
        checker.addTimer(new NewAnswerTimer());
        checker.addTimer(new ExactTimer(2));

        assertFalse(checker.check());

        checker.hit();
        assertFalse(checker.check());

        checker.hit();
        assertTrue(checker.check());

        assertEquals(2, checker.getHitTimes());
    }

    @Test
    public void testTimerCheckerWithContinuesMultipleNewAnswerTimer() {
        TimerChecker checker = new TimerCheckerImpl();
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

        assertEquals(4, checker.getHitTimes());
    }

    @Test
    public void testTimerCheckerWithContinuesMultipleNewAnswerTimerWithExactTimerBetween() {
        TimerChecker checker = new TimerCheckerImpl();
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

        assertEquals(3, checker.getHitTimes());
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
