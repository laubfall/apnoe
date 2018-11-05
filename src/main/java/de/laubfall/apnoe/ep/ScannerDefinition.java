package de.laubfall.apnoe.ep;

import java.util.ArrayList;
import java.util.List;

public class ScannerDefinition
{
  private ByExtendedTypeScanner scanner;

  private List<String> entryPoints = new ArrayList<>();

  public ScannerDefinition(ByExtendedTypeScanner scanner, String... entryPoints)
  {
    super();
    this.scanner = scanner;
    for(String ep : entryPoints) {
      this.entryPoints.add(ep);
    }
  }

  public List<String> getEntryPoints()
  {
    return entryPoints;
  }

  public ByExtendedTypeScanner getScanner()
  {
    return scanner;
  }
}
