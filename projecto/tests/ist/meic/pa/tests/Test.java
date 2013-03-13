package ist.meic.pa.tests;
import ist.meic.pa.Assertion;

public class Test {
    @Assertion("foo>0")
    int foo=1;

    @Assertion("bar%2==0")
    long bar;

    @Assertion("baz>foo")
    int baz;

    @Assertion("quux.length()>1")
    String quux;

    public static void main(String[] args) throws Exception, Throwable {

        Test t = new Test();

        // Testing foo
        t.foo = 0;
        t.foo = 1;

        // Testing bar
        t.bar = 4;
        t.bar = 3;

        // Testing baz
        t.foo = 1;
        t.baz = 2;
        t.baz = 1;

        // Testing quux
        t.quux = "aa";
        t.quux = "a";
    }
}
