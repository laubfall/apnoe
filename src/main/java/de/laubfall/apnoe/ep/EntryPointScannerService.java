package de.laubfall.apnoe.ep;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EntryPointScannerService
{
  private static final Logger LOG = LogManager.getLogger(EntryPointScannerService.class);

  private IScanner scanner;

  private String path;

  public EntryPointScannerService(IScanner scanner, String path)
  {
    super();
    this.scanner = scanner;
    this.path = path;
  }

  public List<File> findEntryPoints()
  {
    final File scanRoot = new File(path);
    final List<File> matches = matches(scanRoot);
    return matches;
  }

  private List<File> matches(File root)
  {
    final List<File> result = new ArrayList<>();
    final File[] listFiles = root.listFiles();
    for (File f : listFiles) {
      if (f.isFile() && f.getName().endsWith(".java")) {
        try (FileInputStream ois = FileUtils.openInputStream(f);) {
          final boolean match = scanner.match(ois);
          if (match) {
            result.add(f);
          }
        } catch (IOException e) {
          LOG.error("Failed to open source file: " + f.getAbsolutePath(), e);
        }
      } else if (f.isDirectory()) {
        result.addAll(matches(f));
      }
    }
    return result;
  }
}
