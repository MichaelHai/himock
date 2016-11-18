package cn.michaelwang.himock.process.timer;

import cn.michaelwang.himock.process.Timer;

public class LessThanTimer implements Timer {
    private final int maxTimes;
    private int hit;

    public LessThanTimer(int maxTimes) {
        this.maxTimes = maxTimes;
    }

    @Override
    public boolean hit() {
        if (hit >= maxTimes) {
            throw new HitMoreThanExpectedTimesException();
        } else {
            hit++;
        }

        /*
         * this timer should check weather the hit times less than expected,
         * in this case, all hit should come to this timer.
         */
        return false;
    }

    @Override
    public Timer copy() {
        return new LessThanTimer(maxTimes);
    }

    @Override
    public String getTimes() {
        return "less than " + maxTimes + " time" + (maxTimes > 1 ? "s" : "");
    }

    @Override
    public boolean pass() {
        return hit < maxTimes;
    }

    @Override
    public boolean hitMore() {
        // always can be hit
        return true;
    }
}
