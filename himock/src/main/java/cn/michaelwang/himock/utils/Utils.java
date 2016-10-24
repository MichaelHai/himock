package cn.michaelwang.himock.utils;

public class Utils {
    public static String removeParenthesesInFunctionName(String functionName) {
        if (functionName.endsWith("()")) {
            return functionName.substring(0, functionName.length() - 2);
        } else {
            return functionName;
        }
    }

    public static boolean isPrimitiveOrBoxType(Class<?> type) {
        return type.isPrimitive() || type.equals(Byte.class) || type.equals(Character.class)
                || type.equals(Short.class) || type.equals(Integer.class) || type.equals(Long.class)
                || type.equals(Float.class) || type.equals(Double.class) || type.equals(Boolean.class);
    }

    public static boolean isNameOfPrimitiveType(String type) {
        return "byte".equals(type) || "char".equals(type) || "short".equals(type)
                || "int".equals(type) || "long".equals(type) || "float".equals(type)
                || "double".equals(type) || "boolean".equals(type);
    }
}
