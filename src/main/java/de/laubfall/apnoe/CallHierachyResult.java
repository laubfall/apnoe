package de.laubfall.apnoe;

import java.util.ArrayList;
import java.util.List;

import com.github.javaparser.ast.Node;

public class CallHierachyResult
{
  private String scopeName;
  
  private List<CallHierachyResult> leafs = new ArrayList<>();

  private Node node;
  
  public CallHierachyResult() {
    this("");
  }
  
  public CallHierachyResult(String scopeName)
  {
    super();
    this.scopeName = scopeName;
  }

  public void addLeaf(final CallHierachyResult leaf)
  {
    leafs.add(leaf);
  }

  public String getScopeName()
  {
    return scopeName;
  }

  public void setScopeName(String scopeName)
  {
    this.scopeName = scopeName;
  }

  public List<CallHierachyResult> getLeafs()
  {
    return leafs;
  }

  public Node getNode()
  {
    return node;
  }

  public void setNode(Node node)
  {
    this.node = node;
  }
}
