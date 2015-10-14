package cn.michaelwang.himock.recorder;

public class InvocationRecord {
    private String invocation;

    private Object returnValue;
    private Class<?> returnType;

    InvocationRecord(String invocation, Class<?> returnType) {
        this.invocation = invocation;
        this.returnType = returnType;
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
        return super.hashCode() + invocation.hashCode() + returnType.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof InvocationRecord) {
            InvocationRecord toCompare = (InvocationRecord) obj;
            return invocation.equals(toCompare.invocation)
                    && returnType.equals(toCompare.returnType);
        }

        return false;
    }
}
