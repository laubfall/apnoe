package de.laubfall.apnoe.hie;

import java.util.ArrayList;
import java.util.List;

public class IfElseNode extends ExecutionControlNode
{
  private List<IfElseNode> successors = new ArrayList<>();
  
  public final void addSuccessor(IfElseNode node) {
    successors.add(node);
  }

  public List<IfElseNode> getSuccessors()
  {
    return successors;
  }
}
