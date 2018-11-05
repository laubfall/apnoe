package de.laubfall.apnoe.ep;

import java.io.ByteArrayInputStream;

public abstract class AbstractScannerAdapter implements IScanner
{

  @Override
  public boolean match(byte[] rawArtifact)
  {
    return match(new ByteArrayInputStream(rawArtifact));
  }

}
