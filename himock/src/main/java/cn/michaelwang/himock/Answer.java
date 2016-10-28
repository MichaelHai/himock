package cn.michaelwang.himock;

@FunctionalInterface
public interface Answer {
    Object answer(Object... params);
}
