package de.laubfall.apnoe;

import java.util.Optional;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;
import com.github.javaparser.symbolsolver.javaparsermodel.declarations.JavaParserMethodDeclaration;
import com.github.javaparser.symbolsolver.model.resolution.SymbolReference;

public class CallHierachyVisitor extends CallHierachyVisitorAdapter
{

  @Override
  public Void visit(MethodCallExpr n, CallHierachyResult arg)
  {

    var childScope = methodCallExpressionScope(n);
    arg.addLeaf(childScope);

    n.getChildNodes().stream().filter(cn -> childScope.findCallHierachyByNode(cn) == null)
        .forEach(cn -> cn.accept(this, childScope));

    var solver = JavaParserFacade.get(TypeSolverFactory.get().build());
    try {
      SymbolReference<ResolvedMethodDeclaration> symbolReference = solver.solve(n);
      if (symbolReference != null && symbolReference.isSolved()) {
        ResolvedMethodDeclaration correspondingDeclaration = symbolReference.getCorrespondingDeclaration();
        if (correspondingDeclaration instanceof JavaParserMethodDeclaration) {
          JavaParserMethodDeclaration jpmd = (JavaParserMethodDeclaration) correspondingDeclaration;
          jpmd.getWrappedNode().accept(this, childScope);
        }
      }
    } catch (RuntimeException e) {
      System.out.println("unable to solve scope symbol " + n.getNameAsString());
      //      List<ResolvedReferenceType> ancestors = solver.getTypeDeclaration(n).getAncestors();
      //      System.out.println(ancestors);
    }

    return null;
  }

  private CallHierachyResult methodCallExpressionScope(MethodCallExpr methCallExpr)
  {
    Expression scopeExpr = scopeExpression(methCallExpr);
    CallHierachyResult res = new CallHierachyResult();
    res.setNode(methCallExpr);
    res.setScopeName(methCallExpr.getNameAsString());
    while (scopeExpr != null && scopeExpr instanceof MethodCallExpr) {
      final CallHierachyResult scope = new CallHierachyResult();
      final MethodCallExpr methodCallInScope = (MethodCallExpr) scopeExpr;
      scope.setNode(methodCallInScope);
      scope.setScopeName(methodCallInScope.getNameAsString());
      scope.addLeaf(res);
      res = scope;
      scopeExpr = scopeExpression(methodCallInScope);
    }

    return res;
  }

  private Expression scopeExpression(MethodCallExpr expr)
  {
    Expression scopeExpr = null;
    Optional<Expression> scope = expr.getScope();
    if (scope.isPresent()) {
      scopeExpr = scope.get();
    }
    return scopeExpr;
  }
}
