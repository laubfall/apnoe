package de.laubfall.apnoe.ep;

import java.util.List;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

public class ScannerConfigurerServiceTest
{
  @Test
  public void scan()
  {
    final String scanArg = "de.laubfall.A:[blah,blub];de.laubfall.V:[hurz]";
    final ScannerConfigurerService scs = new ScannerConfigurerService();
    final List<ScannerDefinition> scannerDefinitions = scs.createScannerDefinitions(scanArg);
    
    Assert.assertNotNull(scannerDefinitions);
    Assert.assertEquals(2, scannerDefinitions.size());
    
    ScannerDefinition sd = scannerDefinitions.get(0);
    Assert.assertEquals(2, sd.getEntryPoints().size());
    
    sd = scannerDefinitions.get(1);
    Assert.assertEquals(1, sd.getEntryPoints().size());
  }
}
