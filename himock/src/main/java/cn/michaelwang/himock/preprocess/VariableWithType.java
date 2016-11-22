package cn.michaelwang.himock.preprocess;

public class VariableWithType {
    private final VariableType type;
    private final String name;

    public VariableWithType(String name, VariableType type) {
        this.name = name;
        this.type = type;
    }

    public VariableType getType() {
        return type;
    }

    public String getName() {
        return name;
    }
}