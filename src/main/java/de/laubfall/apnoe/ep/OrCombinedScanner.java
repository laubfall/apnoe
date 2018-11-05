package de.laubfall.apnoe.ep;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class OrCombinedScanner extends AbstractScannerAdapter
{
  private static final Logger LOG = LogManager.getLogger(OrCombinedScanner.class);

  private final IScanner left;

  private final IScanner right;

  public OrCombinedScanner(IScanner left, IScanner right)
  {
    super();
    this.left = left;
    this.right = right;
  }

  @Override
  public boolean match(InputStream artifact)
  {
    try {
      byte[] byteArray = IOUtils.toByteArray(artifact);
      return left.match(byteArray) || right.match(byteArray);
    } catch (IOException e) {
      LOG.error("Failed to copy source artifact stream. Returning false for this matcher", e);
    }

    return false;
  }

}