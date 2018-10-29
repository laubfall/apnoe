package de.laubfall.apnoe.hie;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;
import com.github.javaparser.symbolsolver.javaparsermodel.declarations.JavaParserMethodDeclaration;
import com.github.javaparser.symbolsolver.model.resolution.SymbolReference;

/**
 * This visitor is specialized for analyzing the call hierarchy of methods. Method calls and statements that modifies
 * call hierarchy (like if-Statement) are logged inside the {@link CallHierarchyNode}.
 * 
 * @author Daniel
 *
 */
public class CallHierachyVisitor extends CallHierachyVisitorAdapter
{
  private static final Logger LOG = LogManager.getLogger();

  @Override
  public Void visit(MethodCallExpr n, CallHierarchyNode arg)
  {

    var childScope = methodCallExpressionScope(n);
    arg.addLeaf(childScope);

    n.getChildNodes().stream().filter(cn -> childScope.findCallHierachyByNode(cn) == null)
        .forEach(cn -> cn.accept(this, childScope));

    var solver = JavaParserFacade.get(TypeSolverFactory.get().typeSolver());
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
      LOG.warn("unable to solve scope symbol " + n.getNameAsString());
    }

    return null;
  }

  @Override
  public Void visit(IfStmt n, CallHierarchyNode arg)
  {
    IfElseNode childScope = new IfElseNode();
    childScope.setNode(n);
    childScope.setScopeName("if");
    arg.addLeaf(childScope);
    n.getThenStmt().accept(this, childScope);

    final Optional<Statement> elseStmtOpt = n.getElseStmt();
    if (elseStmtOpt.isPresent()) {
      ifElseIfScope(n, arg, childScope);

      final Statement elseStmt = elseStmtOpt.get();
      if (elseStmt instanceof IfStmt == false) {
        IfElseNode elseChildScope = new IfElseNode();
        elseChildScope.setNode(elseStmtOpt.get());
        elseChildScope.setScopeName("else");
        arg.addLeaf(elseChildScope);
        childScope.addSuccessor(elseChildScope);
        elseStmt.accept(this, elseChildScope);
      }
    }

    return null;
  }

  @Override
  public Void visit(AssignExpr n, CallHierarchyNode arg)
  {
    // TODO maybe we have to create a hierarchyResult for that (e.g. an assignment inside an if-else-block
    return super.visit(n, arg);
  }

  private CallHierarchyNode ifElseIfScope(IfStmt ifStatement, CallHierarchyNode parent, IfElseNode rootNode)
  {
    ifStatement.getElseStmt().ifPresent(elseStmt -> {
      if(elseStmt instanceof IfStmt) {
        final IfElseNode chr = new IfElseNode();
        chr.setScopeName("else if");
        chr.setNode(elseStmt);
        parent.addLeaf(chr);
        rootNode.addSuccessor(chr);
        final IfStmt thenBlockOfElse = (IfStmt) elseStmt;
        ifElseIfScope(thenBlockOfElse, parent, rootNode);
        
        thenBlockOfElse.getThenStmt().accept(this, chr);
      }
    });
    return null;
  }

  private CallHierarchyNode methodCallExpressionScope(MethodCallExpr methCallExpr)
  {
    Expression scopeExpr = scopeExpression(methCallExpr);
    CallHierarchyNode res = new CallHierarchyNode();
    res.setNode(methCallExpr);
    res.setScopeName(methCallExpr.getNameAsString());
    while (scopeExpr != null && scopeExpr instanceof MethodCallExpr) {
      final CallHierarchyNode scope = new CallHierarchyNode();
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
