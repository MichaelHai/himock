package cn.michaelwang.himock.preprocess;

import cn.michaelwang.himock.utils.Utils;

public enum VariableType {
    OBJECT, PRIMITIVE;

    public static VariableType convertToType(String type) {
        if (Utils.isNameOfPrimitiveType(type)) {
            return PRIMITIVE;
        } else {
            return OBJECT;
        }
    }
}
