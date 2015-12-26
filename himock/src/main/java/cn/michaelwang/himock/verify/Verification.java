package cn.michaelwang.himock.verify;

import cn.michaelwang.himock.Invocation;
import cn.michaelwang.himock.matcher.Matcher;

import java.util.List;

public interface Verification extends Invocation {
    boolean satisfyWith(Invocation invocation);
    boolean satisfyWith(List<Invocation> toBeVerified);

    Object getReturnValue() throws Throwable;

    void addArgumentMatchers(List<Matcher<?>> matchers);

    void addReturnValue(Object returnValue, Class<?> type);
    void addException(Throwable toThrow);

    void answerMore(int i);
}
