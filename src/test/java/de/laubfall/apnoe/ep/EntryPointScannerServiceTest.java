package de.laubfall.apnoe.ep;

import java.io.File;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import de.laubfall.apnoe.hie.TypeSolverFactory;
import junit.framework.Assert;

public class EntryPointScannerServiceTest
{
  @BeforeAll
  public static void initTypeSolver()
  {
    TypeSolverFactory.get().create();
  }
  
  @Test
  public void scan01()
  {
    final ByTypeScanner byTypeScanner = new ByTypeScanner("de.laubfall.apnoe.dummy.A");
    final EntryPointScannerService scannerService = new EntryPointScannerService(byTypeScanner, "src/test");
    final List<File> entryPoints = scannerService.findEntryPoints();
    Assert.assertNotNull(entryPoints);
    Assert.assertEquals(1, entryPoints.size());
  }
}
