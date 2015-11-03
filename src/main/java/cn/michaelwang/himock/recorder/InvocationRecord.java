package cn.michaelwang.himock.recorder;

import cn.michaelwang.himock.Utils;

import java.util.Arrays;

public class InvocationRecord {
    private String methodName;

    private Object returnValue;
    private Class<?> returnType;
    private StackTraceElement[] setReturnStackTrace;

    private Object[] args;

    private StackTraceElement[] stackTraceElements;

    InvocationRecord(String methodName, Class<?> returnType, Object[] args) {
        this.methodName = methodName;
        this.returnType = returnType;
        this.args = args;
        this.stackTraceElements = new Exception().getStackTrace();
    }

    public String getMethodName() {
        return methodName;
    }

    public Object getReturnValue() {
        return returnValue == null ? nullValue() : returnValue;
    }

    public void setReturnValue(Object toSet, Class<?> toSetType) {
        if (this.returnValue != null) {
            throw new ReturnValueAlreadySetException(this, toSet);
        }

        this.setReturnStackTrace = new Exception().getStackTrace();
        if (isSuitableType(toSet.getClass(), returnType)) {
            this.returnValue = toSet;
        } else if (returnType == Void.TYPE) {
            throw new NoReturnTypeException(this);
        } else {
            throw new ReturnTypeIsNotSuitableException(this, toSetType);
        }
    }

    public Object[] getParameters() {
        return args;
    }

    public String getSetReturnStackTrace() {
        return Utils.buildStackTraceInformation(setReturnStackTrace, "\t\t");
    }

    public String getInvocationRecordDetail() {
        return "\t\t" + getInvocationMessage() + "\n"
                + getInvocationStackTrace();
    }

    public String getInvocationStackTrace() {
        return Utils.buildStackTraceInformation(stackTraceElements, "\t\t");
    }

    public Class<?> getReturnType() {
        return returnType;
    }

    private String getInvocationMessage() {
        StringBuilder sb = new StringBuilder();
        sb.append(Utils.removeParenthesesInFunctionName(methodName));
        sb.append("(");
        if (args != null) {
            for (Object parameter : args) {
                sb.append(parameter);
                sb.append(", ");
            }
            sb.delete(sb.lastIndexOf(","), sb.length());
        }
        sb.append(")");
        return sb.toString();
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
        int hashCode = super.hashCode() + methodName.hashCode() + returnType.hashCode();
        for (Object arg : args) {
            hashCode += arg.hashCode();
        }

        return hashCode;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof InvocationRecord) {
            InvocationRecord toCompare = (InvocationRecord) obj;
            return methodName.equals(toCompare.methodName)
                    && returnType.equals(toCompare.returnType)
                    && Arrays.equals(args, toCompare.args);
        }

        return false;
    }
}
