package ist.meic.pa.tests;
import ist.meic.pa.Assertion;

public class Test {
    @Assertion("foo>0")
    int foo;

    @Assertion("bar%2==0")
    long bar;

    @Assertion("baz>foo")
    int baz;

    @Assertion("quux.length()>1")
    String quux;

    {
        foo++;
        bar=2;
        baz=3;
        bar+=2;
        quux="foo";
        bar++;
    }

    public void testing(int x){
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

    @Assertion("($1>=0) && ($_>$1)")
    public int methodTest(int x){
        return ++x; // return TRUE
        // return x++; // return FALSE
    }

    public static void main(String[] args) throws Exception, Throwable {

        Test t = new Test();

        t.methodTest(1);
    }
}
