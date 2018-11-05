package de.laubfall.apnoe.ep;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Daniel
 *
 */
public class ScannerConfigurerService
{
  /**
   * 
   * @param scannerDefinition for example: de.laubfall.A:[blah,blub];de.laubfall.V:[hurz]
   * @param sourcePath starting point for the scan that is done recursively.
   * @return the scanner service.
   */
  public EntryPointScannerService createScannerService(final ScannerDefinition scannerDefinition, final String sourcePath)
  {
    return new EntryPointScannerService(scannerDefinition.getScanner(), sourcePath);
  }

  public List<ScannerDefinition> createScannerDefinitions(String scannerDefinition)
  {
    final String[] definitions = scannerDefinition.split(";");

    final List<ScannerDefinition> result = new ArrayList<>();
    for (String def : definitions) {
      String[] split = def.split(":");
      if (split == null || split.length != 2) {
        throw new RuntimeException("Unexpected length of scan definition, expected two tokens");
      }

      // the split contains the wanted super type and the entrypoints (or a single entry point)
      final ByExtendedTypeScanner typeScanner = new ByExtendedTypeScanner(split[0]);

      // get rid of the [] at the beginning and the end.
      final String entryPoints = split[1].substring(1, split[1].length() -1);
      final ScannerDefinition sd = new ScannerDefinition(typeScanner, entryPoints.split(","));
      result.add(sd);
    }

    return result;
  }
}
