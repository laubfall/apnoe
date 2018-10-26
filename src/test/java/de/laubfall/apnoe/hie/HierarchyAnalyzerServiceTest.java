package de.laubfall.apnoe.hie;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

public class HierarchyAnalyzerServiceTest
{
  @Test
  public void simpleHierarchyScan()
  {
    TypeSolverFactory.get().create();
    
    final HierarchyAnalyzerService has = new HierarchyAnalyzerService();
    final CallHierachyResult result = has.analyze("src/test/java/de/laubfall/apnoe/dummy/A.java", "main");
    
    new CallHierachyResultPrinter().printToSyso(result);
    
    assertNotNull(result);
    assertNotNull(result.getNode());
    assertEquals("main", result.getScopeName());
    assertEquals(6, result.getLeafs().size());
  }
}
