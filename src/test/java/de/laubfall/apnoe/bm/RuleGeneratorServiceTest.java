package de.laubfall.apnoe.bm;

import de.laubfall.apnoe.ty.TypeSolverFactory;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import de.laubfall.apnoe.dummy.A;
import de.laubfall.apnoe.hie.CallHierarchyNode;
import de.laubfall.apnoe.hie.HierarchyAnalyzerService;

public class RuleGeneratorServiceTest
{
  @Test
  public void generateEntryRule()
  {

    RamRuleWriter ramRuleWriter = new RamRuleWriter();
    SimpleRuleGenerator simpleRuleGenerator = new SimpleRuleGenerator();
    RuleGeneratorService ruleGeneratorService = new RuleGeneratorService(simpleRuleGenerator, ramRuleWriter);
    GeneratorContext ctx = new GeneratorContext("Test Rule", A.class, "main", "AT INVOKE", "debug('entered main')");
    ruleGeneratorService.generateEntryPointRule(ctx);

    String rule = ramRuleWriter.getRule("Test Rule");
    Assert.assertNotNull(rule);
  }

  @Test
  public void generateRulesForA()
  {
    TypeSolverFactory.get().create().addJavaSourceSolver("src/test/java");

    final HierarchyAnalyzerService has = new HierarchyAnalyzerService();
    final CallHierarchyNode result = has.analyze("src/test/java/de/laubfall/apnoe/dummy/A.java", "main");
    
  }
}
