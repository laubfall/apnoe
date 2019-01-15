package de.laubfall.apnoe.bm;

import java.io.File;

/**
 * Service provides functionality to generate inspection rules (see InvokeInspect.btm and EntryPointInspect.btm)
 * 
 * This class and its methods are the starting point of the whole process of recording and displaying recorded runtime
 * values.
 * 
 * The second step is to run the programm code with the generated rules. After that the recordings can be read and
 * displayed somehow.
 * 
 * @author Daniel
 *
 */
public class RuleGeneratorService
{
  private IRuleGenerator ruleGenerator;

  private IRuleWriter ruleWriter;
  
  public RuleGeneratorService()
  {
    this.ruleGenerator = new SimpleRuleGenerator();
    this.ruleWriter = new FileSystemRuleWriter(new File("src/main/resources"));
  }

  public RuleGeneratorService(IRuleGenerator ruleGenerator, IRuleWriter ruleWriter)
  {
    this.ruleGenerator = ruleGenerator;
    this.ruleWriter = ruleWriter;
  }

  public final void generateEntryPointRule(GeneratorContext ctx)
  {
    final String entryPointRule = ruleGenerator.entryPointRule(ctx);
    ruleWriter.writeRule(ctx.getRuleName(), entryPointRule);
  }

  public void generateInvokeRule(GeneratorContext ctx)
  {

  }
}
