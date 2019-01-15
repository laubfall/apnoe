package de.laubfall.apnoe.bm;

public interface IRuleGenerator
{
  String entryPointRule(GeneratorContext ctx);
  
  String invokeRule(GeneratorContext ctx);
}
