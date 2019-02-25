package de.laubfall.apnoe.bm;

/**
 * Generates Rules for entry points and Method invocations.
 * 
 * @author Daniel
 *
 */
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
    final StringBuilder sb = new StringBuilder();
    sb.append("RULE ").append(ctx.getRuleName()).append("\n");
    sb.append("CLASS ").append(ctx.getClazz().getCanonicalName()).append("\n");
    sb.append("HELPER ").append(RuntimeRecorder.class.getCanonicalName()).append("\n");
    sb.append("METHOD ").append(ctx.getMethodName()).append("\n");
    // TODO method declaration myMethod(java.lang.String s) or similiar
    sb.append("AT INVOKE ").append("\n").append(" ALL");
    sb.append("IF true").append("\n");
    sb.append("DO ").append(ctx.getDoExpression()).append("\n");
    sb.append("ENDRULE");
    return sb.toString();
  }

  /**
   * Creates the method declaration consumable by the byteman METHOD statement. Needs to know the name of the method and
   * the type of the method parameters.
   * 
   * @return
   */
  private String generateInvokeRuleMethodDeclaration()
  {

    return null;
  }
}
