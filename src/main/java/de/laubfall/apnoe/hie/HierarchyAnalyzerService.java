package de.laubfall.apnoe.hie;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;

public class HierarchyAnalyzerService
{
  private static final Logger LOG = LogManager.getLogger(HierarchyAnalyzerService.class);
  
  public CallHierachyResult analyze(String pathToEntryPointSourceFile, String entryPointMethodName)
  {
    // The starting point from where we start to find all the different call hierarchies.
    Optional<ClassOrInterfaceDeclaration> node;
    try {
      node = JavaParser
          .parse(new File(pathToEntryPointSourceFile))
          .findFirst(ClassOrInterfaceDeclaration.class);
    } catch (FileNotFoundException e) {
      LOG.warn("Source file to analyze not found", e);
      throw new HierarchyException("Source file not found.");
    }

    // looking for the entry point
    Optional<MethodDeclaration> entryPoint = node.get().findFirst(MethodDeclaration.class)
        .filter(md -> md.getNameAsString().equals(entryPointMethodName));

    CallHierachyResult result = new CallHierachyResult();
    entryPoint.get().accept(new CallHierachyVisitor(), result);
    return result;
  }
}
