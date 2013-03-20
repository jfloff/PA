package ist.meic.pa.tests;
import ist.meic.pa.Assertion;

public class Test {

    abstract class HelloWorld {
        abstract public void greetSomeone(String someone);

        @Assertion("$1.length()>1") // --> ESTE FALHA
        public void greet(String name){
            System.out.println("HAI" + name);
        }
    }

    class Spanish extends HelloWorld {
        String name = "mundo";

        // @Assertion("$1.length()>1")
        public void greetSomeone(String someone) {
            name = someone;
            System.out.println("Hola, " + name);
            super.greet(someone);
        }
    }

    @Assertion("foo>0")
    public int foo;

    @Assertion("bar%2==0")
    long bar;

    @Assertion("baz>foo")
    int baz;

    @Assertion("quux.length()>1")
    String quux;

    Spanish spanish;

    {
        // foo = 2;
        // foo = dub(foo);
        // foo = dub(foo);
        // foo = dub(foo);
        // bar=2;
        // baz=3;
        // bar+=2;
        // quux="foo";
        // bar++;
        spanish = new Spanish();
    }

    @Assertion("$_==($1*2)")
    public static int dub(int x){
        return 2*x;
    }

    public void testing(){
        // Testing foo
        foo = 0;
        foo = 1;

        // Testing bar
        bar = 4;
        bar = 3;

        // Testing baz
        foo = 1;
        baz = 2;
        baz = 1;

        // Testing quux
        quux = "aa";
        quux = "a";
    }

    // @Assertion("($1>=0) && ($_>$1)")
    public int methodTest(int x){
        return ++x; // return TRUE
        // return x++; // return FALSE
    }

    public static void main(String[] args) throws Exception, Throwable {

        Test t = new Test();
        t.spanish.greetSomeone("aa");

        // t.testing();

        // Base b = new Base();
        // b.fooBar(1);

        // Derived d = new Derived();
        // d.fooBar(0);

        // Spanish p = new Spanish();
        // p.greetSomeone("m");
    }
}
