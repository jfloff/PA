package ist.meic.pa.tests;
import ist.meic.pa.Assertion;

class Base {

    @Assertion("true")
    protected int foo;

    @Assertion("($1>=0) && ($_>$1)")
    public int fooBar(int x) {
        return ++x;
    }

    public void inc(){
        foo++;
    }

    public void init(){
        foo = 1;
    }
}
