package cn.michaelwang.himock.recorder;

import java.util.Arrays;

public class InvocationRecord {
    private String invocation;

    private Object returnValue;
    private Class<?> returnType;

    private Class<?>[] parameterTypes;
    private Object[] args;

    InvocationRecord(String invocation, Class<?> returnType, Class<?>[] parameterTypes, Object[] args) {
        this.invocation = invocation;
        this.returnType = returnType;
        this.parameterTypes = parameterTypes;
        this.args = args;
    }

    public String getInvocation() {
        return invocation;
    }

    public Object getReturnValue() {
        return returnValue == null ? nullValue() : returnValue;
    }

    public void setReturnValue(Object returnValue) throws ReturnValueAlreadySetException, ReturnTypeIsNotSuitableException {
        if (this.returnValue != null) {
            throw new ReturnValueAlreadySetException();
        }

        if (isSuitableType(returnValue.getClass(), returnType)) {
            this.returnValue = returnValue;
        } else {
            throw new ReturnTypeIsNotSuitableException();
        }
    }

    public Object[] getParameters() {
        return args;
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
        return super.hashCode() + invocation.hashCode() + returnType.hashCode()
                + args.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof InvocationRecord) {
            InvocationRecord toCompare = (InvocationRecord) obj;
            return invocation.equals(toCompare.invocation)
                    && returnType.equals(toCompare.returnType)
                    && Arrays.equals(args, toCompare.args);
        }

        return false;
    }
}
