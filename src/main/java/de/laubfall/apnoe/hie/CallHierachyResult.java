package de.laubfall.apnoe.hie;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.github.javaparser.ast.Node;

/**
 * Represents an element inside one call hierarchy. Most typically a method call or a flow statement like if, switch or
 * similar. It is also used as the root statement of all following method calls and flow statements.
 * 
 * @author Daniel
 *
 */
public class CallHierachyResult
{
  /**
   * Name of the statement that is represented by this instance (for example a method name).
   */
  private String scopeName;

  /**
   * Actually not in use.
   */
  private CallHierachyResult parent;

  /**
   * All the leafs (e.g. method calls of if-else-statements.
   */
  private List<CallHierachyResult> leafs = new ArrayList<>();

  /**
   * Node as found by the java parser. Hold to obtain additional information about the code artifact if necessary.
   */
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

  /**
   * Find a instance of {@link CallHierachyResult} for the given node (see parameter).
   * 
   * @param node if an instance of {@link CallHierachyResult} has the given node stored inside the corresponding field
   *          the method will return this instance.
   * @return null if there was no matching instance (see parameter description).
   */
  public final CallHierachyResult findCallHierachyByNode(Node node)
  {
    if (this.node.equals(node)) {
      return this;
    }

    for (CallHierachyResult leaf : leafs) {
      CallHierachyResult result = leaf.findCallHierachyByNode(node);
      if (result != null) {
        return result;
      }
    }

    return null;
  }

  /**
   * Count all method calls and flow control statements.
   * 
   * @return the count as specified by the description.
   */
  public final int countLeafs()
  {
    int result = leafs.size();
    result += leafs.stream().map(chr -> chr.countLeafs()).collect(Collectors.summingInt(in -> in));    
    return result;
  }

  /**
   * Count all possible branches (execution paths).
   * 
   * @return the count as specified by the description.
   */
  public final int countBranches()
  {
    int result = 0;
    if(leafs.isEmpty()) {
      result += 1;
    } else {
      result += leafs.stream().map(chr -> chr.countBranches()).collect(Collectors.summingInt(in -> in));
    }
    
    return result;
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
