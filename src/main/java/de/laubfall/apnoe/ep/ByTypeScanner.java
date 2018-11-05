package de.laubfall.apnoe.ep;

import java.io.InputStream;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;

import de.laubfall.apnoe.hie.TypeSolverFactory;

public class ByTypeScanner extends AbstractScannerAdapter
{
  private final String fqnTypeName;

  public ByTypeScanner(String fqnTypeName)
  {
    super();
    this.fqnTypeName = fqnTypeName;
  }

  @Override
  public boolean match(InputStream artifact)
  {
    final CompilationUnit parsedArtifact = JavaParser.parse(artifact);

    final JavaParserFacade javaParserFacade = JavaParserFacade.get(TypeSolverFactory.get().typeSolver());

    final NodeList<TypeDeclaration<?>> types = parsedArtifact.getTypes();
    return types.stream().filter(td -> {
      final ResolvedReferenceTypeDeclaration type = javaParserFacade.getTypeDeclaration(td);
      if(type == null) {
        return false;
      }
      return type.getQualifiedName().equals(fqnTypeName);
    }).count() > 0l;
  }

  public String getFqnTypeName()
  {
    return fqnTypeName;
  }

}
