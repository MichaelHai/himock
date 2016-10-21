package cn.michaelwang.himock.preprocess;

import com.strobel.decompiler.languages.java.ast.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ASTVisitorVariablesExtractor extends DepthFirstAstVisitor<Object, Object> implements VariablesExtractor {
	private List<VariableWithType> allVariables = new ArrayList<>();
	private List<VariableWithType> members = new ArrayList<>();
	private Map<String, List<VariableWithType>> localVariables = new HashMap<>();

	private String currentMethod = null;

	public ASTVisitorVariablesExtractor(Class<?> clazz) {
		CompilationUnit ast = ASTPool.getInstance().getAST(clazz);
		ast.acceptVisitor(this, null);
	}

	@Override
	public List<VariableWithType> getAllLocalVariables() {
		return allVariables;
	}

	@Override
	public List<VariableWithType> getMembers() {
		return members;
	}

	@Override
	public List<VariableWithType> getLocalVariablesIn(String aMethodWithLocalVariables) {
		return localVariables.get(aMethodWithLocalVariables);
	}

	@Override
	public Object visitMethodDeclaration(MethodDeclaration node, Object data) {
		currentMethod = node.getName();
		localVariables.put(currentMethod, new ArrayList<>());
		return super.visitMethodDeclaration(node, data);
	}

	@Override
	public Object visitVariableDeclaration(VariableDeclarationStatement node, Object data) {
		VariableType type = VariableType.convertToType(node.getType().getText());
		node.getVariables().forEach(variableInitializer -> {
			String variable = variableInitializer.getName();
			VariableWithType variableWithType = new VariableWithType(variable, type);
			allVariables.add(variableWithType);
			localVariables.get(currentMethod).add(variableWithType);
		});

		return super.visitVariableDeclaration(node, data);
	}

	@Override
	public Object visitFieldDeclaration(FieldDeclaration node, Object data) {
		VariableType type = VariableType.convertToType(node.getReturnType().getText());
		node.getVariables().forEach(variableInitializer -> {
			String variable = variableInitializer.getName();
			if (!variable.startsWith("this$")) {
				VariableWithType variableWithType = new VariableWithType(variable, type);
				members.add(variableWithType);
			}
		});
		return super.visitFieldDeclaration(node, data);
	}
}
