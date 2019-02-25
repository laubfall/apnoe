package de.laubfall.apnoe;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;

import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;

import de.laubfall.apnoe.ep.EntryPointScannerService;
import de.laubfall.apnoe.ep.ScannerConfigurerService;
import de.laubfall.apnoe.ep.ScannerDefinition;
import de.laubfall.apnoe.hie.CallHierarchyNode;
import de.laubfall.apnoe.hie.HierarchyAnalyzerService;
import de.laubfall.apnoe.ty.TypeSolverFactory;

/**
 * Base class for apps that want to make use of the {@link HierarchyAnalyzerService}. It offers some convenient methods
 * for handling cmd line parameters that are necessary for the analyzer and type solver.
 * 
 * @author Daniel
 *
 */
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

  /**
   * Name of the method that acts as the entry point for the analyze.
   */
  private static final String ARG_ENTRY_POINT_METHOD_NAME = "-DentryPointMethod=";

  /**
   * A scanner definition. It is a concatenation of tokens, delimited by a semicolon. A token is defined by two things:
   * FQN of the java source where we try to find an method where to start the scan and the name of that method.
   * 
   * See {@link ScannerConfigurerService} for more details.
   */
  private static final String ARG_SCAN_DEFINITION = "-Dscanner=";

  protected final List<CallHierarchyNode> startScanner(String sourcePath, String scannerDefinition,
      BiFunction<String, List<File>, List<CallHierarchyNode>> doScanWork)
  {
    final ScannerConfigurerService scs = new ScannerConfigurerService();
    List<ScannerDefinition> scds = scs.createScannerDefinitions(scannerDefinition);
    List<CallHierarchyNode> result = new ArrayList<>();
    //    LOG.info("Created " + scds.size() + " scanner definitions");
    for (ScannerDefinition sd : scds) {
      final EntryPointScannerService scannerService = scs.createScannerService(sd, sourcePath);
      final List<File> findEntryPoints = scannerService.findEntryPoints();
      //      LOG.info("Found " + findEntryPoints.size() + " potential source files");
      for (String entryPoint : sd.getEntryPoints()) {
        final List<CallHierarchyNode> doneWork = doScanWork.apply(entryPoint, findEntryPoints);
        if (doneWork != null) {
          result.addAll(doneWork);
        }
      }
    }

    return result;
  }

  protected static final void initTypeSolver(String[] args)
  {
    TYPE_SOLVER = TypeSolverFactory.get()
        .create()
        .addJavaSourceSolver(argJavaSourceDeps(args))
        .addJarSolver(argJarDeps(args))
        .typeSolver();
  }

  protected static String argScanner(String[] args, boolean silent)
  {
    Optional<String> findFirst = Arrays.stream(args).filter(arg -> arg.startsWith(ARG_SCAN_DEFINITION)).findFirst();
    if (silent) {
      if (findFirst.isPresent() == false) {
        return null;
      }
      return findFirst.get().substring(ARG_SCAN_DEFINITION.length());
    } else {
      return findFirst.orElseThrow(() -> new RuntimeException("Does not found scan definition arg"))
          .substring(ARG_SCAN_DEFINITION.length());
    }
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
