package de.laubfall.apnoe.dummy;

public class IfElseSample
{

  public static void main(String[] args)
  {
    String b = "b";
    if(b.equals("a")) {
      System.out.println("a");
    } else if(b.equals("b")) {
      System.out.println("b");
    } else if(b.equals("c")) {
      System.out.println("c");
    }
    
    if(b.equals("d")) {
      System.out.println("d");
    } else {
      System.out.println("whatever");
    }
  }

}
