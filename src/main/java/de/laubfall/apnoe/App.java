package de.laubfall.apnoe;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.laubfall.apnoe.ep.EntryPointScannerService;
import de.laubfall.apnoe.ep.ScannerConfigurerService;
import de.laubfall.apnoe.ep.ScannerDefinition;
import de.laubfall.apnoe.hie.CallHierachyResultPrinter;
import de.laubfall.apnoe.hie.CallHierarchyNode;
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
    if (scannerDefinition != null) {
      a.startScanner(argEntryPointSrc(args), scannerDefinition, (entryMethodName, artifacts) -> {
        artifacts.stream().map(f -> a.start(f.getAbsolutePath(), entryMethodName))
            .forEach(node -> new CallHierachyResultPrinter().printToSyso(node));
        return null;
      });
    } else {
      CallHierarchyNode node = a.start(argEntryPointSrc(args), argEntryPointMethodName(args));
      new CallHierachyResultPrinter().printToSyso(node);
    }

  }

  CallHierarchyNode start(String pathToEntryPointSourceFile, String entryPointMethodName)
  {
    final HierarchyAnalyzerService has = new HierarchyAnalyzerService();
    return has.analyze(pathToEntryPointSourceFile, entryPointMethodName);
  }
}
