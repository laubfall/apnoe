package de.laubfall.apnoe.hie;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

public class HierarchyAnalyzerServiceTest
{
  @Test
  public void simpleHierarchyScan()
  {
    TypeSolverFactory.get().create();
    
    final HierarchyAnalyzerService has = new HierarchyAnalyzerService();
    CallHierachyResult result = has.analyze("src/test/java/de/laubfall/apnoe/dummy/A.java", "main");
    assertNotNull(result);
  }
}
