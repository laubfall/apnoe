package de.laubfall.apnoe;

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
    final CallHierachyResult childScope = new CallHierachyResult();
    arg.addLeaf(childScope);
    childScope.setScopeName(n.getNameAsString());
    childScope.setNode(n);
    n.getChildNodes().forEach(cn -> cn.accept(this, childScope));

    try {
      SymbolReference<ResolvedMethodDeclaration> symbolReference = JavaParserFacade.get(TypeSolverFactory.get().build())
          .solve(n);
      if (symbolReference != null && symbolReference.isSolved()) {
        ResolvedMethodDeclaration correspondingDeclaration = symbolReference.getCorrespondingDeclaration();
        if (correspondingDeclaration instanceof JavaParserMethodDeclaration) {
          JavaParserMethodDeclaration jpmd = (JavaParserMethodDeclaration) correspondingDeclaration;
          jpmd.getWrappedNode().accept(this, childScope);
        }
      }
    } catch (RuntimeException e) {
      System.out.println("unable to solve scope symbol " + n.getNameAsString());
    }

    return null;
  }

}
