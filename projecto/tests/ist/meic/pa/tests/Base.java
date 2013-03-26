package ist.meic.pa.tests;
import ist.meic.pa.Assertion;

class Base {

    @Assertion("foo>0")
    int foo;

    @Assertion("($1>=0) && ($_>$1)")
    public int fooBar(int x) {
        return ++x;
    }

    @Assertion("true")
    public void test(){

    }
}
