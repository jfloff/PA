package ist.meic.pa.tests;
import ist.meic.pa.Assertion;

public class SuperC {

	@Assertion("filhoses>0")
	protected int filhoses;

	//@Assertion("true")
	protected int _t1 = 0;

	private int _t2 = 4;

	public SuperC() {

	}

	@Assertion("$1 > 0")
	public SuperC(int i2) {

	}

	@Assertion("$1 > 1")
	public int test1(int i1) {
		return i1++;
	}

	@Assertion("$1 != $2")
	public int test1(int i1, int i2) {
		return i1 + i2;
	}

	@Assertion("($_ % 5) == 0")
	public int test2() {
		return ++_t2;
	}

	@Assertion("($_ % 3) == 0")
	public int test2(int i) {
		return i++;
	}
}
