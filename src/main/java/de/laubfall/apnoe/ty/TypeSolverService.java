package de.laubfall.apnoe.ty;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;
import com.github.javaparser.symbolsolver.javaparsermodel.declarations.JavaParserMethodDeclaration;
import com.github.javaparser.symbolsolver.model.resolution.SymbolReference;

public final class TypeSolverService
{
  private static final Logger LOG = LogManager.getLogger();
  
  public final Optional<JavaParserMethodDeclaration> solveByMethodCallExpression(MethodCallExpr n)
  {
    var solver = JavaParserFacade.get(TypeSolverFactory.get().typeSolver());
    try {
      SymbolReference<ResolvedMethodDeclaration> symbolReference = solver.solve(n);
      if (symbolReference != null && symbolReference.isSolved()) {
        ResolvedMethodDeclaration correspondingDeclaration = symbolReference.getCorrespondingDeclaration();
        if (correspondingDeclaration instanceof JavaParserMethodDeclaration) {
          JavaParserMethodDeclaration jpmd = (JavaParserMethodDeclaration) correspondingDeclaration;
          return Optional.ofNullable(jpmd);
        }
      }
    } catch (RuntimeException e) {
      LOG.warn("unable to solve scope symbol " + n.getNameAsString());
    }
    
    return Optional.empty();
  }
}
