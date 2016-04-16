package cn.michaelwang.himock.preprocess;

import java.util.List;

public interface VariablesExtractor {
    List<VariableWithType> getAllLocalVariables();

    List<VariableWithType> getMembers();

    List<VariableWithType> getLocalVariablesIn(String aMethodWithLocalVariables);
}
