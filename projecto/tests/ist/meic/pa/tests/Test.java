package ist.meic.pa.tests;
import ist.meic.pa.Assertion;

public class Test {
    @Assertion("foo>0")
    static int foo=1;

    @Assertion("bar%2==0")
    static long bar;

    @Assertion("baz>foo")
    static int baz;

    @Assertion("quux.length()>1")
    static String quux;

    public static void main(String[] args) throws Exception, Throwable {

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
}
