package ist.meic.pa.tests;
import ist.meic.pa.Assertion;

public class Basic extends Base {

    static class SubBasic extends Basic {

        @Override
        @Assertion("true")
        public void test() {
            System.out.println("SubBasic.test()");
        }
    }


    @Assertion("false")
    public void test() {
        System.out.println("Basic.test()");
    }
}
