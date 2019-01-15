package de.laubfall.apnoe.bm;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.io.IOUtils;

public class FileSystemRuleWriter implements IRuleWriter
{
  private File pathToWriteTo;

  public FileSystemRuleWriter(File pathToWriteTo)
  {
    super();
    this.pathToWriteTo = pathToWriteTo;
  }

  @Override
  public void writeRule(String filename, String ruleContent)
  {
    try {
      IOUtils.write(ruleContent, new FileOutputStream(new File(pathToWriteTo, filename +".btm")));
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

}
