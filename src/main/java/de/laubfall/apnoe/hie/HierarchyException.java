package de.laubfall.apnoe.hie;

/**
 * Specialized Exception for all unwanted behavior or failure situations in {@link HierarchyAnalyzerService}.
 * 
 * @author Daniel
 *
 */
public class HierarchyException extends RuntimeException
{
  private static final long serialVersionUID = 1466676887583291210L;

  public HierarchyException()
  {
  }

  public HierarchyException(String message)
  {
    super(message);
  }

  public HierarchyException(Throwable cause)
  {
    super(cause);
  }

  public HierarchyException(String message, Throwable cause)
  {
    super(message, cause);
  }

  public HierarchyException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
  {
    super(message, cause, enableSuppression, writableStackTrace);
  }

}
