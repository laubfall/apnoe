package de.laubfall.apnoe.hie;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Test the Hierarchy Analyzer Service. Tries to check the resulting structure.
 * 
 * @author Daniel
 *
 */
public class HierarchyAnalyzerServiceTest
{
  @BeforeAll
  public static void initTypeSolver()
  {
    TypeSolverFactory.get().create();
  }

  @Test
  public void simpleHierarchyScan()
  {
    final HierarchyAnalyzerService has = new HierarchyAnalyzerService();
    final CallHierachyResult result = has.analyze("src/test/java/de/laubfall/apnoe/dummy/A.java", "main");

    new CallHierachyResultPrinter().printToSyso(result);

    assertNotNull(result);
    assertNotNull(result.getNode());
    assertEquals("main", result.getScopeName());
    assertEquals(6, result.getLeafs().size());
    
    assertEquals(18, result.countLeafs());
  }

  @Test
  public void ifElseScan()
  {
    final HierarchyAnalyzerService has = new HierarchyAnalyzerService();
    final CallHierachyResult result = has.analyze("src/test/java/de/laubfall/apnoe/dummy/IfElseSample.java", "main");

    new CallHierachyResultPrinter().printToSyso(result);

    assertNotNull(result);
    assertEquals(5, result.getLeafs().size());
    assertEquals("if", result.getLeafs().get(0).getScopeName());
    assertEquals("else if", result.getLeafs().get(1).getScopeName());
    assertEquals("else if", result.getLeafs().get(2).getScopeName());
    
    assertEquals("if", result.getLeafs().get(3).getScopeName());
    assertEquals("else", result.getLeafs().get(4).getScopeName());
    
    assertEquals(10, result.countLeafs());
  }
}
