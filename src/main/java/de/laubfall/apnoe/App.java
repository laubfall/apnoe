package de.laubfall.apnoe;

import java.io.IOException;
import java.util.List;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.resolution.UnsolvedSymbolException;
import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;
import com.github.javaparser.symbolsolver.javaparsermodel.declarations.JavaParserMethodDeclaration;
import com.github.javaparser.symbolsolver.model.resolution.SymbolReference;

import de.laubfall.apnoe.hie.CallHierachyResultPrinter;
import de.laubfall.apnoe.hie.HierarchyAnalyzerService;

/**
 * Sample class that demonstrate the function of the source analyzer service. The result is printed to the console.
 * 
 * @author Daniel
 *
 */
public class App extends AbstractApnoeApp
{
  public static void main(String[] args) throws IOException
  {
    initTypeSolver(args);

    App a = new App();
    a.start(argEntryPointSrc(args), argEntryPointMethodName(args));
  }

  void start(String pathToEntryPointSourceFile, String entryPointMethodName)
  {
    final HierarchyAnalyzerService has = new HierarchyAnalyzerService();
    new CallHierachyResultPrinter().printToSyso(has.analyze(pathToEntryPointSourceFile, entryPointMethodName));

    //    StringBuilder callBuilder = new StringBuilder();
    //    recMethodCalls(entryPoint.get(), callBuilder);
    //    System.out.println(callBuilder.toString());
  }

  private void recMethodCalls(final MethodDeclaration methDec, StringBuilder callBuilder)
  {
    // find all method calls inside the entry point ;
    final List<MethodCallExpr> methodCalls = methDec.findAll(MethodCallExpr.class);
    if (methodCalls.isEmpty()) {
      callBuilder.append("\n");
      return;
    }

    callBuilder.append("->");

    for (MethodCallExpr methodCallExpr : methodCalls) {
      SymbolReference<ResolvedMethodDeclaration> typeDeclaration = null;
      try {
        typeDeclaration = JavaParserFacade.get(TYPE_SOLVER).solve(methodCallExpr);
      } catch (UnsolvedSymbolException e) {
        System.err.println("unable to resolve declaring type of: " + methodCallExpr.getNameAsString());
      } catch (RuntimeException e) {
        System.err.println("failed to resolve declaring type of: " + methodCallExpr.getNameAsString());
      }

      if (typeDeclaration != null && typeDeclaration.isSolved()) {
        callBuilder.append(typeDeclaration.getCorrespondingDeclaration().declaringType().getName()).append(".");
      }

      callBuilder.append(methodCallExpr.getNameAsString());
      callBuilder.append("(");
      if (methodCallExpr.getArguments().isNonEmpty()) {
        methodCallExpr.getArguments().forEach(expr -> callBuilder.append(expr));
      }
      callBuilder.append(")");

      if (typeDeclaration != null && typeDeclaration.isSolved()) {
        ResolvedMethodDeclaration correspondingDeclaration = typeDeclaration.getCorrespondingDeclaration();
        if (correspondingDeclaration instanceof JavaParserMethodDeclaration) {
          JavaParserMethodDeclaration jpmd = (JavaParserMethodDeclaration) correspondingDeclaration;
          recMethodCalls(jpmd.getWrappedNode(), callBuilder);
        }
      }
    }

    return;
  }
}
