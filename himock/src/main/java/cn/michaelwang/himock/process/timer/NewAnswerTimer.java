package cn.michaelwang.himock.process.timer;

import cn.michaelwang.himock.process.Timer;

public class NewAnswerTimer implements Timer {
    private boolean hit = false;

    @Override
    public void hit() {
        if (hit) {
            throw new HitMoreThanExpectedTimesException("NewAnswerTimer should be hit only once.");
        }
        hit = true;
    }

    @Override
    public Timer copy() {
        return new NewAnswerTimer();
    }

    @Override
    public String getTimes() {
        return "1";
    }

    @Override
    public boolean pass() {
        return hit;
    }

    @Override
    public boolean hitMore() {
        return !hit;
    }
}
