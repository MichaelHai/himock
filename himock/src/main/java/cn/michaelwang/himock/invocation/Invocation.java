package cn.michaelwang.himock.invocation;

import cn.michaelwang.himock.matcher.Matcher;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Invocation {
    private int id;
    private String methodName;

    private Queue<Answer> returnValue = new LinkedList<>();
    private Answer lastAnswer;
    private Class<?> returnType;
    private List<Class<Throwable>> exceptionTypes;

    private Object[] args;
    private Queue<Matcher<?>> matchers;

    private StackTraceElement[] stackTraceElements;

    public Invocation(int id, String methodName, Class<?> returnType, Object[] args, List<Class<Throwable>> exceptionTypes) {
        this.id = id;
        this.methodName = methodName;
        this.returnType = returnType;
        this.args = args;
        this.stackTraceElements = new Exception().getStackTrace();
        this.exceptionTypes = exceptionTypes;
    }

    public int getId() {
        return id;
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
        if (returnType.isPrimitive()) {
            if (returnType.equals(Boolean.TYPE)) {
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

    public void addArgumentMatchers(Queue<Matcher<?>> matchers) {
        this.matchers = matchers;
    }

    @Override
    public int hashCode() {
        int hashCode = super.hashCode() + id + methodName.hashCode() + returnType.hashCode();
        if (args != null) {
            for (Object arg : args) {
                hashCode += arg.hashCode();
            }
        }

        return hashCode;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Invocation) {
            Invocation toCompare = (Invocation) obj;
            return id == toCompare.id
                    && methodName.equals(toCompare.methodName)
                    && returnType.equals(toCompare.returnType)
                    && checkArguments(toCompare);
        }

        return false;
    }

    @SuppressWarnings("unchecked")
    private boolean checkArguments(Invocation toCompare) {
        for (int i = 0; i < args.length; i++) {
            Object thisArg = args[i];
            Object toCompareArg = toCompare.args[i];
            if (isNullValue(thisArg)) {
                Matcher<Object> matcher = (Matcher<Object>) matchers.poll();
                if (!matcher.isMatch(toCompareArg)) {
                    return false;
                }
            } else {
                if (!thisArg.equals(toCompareArg)) {
                    return false;
                }
            }
        }

        return true;
    }

    private boolean isNullValue(Object thisArg) {
        if (returnType.isPrimitive()) {
            if (returnType.equals(Boolean.TYPE)) {
                return thisArg.equals(false);
            }
            return thisArg.equals(0);
        }

        return thisArg == null;
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
