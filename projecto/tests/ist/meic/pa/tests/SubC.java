package ist.meic.pa.tests;
import ist.meic.pa.Assertion;

public class SubC extends SuperC {

	static class SubSubC extends SubC {

		int filhoses;

		{
			filhoses = -1; //TEM DE MANDAR RTE se for acesso a super
			//System.out.println("NESTUM: "+nestum);
		}

		//@Assertion("milho < 5")
		int milho;

		@Assertion("$1 > 0")
		public int test1(int i1) {
			return i1*i1;
		}

		@Assertion("($1 + $2) > 0")
		public int test1(int i1, int i2) {
			System.out.println((i1 + i2) > 0);
			System.out.println((i2 % 2) == 1);
			//this.test1(i1);
			//super.test1(i2);
			//System.out.println(arroz);
			//super.arroz++;
			//System.out.println(arroz);
			//milho++;
			return i1*2 + i2;
		}

		@Assertion("$_ > 0")
		public int test2() {
			return _t1++;
		}

		public int test2(int i) {
			return --i;
		}
	}

	@Assertion("filhoses<0")
	protected int filhoses;

	//@Assertion("(nestum % 2) == 1")
	int nestum;

	{
		//System.out.println(filhoses);
	}

	//@Assertion("arroz<0")
	public int arroz = -1;

	@Assertion("($1 % 2) == 1")
	public int test1(int i1) {
		return i1--;
	}

	@Assertion("($2 % 2) == 1")
	public int test1(int i1, int i2) {
		return i1*2 + i2;
	}

	public int test2() {
		return _t1--;
	}
}
