package cn.michaelwang.himock.process.timer;

import cn.michaelwang.himock.process.Timer;
import org.junit.Test;

import static org.junit.Assert.*;

public class TimerTest {
    @Test
    public void testExactTimerShouldBeHitTheGivenTimes() {
        ExactTimer exactTimer = new ExactTimer(3);

        assertFalse(exactTimer.hit());
        assertFalse(exactTimer.hit());
        assertTrue(exactTimer.hit());

        assertHitMore(exactTimer, "Timer should be hit exactly 3 times");
    }

    @Test
    public void testNewAnswerTimerShouldBeHitOnlyOnceWithoutOtherTimers() {
        NewAnswerTimer newAnswerTimer = new NewAnswerTimer();
        assertTrue(newAnswerTimer.hit());
        assertHitMore(newAnswerTimer, "NewAnswerTimer should be hit only once.");
    }

    private void assertHitMore(Timer timer, String message) {
        try {
            timer.hit();
            fail("HitMoreThanExpectedTimesException should be thrown");
        } catch (HitMoreThanExpectedTimesException e) {
            assertEquals(message, e.getMessage());
        }
    }
}
