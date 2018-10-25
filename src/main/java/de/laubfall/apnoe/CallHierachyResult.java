package de.laubfall.apnoe;

import java.util.ArrayList;
import java.util.List;

import com.github.javaparser.ast.Node;

public class CallHierachyResult
{
  private String scopeName;

  private CallHierachyResult parent;

  private List<CallHierachyResult> leafs = new ArrayList<>();

  private Node node;

  public CallHierachyResult()
  {
    this("");
  }

  public CallHierachyResult(String scopeName)
  {
    super();
    this.scopeName = scopeName;
  }

  public CallHierachyResult findCallHierachyByNode(Node node)
  {
    if(this.node.equals(node)) {
      return this;
    }
    
    for(CallHierachyResult leaf : leafs) {
      CallHierachyResult result = leaf.findCallHierachyByNode(node);
      if(result != null) {
        return result;
      }
    }
    
    return null;
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

  public CallHierachyResult getParent()
  {
    return parent;
  }

  public void setParent(CallHierachyResult parent)
  {
    this.parent = parent;
  }
}
