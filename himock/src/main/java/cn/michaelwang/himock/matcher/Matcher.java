package cn.michaelwang.himock.matcher;

@FunctionalInterface
public interface Matcher<T> {
    boolean isMatch(T actual);
}