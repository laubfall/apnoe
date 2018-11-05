package de.laubfall.apnoe.ep;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ChainedScanner extends AbstractScannerAdapter
{
  private List<IScanner> chain = new ArrayList<>();

  public ChainedScanner(IScanner... iScanners)
  {
    for (IScanner sc : iScanners) {
      chain.add(sc);
    }
  }

  @Override
  public boolean match(InputStream artifact)
  {
    for (IScanner sc : chain) {
      boolean match = sc.match(artifact);
      if (match) {
        return true;
      }
    }
    return false;
  }

}
