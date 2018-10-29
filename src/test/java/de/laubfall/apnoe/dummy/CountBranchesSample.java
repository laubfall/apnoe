package de.laubfall.apnoe.dummy;

public class CountBranchesSample
{

  public static void main(String[] args)
  {
    final CountBranchesSample cbs = new CountBranchesSample();
    cbs.a();
    cbs.b();
    cbs.ab();
    cbs.z();
  }

  public void a() {
    System.out.println("a");
  }
  
  public void b() {
    System.out.println("b");
  }
  
  public void ab() {
    a();
    b();
  }
  
  public void z() {
    a();
    ab();
  }
}
