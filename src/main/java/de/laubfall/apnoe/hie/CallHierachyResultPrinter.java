package de.laubfall.apnoe.hie;

import java.util.List;

public class CallHierachyResultPrinter
{

  public final void printToSyso(final CallHierachyResult result)
  {
    System.out.println("Call Hierachy:");
    System.out.println(result.getScopeName());
    printToSyso(result.getLeafs(), 1);
  }

  private final void printToSyso(final List<CallHierachyResult> childs, int depth)
  {
    for(CallHierachyResult c : childs) {
      System.out.println("");
      for(int i = 0; i < depth; i++)
        System.out.print("\t");
      System.out.print(c.getScopeName());

      printToSyso(c.getLeafs(), depth+1);
    }
  }
}
