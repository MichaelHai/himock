package cn.michaelwang.himock.process.timer;

import java.util.ArrayList;
import java.util.List;

public class TimerChecker {
    private List<Timer> timers = new ArrayList<>();
    private int index = 0;
    private boolean hitAtLeastOnce;

    public boolean check() {
        return hitAtLeastOnce && index >= timers.size();
    }

    public void hit() {
        hitAtLeastOnce = true;
        try {
            if (!timers.isEmpty() && timers.get(index).hit()) {
                index++;
            }
        } catch (IndexOutOfBoundsException e) {
            throw new HitMoreThanExpectedTimesException();
        }
    }

    public void addTimer(Timer timer) {
        int lastIndex = timers.size() - 1;
        if (!timers.isEmpty() && (timers.get(lastIndex) instanceof NewAnswerTimer)) {
            if (!(timer instanceof NewAnswerTimer)) {
                timers.remove(lastIndex);
            }
        }
        timers.add(timer);
    }
}
