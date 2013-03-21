package ist.meic.pa.tests;
import ist.meic.pa.Assertion;

class Metho {

    Object o = new Object();

    public Object getAndClear() {
        Object old = o;
        o = null;
        return old;
    }

    @Assertion(
        value="$1 != getAndClear()",
        entry="true"
    )
    public void m(Object o) {
        o.toString();
    }
}
