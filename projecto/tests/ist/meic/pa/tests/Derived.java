package ist.meic.pa.tests;
import ist.meic.pa.Assertion;

public class Derived extends Base {

    @Override
    @Assertion("($1%2==1) && ($_%2==1)")
    public int fooBar(int x) {
        return x+1;
    }
}
