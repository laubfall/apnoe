package de.laubfall.apnoe.bm;

/**
 * Used by {@link RuleGeneratorService}. Provides information required by the rule generation service.
 * 
 * @author Daniel
 *
 */
public class GeneratorContext
{
  /**
   * RULE a rulename
   */
  private String ruleName;
  
  /**
   * CLASS AClass
   */
  private Class<?> clazz;
  
  /**
   * METHOD methodName
   */
  private String methodName;
  
  /**
   * E.g. AT ENTRY
   */
  private String bytemanEvent;
  
  /**
   * DO an Byteman expression
   */
  private String doExpression;

  public GeneratorContext(String ruleName, Class<?> clazz, String methodName, String bytemanEvent, String doExpression)
  {
    super();
    this.ruleName = ruleName;
    this.clazz = clazz;
    this.methodName = methodName;
    this.bytemanEvent = bytemanEvent;
    this.doExpression = doExpression;
  }

  public String getRuleName()
  {
    return ruleName;
  }

  public Class<?> getClazz()
  {
    return clazz;
  }

  public String getMethodName()
  {
    return methodName;
  }

  public String getBytemanEvent()
  {
    return bytemanEvent;
  }

  public String getDoExpression()
  {
    return doExpression;
  }
}
