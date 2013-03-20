package ist.meic.pa.tests;
import ist.meic.pa.Assertion;

public class Derived extends Base {

    @Override
    @Assertion("($1%2==0) && ($_%2==1)")
    public int fooBar(int x) {
        super.foo++;
        return x+1;
    }
}
