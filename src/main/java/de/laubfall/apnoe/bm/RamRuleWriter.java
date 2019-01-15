package de.laubfall.apnoe.bm;

import java.util.HashMap;
import java.util.Map;

public class RamRuleWriter implements IRuleWriter
{
  private Map<String, String> inMemoryStore = new HashMap<>();

  @Override
  public void writeRule(String filename, String ruleContent)
  {
    inMemoryStore.put(filename, ruleContent);
  }

  public final String getRule(String filename)
  {
    return inMemoryStore.get(filename);
  }
}
