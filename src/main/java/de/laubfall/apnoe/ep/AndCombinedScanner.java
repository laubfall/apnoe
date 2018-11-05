package de.laubfall.apnoe.ep;

import java.io.InputStream;

public class AndCombinedScanner implements IScanner
{
  private IScanner left;
  
  private IScanner right;
  
  public AndCombinedScanner(IScanner left, IScanner right)
  {
    super();
    this.left = left;
    this.right = right;
  }

  @Override
  public boolean match(InputStream artifact)
  {
    return left.match(artifact) && right.match(artifact);
  }

}
