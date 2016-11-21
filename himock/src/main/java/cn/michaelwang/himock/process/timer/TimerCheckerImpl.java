package cn.michaelwang.himock.process.timer;

import cn.michaelwang.himock.process.Timer;
import cn.michaelwang.himock.process.TimerChecker;

import java.util.ArrayList;
import java.util.List;

public class TimerCheckerImpl implements TimerChecker {
    private List<Timer> timers = new ArrayList<>();
    private int index = 0;
    private boolean hitAtLeastOnce;
    private int hit = 0;

    @Override
    public boolean check() {
        return isAllNeverTimer() || isAllPassWithOneHit();
    }

    private boolean isAllPassWithOneHit() {
        return hitAtLeastOnce && timers.stream().allMatch(Timer::pass);
    }

    private boolean isAllNeverTimer() {
        return (!timers.isEmpty()) && timers.stream().allMatch(timer -> timer instanceof NeverTimer);
    }

    @Override
    public boolean hitMore() {
        return !hitAtLeastOnce || (index < timers.size() && timers.get(index).hitMore());
    }

    @Override
    public void hit() {
        hitAtLeastOnce = true;
        try {
            if (!timers.isEmpty()) {
                Timer timer = timers.get(index);
                if (timer.hitMore()) {
                    timer.hit();
                    hit++;
                } else {
                    index++;
                    hit();
                }
            }
        } catch (IndexOutOfBoundsException e) {
            throw new HitMoreThanExpectedTimesException();
        }
    }

    @Override
    public void addTimer(Timer timer) {
        int lastIndex = timers.size() - 1;
        if (!timers.isEmpty() && (timers.get(lastIndex) instanceof NewAnswerTimer)) {
            if (!(timer instanceof NewAnswerTimer)) {
                timers.remove(lastIndex);
            }
        }
        timers.add(timer);
    }

    @Override
    public int getHitTimes() {
        if (hit == 0) {
            return hitAtLeastOnce ? 1 : 0;
        } else {
            return hit;
        }
    }

    @Override
    public String getExpect() {
        List<Integer> exactTimes = new ArrayList<>();
        List<String> otherTimes = new ArrayList<>();

        timers.forEach(timer -> {
            String times = timer.getTimes();
            try {
                exactTimes.add(Integer.parseInt(times));
            } catch (NumberFormatException e) {
                otherTimes.add(times);
            }
        });

        StringBuilder result = new StringBuilder();

        if (!exactTimes.isEmpty()) {
            int total = exactTimes.stream().mapToInt(Integer::intValue).sum();
            result.append(total).append(" time");
            if (total > 1) {
                result.append("s");
            }
        }

        if (!otherTimes.isEmpty()) {
            if (result.length() != 0) {
                result.append(" and");
            }

            otherTimes.forEach(other -> result.append(other).append(" and"));

            result.delete(result.length() - 4, result.length());
        }

        return result.toString();
    }
}
