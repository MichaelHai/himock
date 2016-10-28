package cn.michaelwang.himock.process;

import cn.michaelwang.himock.Answer;
import cn.michaelwang.himock.Invocation;
import cn.michaelwang.himock.Matcher;

import java.util.List;

public interface Expectation {
    boolean match(Invocation invocation);

    void addReturnValue(Object returnValue, Class<?> type);

    void addException(Throwable toThrow);

    void answerMore(int i);
        
    Object getReturnValue(Object[] params) throws Throwable;
    
    Invocation getInvocation();

	List<Matcher<?>> getMatchers();

    boolean equals(Invocation invocation, List<Matcher<?>> matchers);

    void addAnswer(Answer answer);

    interface ExpectedAnswer {
        Object doAnswer(Object[] params) throws Throwable;
    }
}
