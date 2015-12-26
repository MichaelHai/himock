package cn.michaelwang.himock.invocation;

import cn.michaelwang.himock.Invocation;
import cn.michaelwang.himock.NullObjectPlaceHolder;
import cn.michaelwang.himock.matcher.Matcher;
import cn.michaelwang.himock.verify.Verifiable;
import cn.michaelwang.himock.verify.Verification;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class InvocationImpl implements Verifiable, Verification {
    private int id;
    private String methodName;

    private Queue<Answer> returnValue = new LinkedList<>();
    private Answer lastAnswer;
    private Class<?> returnType;
    private List<Class<Throwable>> exceptionTypes;

    private Object[] args;
    private List<Matcher<?>> matchers;

    private StackTraceElement[] stackTraceElements;

    public InvocationImpl(int id, String methodName, Class<?> returnType, Object[] args, List<Class<Throwable>> exceptionTypes) {
        this.id = id;
        this.methodName = methodName;
        this.returnType = returnType;
        this.args = args == null ? new Object[0] : args;
        this.stackTraceElements = new Exception().getStackTrace();
        this.exceptionTypes = exceptionTypes;
    }

    public String getMethodName() {
        return methodName;
    }

    public Object getReturnValue() throws Throwable {
        if (!returnValue.isEmpty()) {
            lastAnswer = returnValue.poll();
        }

        return lastAnswer == null ? nullValue() : lastAnswer.doAnswer();
    }

    public void addReturnValue(Object toSet, Class<?> toSetType) throws NoReturnTypeException, ReturnTypeIsNotSuitableException {
        if (isSuitableType(toSet.getClass(), returnType)) {
            lastAnswer = new ReturnAnswer(toSet);
            returnValue.offer(lastAnswer);
        } else if (returnType == Void.TYPE) {
            throw new NoReturnTypeException(this);
        } else {
            throw new ReturnTypeIsNotSuitableException(this, toSetType);
        }
    }

    public void addException(Throwable toThrow) {
        if (toThrow instanceof RuntimeException
                || exceptionTypes.stream().anyMatch(exceptionType -> exceptionType.isAssignableFrom(toThrow.getClass()))) {
            lastAnswer = new ThrowAnswer(toThrow);
            returnValue.offer(lastAnswer);
        } else {
            throw new ExceptionTypeIsNotSuitableException(this, toThrow);
        }
    }

    public Object[] getParameters() {
        return args;
    }

    public StackTraceElement[] getInvocationStackTrace() {
        return stackTraceElements;
    }

    @Override
    public boolean sameMethod(Invocation invocation) {
        return invocation instanceof InvocationImpl
                && ((InvocationImpl) invocation).id == this.id
                && invocation.getMethodName().equals(this.getMethodName());

    }

    public Class<?> getReturnType() {
        return returnType;
    }

    public List<Class<Throwable>> getExceptionTypes() {
        return exceptionTypes;
    }

    public boolean isAllReturned() {
        return returnValue.isEmpty();
    }

    public void answerMore(int times) {
        for (int i = 0; i < times; i++) {
            returnValue.offer(lastAnswer);
        }
    }

    protected Object nullValue() {
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
                        || (thisTypeName.equals("Integer") && targetTypeName.equals("int"))) {
                    return true;
                }
            }
        }

        return false;
    }

    public void addArgumentMatchers(List<Matcher<?>> matchers) {
        this.matchers = matchers;
    }

    @Override
    public int hashCode() {
        int hashCode = super.hashCode() + id + methodName.hashCode() + returnType.hashCode();
        for (Object arg : args) {
            if (arg != null) {
                hashCode += arg.hashCode();
            }
        }

        return hashCode;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof InvocationImpl) {
            InvocationImpl toCompare = (InvocationImpl) obj;
            return id == toCompare.id
                    && methodName.equals(toCompare.methodName)
                    && returnType.equals(toCompare.returnType)
                    && checkArguments(toCompare);
        }

        return false;
    }

    @SuppressWarnings("unchecked")
    private boolean checkArguments(InvocationImpl toCompare) {
        int matchIndex = 0;
        for (int i = 0; i < args.length; i++) {
            Object thisArg = args[i];
            Object toCompareArg = toCompare.args[i];

            if (toCompareArg == NullObjectPlaceHolder.getInstance()) {
                if (!isNullValue(thisArg)) {
                    return false;
                }
            } else if (isNullValue(thisArg)) {
                Matcher<Object> matcher = (Matcher<Object>) matchers.get(matchIndex);
                matchIndex++;
                if (!matcher.isMatch(toCompareArg)) {
                    return false;
                }
            } else if (!thisArg.equals(toCompareArg)) {
                return false;
            }
        }

        return true;
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

    private boolean isPrimitiveOrBoxType(Class<?> type) {
        return type.isPrimitive() || type.equals(Byte.class) || type.equals(Character.class)
                || type.equals(Short.class) || type.equals(Integer.class) || type.equals(Long.class)
                || type.equals(Float.class) || type.equals(Double.class) || type.equals(Boolean.class);
    }

    @Override
    public boolean satisfyWith(Verifiable verifiable) {
        return this.equals(verifiable);
    }

    @Override
    public boolean satisfyWith(List<? extends Verifiable> toBeVerified) {
        return toBeVerified.contains(this) && this.isAllReturned();
    }

    interface Answer {
        Object doAnswer() throws Throwable;
    }

    class ReturnAnswer implements Answer {
        private Object returnValue;

        ReturnAnswer(Object returnValue) {
            this.returnValue = returnValue;
        }

        @Override
        public Object doAnswer() {
            return returnValue;
        }
    }

    class ThrowAnswer implements Answer {
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
