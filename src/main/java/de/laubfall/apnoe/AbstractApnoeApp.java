package de.laubfall.apnoe;

import java.util.Arrays;

import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;

import de.laubfall.apnoe.hie.TypeSolverFactory;

public abstract class AbstractApnoeApp
{
  protected static CombinedTypeSolver TYPE_SOLVER;
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

  abstract void start(String pathToEntryPointSourceFile, String entryPointMethodName);
  
  protected static final void initTypeSolver(String[] args) {
    TYPE_SOLVER = TypeSolverFactory.get()
        .create()
        .addJavaSourceSolver(argJavaSourceDeps(args))
        .addJarSolver(argJarDeps(args))
        .typeSolver();
  }
  
  protected static String argEntryPointSrc(String[] args)
  {
    return Arrays.stream(args).filter(arg -> arg.startsWith(ARG_ENTRY_POINT_SRC)).findFirst()
        .orElseThrow(() -> new RuntimeException("Does not found entry point arg"))
        .substring(ARG_ENTRY_POINT_SRC.length());
  }

  protected static String argEntryPointMethodName(String[] args)
  {
    return Arrays.stream(args).filter(arg -> arg.startsWith(ARG_ENTRY_POINT_METHOD_NAME)).findFirst()
        .orElseThrow(() -> new RuntimeException("Does not found entry point method name arg"))
        .substring(ARG_ENTRY_POINT_METHOD_NAME.length());
  }

  protected static String[] argJavaSourceDeps(String[] args)
  {
    for (String a : args) {
      if (a.startsWith(ARG_SOURCE_PATHS)) {
        String substring = a.substring(ARG_SOURCE_PATHS.length());
        return substring.split(":");
      }
    }
    return new String[] {};
  }

  protected static String[] argJarDeps(String[] args)
  {
    for (String a : args) {
      if (a.startsWith(ARG_JAR_DEPS)) {
        String substring = a.substring(ARG_JAR_DEPS.length());
        return substring.split(":");
      }
    }
    return new String[] {};
  }

}
