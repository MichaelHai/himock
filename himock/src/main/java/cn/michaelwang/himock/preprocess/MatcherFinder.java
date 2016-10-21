package cn.michaelwang.himock.preprocess;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.strobel.assembler.ir.attributes.AttributeNames;
import com.strobel.assembler.ir.attributes.LineNumberTableAttribute;
import com.strobel.assembler.ir.attributes.SourceAttribute;
import com.strobel.assembler.metadata.MethodDefinition;
import com.strobel.decompiler.languages.java.LineNumberTableConverter;
import com.strobel.decompiler.languages.java.OffsetToLineNumberConverter;
import com.strobel.decompiler.languages.java.ast.AssignmentExpression;
import com.strobel.decompiler.languages.java.ast.AstNode;
import com.strobel.decompiler.languages.java.ast.CastExpression;
import com.strobel.decompiler.languages.java.ast.CompilationUnit;
import com.strobel.decompiler.languages.java.ast.ConstructorDeclaration;
import com.strobel.decompiler.languages.java.ast.DepthFirstAstVisitor;
import com.strobel.decompiler.languages.java.ast.Expression;
import com.strobel.decompiler.languages.java.ast.InvocationExpression;
import com.strobel.decompiler.languages.java.ast.Keys;
import com.strobel.decompiler.languages.java.ast.LambdaExpression;
import com.strobel.decompiler.languages.java.ast.MethodDeclaration;

import cn.michaelwang.himock.IMatcherIndex;

public class MatcherFinder {
	private IMatcherIndex matcherIndex;
	private CompilationUnit ast;
	public SourceAttribute lineNumberTable;

	public MatcherFinder(Class<?> clazz, IMatcherIndex matcherIndex) {
		this.matcherIndex = matcherIndex;
		this.ast = ASTPool.getInstance().getAST(clazz);
	}

	public void find() {
		MatcherVisitor visitor = new MatcherVisitor(matcherIndex);
		ast.acceptVisitor(visitor, null);
	}

	class MatcherVisitor extends DepthFirstAstVisitor<Object, Object> {
		private IMatcherIndex matcherIndex;
		private Map<String, Integer> markIds = new HashMap<>();

		public MatcherVisitor(IMatcherIndex matcherIndex) {
			this.matcherIndex = matcherIndex;
		}

		private OffsetToLineNumberConverter converter;

		@Override
		public Object visitMethodDeclaration(MethodDeclaration node, Object data) {
			retrieveLineNumberTable(node);

			return super.visitMethodDeclaration(node, data);
		}

		@Override
		public Object visitConstructorDeclaration(ConstructorDeclaration node, Object data) {
			retrieveLineNumberTable(node);

			return super.visitConstructorDeclaration(node, data);
		}

		@Override
		public Object visitLambdaExpression(LambdaExpression node, Object data) {
			// retrieveLineNumberTable(node);
			//
			// return super.visitLambdaExpression(node, data);
			return null;
		}

		private void retrieveLineNumberTable(AstNode node) {
			MethodDefinition method = node.getUserData(Keys.METHOD_DEFINITION);
			LineNumberTableAttribute lineNumberTable = SourceAttribute.find(
					AttributeNames.LineNumberTable,
					method != null ? method.getSourceAttributes()
							: Collections.<SourceAttribute> emptyList());
			if (lineNumberTable != null) {
				converter = new LineNumberTableConverter(lineNumberTable);
			}
		}

		@Override
		public Object visitInvocationExpression(InvocationExpression node, Object data) {
			int offset = node.getOffset();
			if (offset < 0) {
				offset = 0;
			}
			int lineNumber = converter.getLineForOffset(offset);

			String name = node.getTarget().getText();
			if (name.startsWith("HiMock.")) {
				name = name.substring(7);
				if (NameRepository.matchers.contains(name)) {
					return markMatcher(node, lineNumber);
				}
			} else {
				boolean hasMatcher = useMatcher(node, lineNumber, name);
				if (hasMatcher) {
					return null;
				}
			}

			return super.visitInvocationExpression(node, data);
		}

		private Object markMatcher(InvocationExpression node, int lineNumber) {
			String mark = "anonymous";

			AstNode nonCastExpressionNode = node.getParent();
			if (nonCastExpressionNode instanceof CastExpression) {
				nonCastExpressionNode = nonCastExpressionNode.getParent();
			}

			if (nonCastExpressionNode instanceof AssignmentExpression) {
				mark = ((AssignmentExpression) nonCastExpressionNode).getLeft().getText();
				mark = cutToFunctionName(mark);
			} else if (node.getParent() instanceof CastExpression) {

			}

			int id = markIds.getOrDefault(mark, 0);
			String markWithId = mark + id;
			matcherIndex.markMatcher(lineNumber, markWithId);

			id++;
			markIds.put(mark, id);

			return markWithId;
		}

		private boolean useMatcher(InvocationExpression node, int lineNumber, String name) {
			name = cutToFunctionName(name);
			Iterator<Expression> arguments = node.getArguments().iterator();
			List<String> marks = new ArrayList<>();
			boolean hasMatcher = false;
			while (arguments.hasNext()) {
				Expression arg = arguments.next();

				if (arg instanceof CastExpression) {
					arg = ((CastExpression) arg).getExpression();
				}

				if (arg instanceof InvocationExpression) {
					Object result = this.visitInvocationExpression((InvocationExpression) arg, null);
					if (result != null) {
						marks.add(result.toString());
						hasMatcher = true;
					}
				} else {
					String matcherMark = arg.getText();
					matcherMark = cutToFunctionName(matcherMark);
					if (markIds.containsKey(matcherMark)) {
						int id = markIds.get(matcherMark);
						marks.add(matcherMark + (id - 1));
						hasMatcher = true;
					} else {
						marks.add(null);
					}
				}
			}

			if (hasMatcher) {
				String[] args = new String[marks.size()];
				marks.toArray(args);
				matcherIndex.useMatcher(lineNumber, name, args);
				return true;
			}

			return false;
		}

		private String cutToFunctionName(String name) {
			return name.substring(name.lastIndexOf(".") + 1);
		}

	}
}
