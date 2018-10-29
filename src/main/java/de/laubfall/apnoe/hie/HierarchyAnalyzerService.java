package de.laubfall.apnoe.hie;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;

/**
 * Service that provides functionality for analyzing the hierarchy of method calls.
 * 
 * @author Daniel
 *
 */
public class HierarchyAnalyzerService
{
  private static final Logger LOG = LogManager.getLogger(HierarchyAnalyzerService.class);
  
  public CallHierarchyNode analyze(String pathToEntryPointSourceFile, String entryPointMethodName)
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

    if(entryPoint.isPresent() == false) {
      LOG.warn("Entry point not found. Looked for entry point: " + entryPointMethodName);
      throw new HierarchyException("Did not found given entry point");
    }
    
    final CallHierarchyNode result = new CallHierarchyNode();
    result.setNode(node.get());
    result.setScopeName(entryPointMethodName);
    entryPoint.get().accept(new CallHierachyVisitor(), result);
    return result;
  }
}
