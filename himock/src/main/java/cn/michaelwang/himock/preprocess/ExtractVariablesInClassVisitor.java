package cn.michaelwang.himock.preprocess;

import com.strobel.decompiler.languages.java.ast.DepthFirstAstVisitor;
import com.strobel.decompiler.languages.java.ast.VariableDeclarationStatement;

import java.util.ArrayList;
import java.util.List;

public class ExtractVariablesInClassVisitor extends DepthFirstAstVisitor<Object, Object> {
    private List<VariableWithType> allVariables = new ArrayList<>();

    public List<VariableWithType> getAllVariables() {
        return allVariables;
    }

    @Override
    public Object visitVariableDeclaration(VariableDeclarationStatement node, Object data) {
        VariableType type = VariableType.convertToType(node.getType().getText());
        node.getVariables().forEach(variableInitializer -> {
            String variable = variableInitializer.getName();
            VariableWithType variableWithType = new VariableWithType(variable, type);
            allVariables.add(variableWithType);
        });

        return super.visitVariableDeclaration(node, data);
    }
}
