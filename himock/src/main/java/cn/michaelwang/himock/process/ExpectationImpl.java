package cn.michaelwang.himock.process;

import cn.michaelwang.himock.Invocation;
import cn.michaelwang.himock.invocation.ExceptionTypeIsNotSuitableException;
import cn.michaelwang.himock.invocation.NoReturnTypeException;
import cn.michaelwang.himock.invocation.ReturnTypeIsNotSuitableException;
import cn.michaelwang.himock.utils.Utils;
import cn.michaelwang.himock.verify.Verification;

import java.util.*;

public class ExpectationImpl implements Verification, Expectation {
    private Invocation invocation;

    private Queue<Answer> returnValue = new LinkedList<>();
    private Answer lastAnswer;

    public ExpectationImpl(Invocation invocation) {
        this.invocation = invocation;
    }

    @Override
    public Invocation getVerifiedInvocation() {
        return invocation;
    }

    @Override
    public boolean match(Invocation invocation) {
        return this.invocation.sameMethod(invocation)
                && Arrays.equals(this.invocation.getArguments(), invocation.getArguments());
    }

    @Override
    public void addReturnValue(Object toSet, Class<?> toSetType) throws NoReturnTypeException, ReturnTypeIsNotSuitableException {
        Class<?> returnType = invocation.getReturnType();
        if (isSuitableType(toSet.getClass(), returnType)) {
            lastAnswer = new ReturnAnswer(toSet);
            returnValue.offer(lastAnswer);
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
                || exceptionTypes.stream().anyMatch(exceptionType -> exceptionType.isAssignableFrom(toThrow.getClass()))) {
            lastAnswer = new ThrowAnswer(toThrow);
            returnValue.offer(lastAnswer);
        } else {
            throw new ExceptionTypeIsNotSuitableException(invocation, toThrow);
        }
    }

    @Override
    public void answerMore(int times) {
        for (int i = 0; i < times; i++) {
            returnValue.offer(lastAnswer);
        }
    }

    @Override
    public Object getReturnValue() throws Throwable {
        if (!returnValue.isEmpty()) {
            lastAnswer = returnValue.poll();
        }

        return lastAnswer == null ? nullValue() : lastAnswer.doAnswer();
    }

    @Override
    public Verification generateVerification() {
        return this;
    }

    @Override
    public boolean satisfyWith(Invocation invocation) {
        return this.match(invocation)
                && isAllReturned();
    }

    protected Object nullValue() {
        Class<?> returnType = invocation.getReturnType();
        if (Utils.isPrimitiveOrBoxType(returnType)) {
            if (returnType.equals(Boolean.TYPE) || returnType.equals(Boolean.class)) {
                return false;
            }
            return 0;
        }

        return null;
    }

    private boolean isSuitableType(Class<?> thisType, Class<?> targetType) {
        if (targetType.isAssignableFrom(thisType)) {
            return true;
        } else if (targetType.isPrimitive()) {
            String thisTypeName = thisType.getName();
            String targetTypeName = targetType.getName();

            if (thisTypeName.startsWith("java.lang.")) {
                thisTypeName = thisTypeName.substring("java.lang.".length());
                if (thisTypeName.toLowerCase().equals(targetTypeName)
                        || (thisTypeName.equals("Integer") && targetTypeName.equals("int"))
                        || (thisTypeName.equals("Character") && targetTypeName.equals("char"))) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean isAllReturned() {
        return returnValue.isEmpty();
    }

    private class ReturnAnswer implements Answer {
        private Object returnValue;

        ReturnAnswer(Object returnValue) {
            this.returnValue = returnValue;
        }

        @Override
        public Object doAnswer() {
            return returnValue;
        }
    }

    private class ThrowAnswer implements Answer {
        private Throwable toThrow;

        ThrowAnswer(Throwable toThrow) {
            this.toThrow = toThrow;
        }

        @Override
        public Object doAnswer() throws Throwable {
            throw toThrow;
        }
    }
}
