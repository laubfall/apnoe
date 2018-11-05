package de.laubfall.apnoe.ep;

import java.io.InputStream;

public interface IScanner
{
  boolean match(InputStream artifact);
  
  boolean match(byte [] rawArtifact);
}
