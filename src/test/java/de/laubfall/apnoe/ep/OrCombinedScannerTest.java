package de.laubfall.apnoe.ep;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import de.laubfall.apnoe.dummy.A;
import de.laubfall.apnoe.dummy.B;
import de.laubfall.apnoe.ty.TypeSolverFactory;

public class OrCombinedScannerTest
{
  @BeforeAll
  public static void initTypeSolver()
  {
    TypeSolverFactory.get().create().addJavaSourceSolver("src/test/java");
  }

  @Test
  public void matchOr() throws FileNotFoundException
  {
    final ByTypeScanner aScanner = new ByExtendedTypeScanner(A.class.getName());
    final ByTypeScanner bScanner = new ByExtendedTypeScanner(B.class.getName());
    
    final OrCombinedScanner orCombinedScanner = new OrCombinedScanner(aScanner, bScanner);
    boolean match = orCombinedScanner.match(new FileInputStream(new File("src/test/java/de/laubfall/apnoe/dummy/BScannerChild.java")));
    Assert.assertTrue(match);
  }
}
