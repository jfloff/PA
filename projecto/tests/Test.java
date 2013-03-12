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
}
