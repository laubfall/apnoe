package de.laubfall.apnoe.ep;

import java.io.InputStream;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.resolution.types.ResolvedReferenceType;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;

import de.laubfall.apnoe.hie.TypeSolverFactory;

public class ByExtendedTypeScanner extends ByTypeScanner
{
  private static final Logger LOG = LogManager.getFormatterLogger(ByExtendedTypeScanner.class);
  
  public ByExtendedTypeScanner(String fqnTypeName)
  {
    super(fqnTypeName);
  }

  @Override
  public boolean match(InputStream artifact)
  {
    final CompilationUnit parsedArtifact = JavaParser.parse(artifact);
    Optional<TypeDeclaration> typeDecOpt = parsedArtifact.findFirst(TypeDeclaration.class);
    if(typeDecOpt.isPresent() == false) {
      LOG.debug("No type declaration found in artifact: " + parsedArtifact);
      return false;
    }
    final TypeDeclaration<?> typeDeclaration = typeDecOpt.get();
    final JavaParserFacade javaParserFacade = JavaParserFacade.get(TypeSolverFactory.get().typeSolver());
    final List<ResolvedReferenceType> ancestors = javaParserFacade.getTypeDeclaration(typeDeclaration).getAllAncestors();
    for (ResolvedReferenceType rrt : ancestors) {
      if(rrt.getQualifiedName().equals(getFqnTypeName())) {
        return true;
      }
    }
    return false;
  }

}
