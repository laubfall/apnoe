package de.laubfall.apnoe.ep;

import java.io.InputStream;
import java.util.List;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.resolution.types.ResolvedReferenceType;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;

import de.laubfall.apnoe.hie.TypeSolverFactory;

public class ByExtendedTypeScanner extends ByTypeScanner
{

  public ByExtendedTypeScanner(String fqnTypeName)
  {
    super(fqnTypeName);
  }

  @Override
  public boolean match(InputStream artifact)
  {
    final CompilationUnit parsedArtifact = JavaParser.parse(artifact);
    
    final TypeDeclaration<?> typeDeclaration = parsedArtifact.findFirst(TypeDeclaration.class).get();
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
