package cn.michaelwang.himock.invocation;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

public class Invocation {
    private int id;
    private String methodName;

    private Queue<Object> returnValue = new LinkedList<>();
    private Class<?> returnType;
    private StackTraceElement[] setReturnStackTrace;

    private Object[] args;

    private StackTraceElement[] stackTraceElements;

    public Invocation(int id, String methodName, Class<?> returnType, Object[] args) {
        this.id = id;
        this.methodName = methodName;
        this.returnType = returnType;
        this.args = args;
        this.stackTraceElements = new Exception().getStackTrace();
    }

    public int getId() {
        return id;
    }

    public String getMethodName() {
        return methodName;
    }

    public Object getReturnValue() {
        return returnValue.isEmpty() ? nullValue() : returnValue.poll();
    }

    public void addReturnValue(Object toSet, Class<?> toSetType) {
        this.setReturnStackTrace = new Exception().getStackTrace();
        if (isSuitableType(toSet.getClass(), returnType)) {
            returnValue.offer(toSet);
        } else if (returnType == Void.TYPE) {
            throw new NoReturnTypeException(this);
        } else {
            throw new ReturnTypeIsNotSuitableException(this, toSetType);
        }
    }

    public Object[] getParameters() {
        return args;
    }

    public StackTraceElement[] getSetReturnStackTrace() {
        return setReturnStackTrace;
    }

    public StackTraceElement[] getInvocationStackTrace() {
        return stackTraceElements;
    }

    public Class<?> getReturnType() {
        return returnType;
    }

    public boolean isAllReturned() {
        return returnValue.isEmpty();
    }

    private Object nullValue() {
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
                    && Arrays.equals(args, toCompare.args);
        }

        return false;
    }
}
