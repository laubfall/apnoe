package de.laubfall.apnoe;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.laubfall.apnoe.ep.EntryPointScannerService;
import de.laubfall.apnoe.ep.ScannerConfigurerService;
import de.laubfall.apnoe.ep.ScannerDefinition;
import de.laubfall.apnoe.hie.CallHierachyResultPrinter;
import de.laubfall.apnoe.hie.HierarchyAnalyzerService;

/**
 * Sample class that demonstrate the function of the source analyzer service. The result is printed to the console.
 * 
 * @author Daniel
 *
 */
public class App extends AbstractApnoeApp
{
  private static final Logger LOG = LogManager.getLogger(App.class);
  
  public static void main(String[] args) throws IOException
  {
    initTypeSolver(args);
    
    final String scannerDefinition = argScanner(args, true);
    final App a = new App();
    if(scannerDefinition != null) {
      final ScannerConfigurerService scs = new ScannerConfigurerService();
      List<ScannerDefinition> scds = scs.createScannerDefinitions(scannerDefinition);
      LOG.info("Created " + scds.size() + " scanner definitions");
      for(ScannerDefinition sd : scds) {
        final EntryPointScannerService scannerService = scs.createScannerService(sd, argEntryPointSrc(args));
        final List<File> findEntryPoints = scannerService.findEntryPoints();
        LOG.info("Found " + findEntryPoints.size() + " potential source files");
        for (String entryPoint : sd.getEntryPoints()) {
          findEntryPoints.forEach(f -> a.start(f.getAbsolutePath(), entryPoint));
        }
      }
    } else {      
      a.start(argEntryPointSrc(args), argEntryPointMethodName(args));
    }

  }

  void start(String pathToEntryPointSourceFile, String entryPointMethodName)
  {
    final HierarchyAnalyzerService has = new HierarchyAnalyzerService();
    new CallHierachyResultPrinter().printToSyso(has.analyze(pathToEntryPointSourceFile, entryPointMethodName));
  }
}
