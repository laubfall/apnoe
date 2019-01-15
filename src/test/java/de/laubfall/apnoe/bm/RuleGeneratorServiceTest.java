package de.laubfall.apnoe.bm;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import de.laubfall.apnoe.dummy.A;

public class RuleGeneratorServiceTest
{
  @Test
  public void generateRulesForA()
  {
//    final HierarchyAnalyzerService has = new HierarchyAnalyzerService();
//    final CallHierarchyNode result = has.analyze("src/test/java/de/laubfall/apnoe/dummy/A.java", "main");
    
    RamRuleWriter ramRuleWriter = new RamRuleWriter();
    SimpleRuleGenerator simpleRuleGenerator = new SimpleRuleGenerator();
    RuleGeneratorService ruleGeneratorService = new RuleGeneratorService(simpleRuleGenerator, ramRuleWriter);
    GeneratorContext ctx = new GeneratorContext("Test Rule", A.class, "main", "AT INVOKE", "debug('entered main')");
    ruleGeneratorService.generateEntryPointRule(ctx);
    
    String rule = ramRuleWriter.getRule("Test Rule");
    Assert.assertNotNull(rule);
  }
}
