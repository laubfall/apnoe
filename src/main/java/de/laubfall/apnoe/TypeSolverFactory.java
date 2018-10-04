package de.laubfall.apnoe;

import java.io.IOException;

import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JarTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;

public class TypeSolverFactory
{
  private static TypeSolverFactory instance;

  private CombinedTypeSolver typeSolver;

  private TypeSolverFactory()
  {
    super();
  }

  public TypeSolverFactory addJarSolver(String pathToJar)
  {
    try {
      typeSolver.add(new JarTypeSolver(pathToJar));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return this;
  }

  public TypeSolverFactory addJarSolver(String... pathsToJar)
  {
    for (String p : pathsToJar) {
      addJarSolver(p);
    }
    return this;
  }

  public TypeSolverFactory addJavaSourceSolver(String pathToJavaSource)
  {
    typeSolver.add(new JavaParserTypeSolver(pathToJavaSource));
    return this;
  }

  public TypeSolverFactory addJavaSourceSolver(String... pathToJavaSource)
  {
    for(String path : pathToJavaSource) {
      addJavaSourceSolver(path);
    }
    return this;
  }

  public TypeSolverFactory addSrcTypeSolver(String pathToSrc)
  {
    typeSolver.add(new JavaParserTypeSolver(pathToSrc));
    return this;
  }

  public TypeSolverFactory create()
  {
    typeSolver = new CombinedTypeSolver();
    typeSolver.add(new ReflectionTypeSolver());
    return this;
  }

  public CombinedTypeSolver build()
  {
    return typeSolver;
  }

  public static TypeSolverFactory get()
  {
    if (instance == null) {
      instance = new TypeSolverFactory();
    }

    return instance;
  }
}
