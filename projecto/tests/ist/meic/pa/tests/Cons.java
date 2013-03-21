package ist.meic.pa.tests;
import ist.meic.pa.Assertion;

class Cons extends SuperCons{

    int x;

    @Assertion("x>0")
    public Cons(int x){
        System.out.println("COOOONS");
    }

    @Assertion("x>0")
    public Cons(int x, int y){
        System.out.println("COOOONS");
    }

    @Assertion("$1.length() > 1")
    public Cons(String x){
        super(x);
        System.out.println(x);
    }
}
