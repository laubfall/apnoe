package de.laubfall.apnoe.bm;

public class SimpleRuleGenerator implements IRuleGenerator
{

  @Override
  public String entryPointRule(GeneratorContext ctx)
  {
    final StringBuilder sb = new StringBuilder();
    sb.append("RULE ").append(ctx.getRuleName()).append("\n");
    sb.append("CLASS ").append(ctx.getClazz().getCanonicalName()).append("\n");
    sb.append("METHOD ").append(ctx.getMethodName()).append("\n");
    sb.append(ctx.getBytemanEvent()).append("\n");
    sb.append("IF true").append("\n");
    sb.append("DO ").append(ctx.getDoExpression()).append("\n");
    sb.append("ENDRULE");
    return sb.toString();
  }

  @Override
  public String invokeRule(GeneratorContext ctx)
  {
    // TODO Auto-generated method stub
    return null;
  }

}
