package de.laubfall.apnoe.bm;

import de.laubfall.apnoe.hie.CallHierarchyNode;

/**
 * Used by byteman rules for recording method calls and their parameters.
 * 
 * @author Daniel
 *
 */
public class RuntimeRecorder
{
  /**
   * Records one parameter of a method call.
   * 
   * @param callHierarchyNodeId ID given by {@link CallHierarchyNode#getUid()}. Mandatory. Used to constraint the
   *          parameter to the call hierarchy node that was the base for rule that triggered this method.
   * @param o a parameter.
   */
  public void record(String callHierarchyNodeId, Object o)
  {
    System.err.println(o);
  }

  public void record(String callHierarchyNodeId, Object... params)
  {

  }
}
