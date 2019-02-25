package de.laubfall.apnoe.ep;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import de.laubfall.apnoe.dummy.A;
import de.laubfall.apnoe.ty.TypeSolverFactory;

public class ByExtendedTypeScannerTest
{
  @BeforeAll
  public static void initTypeSolver()
  {
    TypeSolverFactory.get().create().addJavaSourceSolver("src/test/java");
  }

  @Test
  public void match() throws FileNotFoundException
  {
    final ByExtendedTypeScanner byExtendedTypeScanner = new ByExtendedTypeScanner(A.class.getName());
    InputStream artifact = new FileInputStream("src/test/java/de/laubfall/apnoe/dummy/AScannerChild.java");
    boolean match = byExtendedTypeScanner.match(artifact);
    Assert.assertTrue(match);
  }

  @Test
  public void matchClassHierarchy() throws FileNotFoundException
  {
    final ByExtendedTypeScanner byExtendedTypeScanner = new ByExtendedTypeScanner(A.class.getName());
    InputStream artifact = new FileInputStream("src/test/java/de/laubfall/apnoe/dummy/AScannerChildChild.java");
    boolean match = byExtendedTypeScanner.match(artifact);
    Assert.assertTrue(match);
  }
}
