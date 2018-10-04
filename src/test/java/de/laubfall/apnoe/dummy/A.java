package de.laubfall.apnoe.dummy;

public class A
{
  public static void main(String[] args)
  {
    A a = new A();
    a.doSomethingMore();
    a.doSomethingMore("fjdksl");
    String blah = "test";
    a.doSomethingMore(blah);
    
    if(blah == "blubb") {
      a.doSomethingMore("if");
    } else {
      a.doSomethingMore("else");
    }
    
    A.doSomethingInStaticContext();
  }

  public void doSomethingMore()
  {
    B b = new B();
    b.doSomething();
  }
  
  private void doSomethingMore(String b) {
    doSomethingMore();
  }
  
  private static void doSomethingInStaticContext() {
    new C().doSomeMinorStuff();
  }
}
