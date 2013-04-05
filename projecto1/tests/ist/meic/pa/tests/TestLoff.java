package ist.meic.pa.tests;
import ist.meic.pa.Assertion;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class TestLoff {

    // abstract class HelloWorld {
    //     abstract public void greetSomeone(String someone);

    //     @Assertion("$1.length()>1")
    //     public void greet(String name){
    //         System.out.println("HAI" + name);
    //     }
    // }

    // class Spanish extends HelloWorld {
    //     String name = "mundo";

    //     // @Assertion("$1.length()>1")
    //     public void greetSomeone(String someone) {
    //         name = someone;
    //         System.out.println("Hola, " + name);
    //         super.greet(someone);
    //     }
    // }

    // @Assertion("true")
    // int foo;

    // @Assertion("baz>foo")
    // int baz;

    // @Assertion("bar%2==0")
    // long bar;

    // @Assertion("quux.length()>1")
    // String quux;

    // Spanish spanish;

    // int[] array = new int[5];

    // {
    //     array[0] = 1;
    //     baz = 3;
    //     foo++;
    //     foo=2;
    //     foo=dub(foo);
    //     foo=dub(foo);
    //     bar=2;
    //     baz=3;
    //     bar+=2;
    //     quux="foo";
    //     bar++;
    //     spanish = new Spanish();
    // }

    // @Assertion("$_==($1*2)")
    // public static int dub(int x){
    //     return 2*x;
    // }

    // @Assertion("!$1.isEmpty()")
    // public static void m1(ArrayList l){
    //     l.clear();
    // }

    // @Assertion("!$1.isEmpty()")
    // public static void m2(ArrayList l){
    //     l = new ArrayList();
    // }

    // public static void mTest(){
    //     ArrayList<Integer> array = new ArrayList<Integer>();

    //     array.add(1);
    //     array.add(2);
    //     m1(array);

    //     array.add(1);
    //     array.add(2);
    //     m2(array);
    // }

    // public void testing(){
    //     // Testing foo
    //     foo = 0;
    //     foo = 1;

    //     // Testing bar
    //     bar = 4;
    //     bar = 3;

    //     // Testing baz
    //     foo = 1;
    //     baz = 2;
    //     baz = 1;

    //     // Testing quux
    //     quux = "aa";
    //     quux = "a";
    // }

    // // @Assertion("($1>=0) && ($_>$1)")
    // public int methodTest(int x){
    //     return ++x; // return TRUE
    //     // return x++; // return FALSE
    // }

    public static void main(String[] args) throws Exception, Throwable {

        // TestLoff t = new TestLoff();
        // t.spanish.greetSomeone("aa");

        // t.testing();

        // Base b = new Base();
        // b.fooBar(1);

        // Derived d = new Derived();
        // d.fooBar(0);

        // StaticCons sc = new StaticCons();
        // Cons c = new Cons(1);
        // c = new Cons("o");

        // try{
        //     new Metho().m();
        // } catch (NullPointerException e){
        //     System.out.println("------>NPE<-----");
        // }

        Basic basic = new Basic();
        // basic.test();

        Basic.SubBasic subBasic = new Basic.SubBasic();
        subBasic.test();
    }
}
