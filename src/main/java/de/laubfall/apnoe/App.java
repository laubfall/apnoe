package de.laubfall.apnoe;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.resolution.UnsolvedSymbolException;
import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;
import com.github.javaparser.symbolsolver.javaparsermodel.declarations.JavaParserMethodDeclaration;
import com.github.javaparser.symbolsolver.model.resolution.SymbolReference;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;

import de.laubfall.apnoe.hie.CallHierarchyNode;
import de.laubfall.apnoe.hie.CallHierachyResultPrinter;
import de.laubfall.apnoe.hie.CallHierachyVisitor;
import de.laubfall.apnoe.hie.HierarchyAnalyzerService;
import de.laubfall.apnoe.hie.TypeSolverFactory;

public class App
{
  private static CombinedTypeSolver TYPE_SOLVER;

  /**
   * Colon separated list of paths to jar files that contains symbols we may need to resolve.
   */
  private static final String ARG_JAR_DEPS = "-DjarDeps=";

  /**
   * Colon separated list of paths that contains source files. Added to the type solver, required for resolving symbols
   * used by the entry point src (defined with arg {@link #ARG_ENTRY_POINT_SRC}).
   */
  private static final String ARG_SOURCE_PATHS = "-DsourcePaths=";

  /**
   * Path to the source file that represents one entry point into your application. This source file is parsed by
   * javaparser and analyzed for all call branches.
   */
  private static final String ARG_ENTRY_POINT_SRC = "-DentryPointSrc=";

  private static final String ARG_ENTRY_POINT_METHOD_NAME = "-DentryPointMethod=";
  
  public static void main(String[] args) throws IOException
  {
    TYPE_SOLVER = TypeSolverFactory.get()
        .create()
        .addJavaSourceSolver(argJavaSourceDeps(args))
        .addJarSolver(argJarDeps(args))
        .typeSolver();

    App a = new App();
    a.start(argEntryPointSrc(args), argEntryPointMethodName(args));
  }

  private static String argEntryPointSrc(String[] args)
  {
    return Arrays.stream(args).filter(arg -> arg.startsWith(ARG_ENTRY_POINT_SRC)).findFirst()
        .orElseThrow(() -> new RuntimeException("Does not found entry point arg"))
        .substring(ARG_ENTRY_POINT_SRC.length());
  }
  
  private static String argEntryPointMethodName(String[] args)
  {
    return Arrays.stream(args).filter(arg -> arg.startsWith(ARG_ENTRY_POINT_METHOD_NAME)).findFirst()
        .orElseThrow(() -> new RuntimeException("Does not found entry point method name arg"))
        .substring(ARG_ENTRY_POINT_METHOD_NAME.length());
  }

  private static String[] argJavaSourceDeps(String[] args)
  {
    for (String a : args) {
      if (a.startsWith(ARG_SOURCE_PATHS)) {
        String substring = a.substring(ARG_SOURCE_PATHS.length());
        return substring.split(":");
      }
    }
    return new String[] {};
  }
  
  private static String[] argJarDeps(String[] args)
  {
    for (String a : args) {
      if (a.startsWith(ARG_JAR_DEPS)) {
        String substring = a.substring(ARG_JAR_DEPS.length());
        return substring.split(":");
      }
    }
    return new String[] {};
  }

  private void start(String pathToEntryPointSourceFile, String entryPointMethodName) throws FileNotFoundException
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
