package cn.michaelwang.himock.verify;

import cn.michaelwang.himock.Invocation;
import cn.michaelwang.himock.NullObjectPlaceHolder;
import cn.michaelwang.himock.Verification;
import cn.michaelwang.himock.invocation.ExceptionTypeIsNotSuitableException;
import cn.michaelwang.himock.invocation.NoReturnTypeException;
import cn.michaelwang.himock.invocation.ReturnTypeIsNotSuitableException;
import cn.michaelwang.himock.Matcher;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;

public class VerificationImpl implements Verification {
    private Invocation invocation;

    private Queue<Answer> returnValue = new LinkedList<>();
    private Answer lastAnswer;

    private List<Matcher<?>> matchers;

    public VerificationImpl(Invocation invocation) {
        this.invocation = invocation;
    }

    public VerificationImpl(Invocation invocation, List<Matcher<?>> matchers) {
        this(invocation);
        this.matchers = matchers;
    }

    @Override
    public Invocation getVerifiedInvocation() {
        return invocation;
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
    public boolean satisfyWith(Invocation invocation) {
        return this.invocation.sameMethod(invocation) && checkArguments(invocation);
    }

    @Override
    public boolean satisfyWith(List<Invocation> toBeVerified) {
        Optional<Invocation> invocation = toBeVerified.stream()
                .filter(this::satisfyWith)
                .findFirst();
        return invocation.isPresent() && isAllReturned();
    }

    protected Object nullValue() {
        Class<?> returnType = invocation.getReturnType();
        if (isPrimitiveOrBoxType(returnType)) {
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

    private boolean isPrimitiveOrBoxType(Class<?> type) {
        return type.isPrimitive() || type.equals(Byte.class) || type.equals(Character.class)
                || type.equals(Short.class) || type.equals(Integer.class) || type.equals(Long.class)
                || type.equals(Float.class) || type.equals(Double.class) || type.equals(Boolean.class);
    }

    private boolean isNullValue(Object thisArg) {
        if (thisArg == null) {
            return true;
        }

        Class<?> type = thisArg.getClass();
        if (isPrimitiveOrBoxType(type)) {
            if (type.equals(Boolean.TYPE) || type.equals(Boolean.class)) {
                return thisArg.equals(false);
            }
            return thisArg.equals(0);
        }

        return false;
    }

    @SuppressWarnings("unchecked")
    private boolean checkArguments(Invocation toCompare) {
        Object[] args = invocation.getArguments();

        int matcherIndex = 0;
        for (int i = 0; i < args.length; i++) {
            Object thisArg = args[i];
            Object toCompareArg = toCompare.getArguments()[i];

            if (toCompareArg == NullObjectPlaceHolder.getInstance()) {
                if (!isNullValue(thisArg)) {
                    return false;
                }
            } else {
                if (isNullValue(thisArg)) {
                    Matcher<Object> matcher = (Matcher<Object>) matchers.get(matcherIndex);
                    matcherIndex++;
                    if (!matcher.isMatch(toCompareArg)) {
                        return false;
                    }
                } else if (!thisArg.equals(toCompareArg)) {
                    return false;
                }
            }
        }

        return true;
    }

    private boolean isAllReturned() {
        return returnValue.isEmpty();
    }

    private interface Answer {
        Object doAnswer() throws Throwable;
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
