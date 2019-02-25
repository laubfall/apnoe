package de.laubfall.apnoe.hie;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.stmt.IfStmt;

/**
 * Represents an element inside one call hierarchy. Most typically a method call or a flow statement like if, switch or
 * similar. It is also used as the root statement of all following method calls and flow statements.
 * 
 * @author Daniel
 *
 */
public class CallHierarchyNode
{
  /**
   * Name of the statement that is represented by this instance (for example a method name).
   */
  private String scopeName;

  /**
   * Actually not in use.
   */
  private CallHierarchyNode parent;

  /**
   * All the leafs (e.g. method calls of if-else-statements.
   */
  private List<CallHierarchyNode> leafs = new ArrayList<>();

  /**
   * Node as found by the java parser. Hold to obtain additional information about the code artifact if necessary.
   */
  private Node node;

  /**
   * Random uid of this node.
   */
  private String uid;
  
  /**
   * Used to create ID for every node.
   */
  private static final Random random = new Random();
  
  public CallHierarchyNode()
  {
    this("");
  }

  public CallHierarchyNode(String scopeName)
  {
    super();
    this.scopeName = scopeName;
    uid = String.valueOf(random.nextInt(999999999));
  }

  /**
   * Find a instance of {@link CallHierarchyNode} for the given node (see parameter).
   * 
   * @param node if an instance of {@link CallHierarchyNode} has the given node stored inside the corresponding field
   *          the method will return this instance.
   * @return null if there was no matching instance (see parameter description).
   */
  public final CallHierarchyNode findCallHierachyByNode(Node node)
  {
    if (this.node.equals(node)) {
      return this;
    }

    for (CallHierarchyNode leaf : leafs) {
      CallHierarchyNode result = leaf.findCallHierachyByNode(node);
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
    if (leafs.isEmpty()) {
      result += 1;
    } else {
      result += leafs.stream().map(chr -> chr.countBranches()).collect(Collectors.summingInt(in -> in));
    }

    return result;
  }

  /**
   * Count all possible execution paths.
   * 
   * @param entryPoint true if you want to calculate possible execution paths starting from the node of this
   *          {@link CallHierarchyNode} instance.
   * @return see description.
   */
  public final int countExecutionPaths(boolean entryPoint)
  {
    // -1 to show that we just started counting
    int result = entryPoint ? 1 : 0;

    if (node instanceof IfStmt) {
      result += 1;

      final IfStmt theIf = (IfStmt) node;
      if (theIf.hasElseBranch() == false) {
        result += 1;
      }
    } else {
      result += leafs.stream().map(chr -> chr.countExecutionPaths(false)).collect(Collectors.summingInt(in -> in));
    }

    return result;
  }

  public final boolean isExecutionControlStmt() {
//    if()
    
    return false;
  }
  
  public void addLeaf(final CallHierarchyNode leaf)
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

  public List<CallHierarchyNode> getLeafs()
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

  public CallHierarchyNode getParent()
  {
    return parent;
  }

  public void setParent(CallHierarchyNode parent)
  {
    this.parent = parent;
  }

  public String getUid()
  {
    return uid;
  }

  public void setUid(String uid)
  {
    this.uid = uid;
  }
}
