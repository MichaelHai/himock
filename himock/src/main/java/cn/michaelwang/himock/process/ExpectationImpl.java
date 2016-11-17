package cn.michaelwang.himock.process;

import cn.michaelwang.himock.Answer;
import cn.michaelwang.himock.HitNeverTimerException;
import cn.michaelwang.himock.Invocation;
import cn.michaelwang.himock.Matcher;
import cn.michaelwang.himock.process.exceptions.ExceptionTypeIsNotSuitableException;
import cn.michaelwang.himock.process.exceptions.NoReturnTypeException;
import cn.michaelwang.himock.process.exceptions.ReturnTypeIsNotSuitableException;
import cn.michaelwang.himock.process.timer.TimerCheckerImpl;
import cn.michaelwang.himock.utils.Utils;

import java.util.*;

public class ExpectationImpl implements Expectation {
    private Queue<ExpectedAnswer> returnValue = new LinkedList<>();
    private Map<ExpectedAnswer, TimerChecker> answerTimerMap = new HashMap<>();
    // the last answer set
    private ExpectedAnswer lastAnswer;
    // the current answer returned for mocking invocation
    private ExpectedAnswer currentAnswer;

    private Invocation invocation;
    private Matchers matchers;

    public ExpectationImpl(Invocation invocation, List<Matcher<?>> matchers) {
        this.invocation = invocation;
        this.matchers = new Matchers(matchers);
        // the default timer checker
        answerTimerMap.put(null, new TimerCheckerImpl());
    }

    @Override
    public boolean match(Invocation invocation) {
        return this.invocation.sameMethod(invocation)
                && matchers.match(invocation.getArguments());
    }

    @Override
    public void addReturnValue(Object toSet, Class<?> toSetType)
            throws NoReturnTypeException, ReturnTypeIsNotSuitableException {
        Class<?> returnType = invocation.getReturnType();
        if (isSuitableType(toSet.getClass(), returnType)) {
            lastAnswer = new ReturnAnswer(toSet);
            returnValue.offer(lastAnswer);
            answerTimerMap.put(lastAnswer, new TimerCheckerImpl());
        } else if (returnType == Void.TYPE) {
            throw new NoReturnTypeException(invocation);
        } else {
            throw new ReturnTypeIsNotSuitableException(invocation, toSetType);
        }
    }

    @Override
    public void addException(Throwable toThrow) {
        List<Class<Throwable>> exceptionTypes = invocation.getExceptionTypes();
        if (toThrow instanceof RuntimeException
                || exceptionTypes
                .stream()
                .anyMatch(exceptionType -> exceptionType.isAssignableFrom(toThrow.getClass()))) {
            lastAnswer = new ThrowAnswer(toThrow);
            returnValue.offer(lastAnswer);
            answerTimerMap.put(lastAnswer, new TimerCheckerImpl());
        } else {
            throw new ExceptionTypeIsNotSuitableException(invocation, toThrow);
        }
    }

    @Override
    public void answerMore(Timer timer) {
        TimerChecker checker = answerTimerMap.get(lastAnswer);
        checker.addTimer(timer);
    }

    @Override
    public Object getReturnValue(Object[] params) throws Throwable {
        if (currentAnswer == null) {
            currentAnswer = returnValue.poll();
        }

        if (currentAnswer == null || answerTimerMap.get(currentAnswer).check()) {
            if (!returnValue.isEmpty()) {
                currentAnswer = returnValue.poll();
            }
        }

        try {
            answerTimerMap.get(currentAnswer).hit();
        } catch (HitNeverTimerException ignore) {
            // ignore this exception when execute the product code
        }
        if (currentAnswer != null) {
            return currentAnswer.doAnswer(params);
        } else {
            return nullValue();
        }
    }

    @Override
    public Invocation getInvocation() {
        return invocation;
    }

    @Override
    public List<Matcher<?>> getMatchers() {
        return matchers.getMatchers();
    }

    @Override
    public void addAnswer(Answer answer) {
        lastAnswer = new DelegatedAnswer(answer);
        returnValue.offer(lastAnswer);
        answerTimerMap.put(lastAnswer, new TimerCheckerImpl());
    }

    @Override
    public boolean equals(Invocation invocation, List<Matcher<?>> matchers) {
        return this.invocation.sameMethod(invocation)
                && this.matchers.getMatchers().equals(matchers);
    }

    protected Object nullValue() {
        Class<?> returnType = invocation.getReturnType();
        return Utils.nullValue(returnType);
    }

    private boolean isSuitableType(Class<?> thisType, Class<?> targetType) {
        if (targetType.isAssignableFrom(thisType)) {
            return true;
        } else if (targetType.isPrimitive()) {
            String thisTypeName = thisType.getName();
            String targetTypeName = targetType.getName();

            if (thisTypeName.startsWith("java.lang.")) {
                thisTypeName = thisTypeName.substring("java.lang.".length());
                if (thisTypeName.equalsIgnoreCase(targetTypeName)
                        || ("Integer".equals(thisTypeName) && "int".equals(targetTypeName))
                        || ("Character".equals(thisTypeName) && "char".equals(targetTypeName))) {
                    return true;
                }
            }
        }

        return false;
    }

    private class ReturnAnswer implements ExpectedAnswer {
        private Object returnValue;

        ReturnAnswer(Object returnValue) {
            this.returnValue = returnValue;
        }

        @Override
        public Object doAnswer(Object[] params) {
            return returnValue;
        }
    }

    private class ThrowAnswer implements ExpectedAnswer {
        private Throwable toThrow;

        ThrowAnswer(Throwable toThrow) {
            this.toThrow = toThrow;
        }

        @Override
        public Object doAnswer(Object[] params) throws Throwable {
            throw toThrow;
        }
    }

    private class DelegatedAnswer implements ExpectedAnswer {
        private Answer answer;

        public DelegatedAnswer(Answer answer) {
            this.answer = answer;
        }

        @Override
        public Object doAnswer(Object[] params) throws Throwable {
            return answer.answer(params);
        }
    }
}
