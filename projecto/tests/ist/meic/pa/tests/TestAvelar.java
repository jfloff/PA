package ist.meic.pa.tests;
import ist.meic.pa.Assertion;

public class TestAvelar extends SuperC {

	@Assertion("___fo$o__l_$ > 0")
	static int ___fo$o__l_$;
	@Assertion("true")
	static int foo;
	@Assertion("(bar % 2 == 0) && (bar > foo)")
	static long bar;
	@Assertion("baz > (foo + 2)")
	static int baz;
	@Assertion("quux.length()>1")
	static String quux;

	public TestAvelar() {

	}

	@Assertion("($1 % 2) == 0")
	public TestAvelar(int i) {

	}

	@Assertion("true")
	public void test0() {
		try {
			filhoses=-1;
			System.out.println("WRONG");
		} catch(RuntimeException e) {
			System.out.println(e.getMessage());
			System.out.println("OK");
		}
		try {
			filhoses=1;
			System.out.println("OK");
		} catch(RuntimeException e) {
			System.out.println("WRONG");
		}
	}

	public void test01() {
		System.out.println(filhoses);
	}

	public static void main(String[] args) {
		TestAvelar test = new TestAvelar();
		___fo$o__l_$ = 1;

		System.out.println("[1]");
		try {
			System.out.println(foo);
			System.out.println("WRONG");
		} catch(RuntimeException e) {
			System.out.println(e.getMessage());
			System.out.println("OK");
		}

		System.out.println("[2]");
		try {
			test.test01();
			System.out.println("WRONG");
		} catch(RuntimeException e) {
			System.out.println(e.getMessage());
			System.out.println("OK");
		}

		System.out.println("[3]");
		test.test0();

		System.out.println("[4]");
		try {
			bar=1;
			System.out.println("WRONG");
		} catch(RuntimeException e) {
			System.out.println(e.getMessage());
			System.out.println("OK");
		}

		System.out.println("[5]");
		try {
			foo=0;
			bar=2;
			System.out.println("OK");
		} catch(RuntimeException e) {
			System.out.println("WRONG");
		}

		System.out.println("[6]");
		try {
			quux="";
			System.out.println("WRONG");
		} catch(RuntimeException e) {
			System.out.println(e.getMessage());
			System.out.println("OK");
		}

		System.out.println("[7]");
		try {
			quux+="QUUX";
			System.out.println("OK");
		} catch(RuntimeException e) {
			System.out.println(e.getMessage());
			System.out.println("WRONG");
		}

		System.out.println("[8]");
		try {
			quux="QUUX";
			System.out.println("OK");
		} catch(RuntimeException e) {
			System.out.println("WRONG");
		}

		System.out.println("[9]");
		try {
			baz=4;
			System.out.println("OK");
		} catch(RuntimeException e) {
			System.out.println(e.getMessage());
			System.out.println("WRONG");
		}

		System.out.println("[10]");
		try {
			foo=3;
			System.out.println("OK");
		} catch(RuntimeException e) {
			System.out.println("WRONG");
		}

		System.out.println("[11]");
		try {
			baz=-1;
			System.out.println("WRONG");
		} catch(RuntimeException e) {
			System.out.println(e.getMessage());
			System.out.println("OK");
		}

		System.out.println("[12]");
		try {
			baz=5;
			System.out.println("WRONG");
		} catch(RuntimeException e) {
			System.out.println(e.getMessage());
			System.out.println("OK");
		}

		System.out.println("[13]");
		try {
			baz=7;
			System.out.println("OK");
		} catch(RuntimeException e) {
			System.out.println("WRONG");
		}


		SubC sub = new SubC();
		System.out.println("[14]");
		try {
			sub.test1(1, 3);
			System.out.println("OK");
		} catch(RuntimeException e) {
			System.out.println("WRONG");
		}

		System.out.println("[15]");
		try {
			sub.test1(7, 7);
			System.out.println("WRONG");
		} catch(RuntimeException e) {
			System.out.println(e.getMessage());
			System.out.println("OK");
		}

		System.out.println("[16]");
		try {
			sub.test1(1, 6);
			System.out.println("OK");
		} catch(RuntimeException e) {
			System.out.println(e.getMessage());
			System.out.println("WRONG");
		}

		System.out.println("[17]");
		try {
			sub.test1(1);
			System.out.println("WRONG");
		} catch(RuntimeException e) {
			System.out.println(e.getMessage());
			System.out.println("OK");
		}

		System.out.println("[18]");
		try {
			sub.test1(4);
			System.out.println("OK");
		} catch(RuntimeException e) {
			System.out.println(e.getMessage());
			System.out.println("OK");
		}

		SubC.SubSubC subsub = new SubC.SubSubC();

		System.out.println("[19]");
		try {
			subsub.test1(4,1);
			System.out.println("OK");
		} catch(RuntimeException e) {
			System.out.println("WRONG!");
		}

		System.out.println("[20]"); //<--
		try {
			subsub.test1(2,2);
			System.out.println("WRONG");
		} catch(RuntimeException e) {
			System.out.println(e.getMessage());
			System.out.println("OK");
		}

		System.out.println("[21]");
		try {
			subsub.test1(2,-3);
			System.out.println("WRONG");
		} catch(RuntimeException e) {
			System.out.println(e.getMessage());
			System.out.println("OK");
		}

		System.out.println("[22]");//<---
		try {
			subsub.test1(2,4);
			System.out.println("WRONG");
		} catch(RuntimeException e) {
			System.out.println(e.getMessage());
			System.out.println("OK");
		}

		System.out.println("[23]");//<---
		try {
			subsub.test1(1);
			System.out.println("WRONG");
		} catch(RuntimeException e) {
			System.out.println(e.getMessage());
			System.out.println("OK");
		}

		System.out.println("[24]");
		try {
			subsub.test1(-1);
			System.out.println("WRONG");
		} catch(RuntimeException e) {
			System.out.println(e.getMessage());
			System.out.println("OK");
		}

		System.out.println("[25]");
		try {
			subsub.test1(0);
			System.out.println("WRONG");
		} catch(RuntimeException e) {
			System.out.println(e.getMessage());
			System.out.println("OK");
		}

		System.out.println("[26]"); //<---
		try {
			subsub.test1(2);
			System.out.println("WRONG");
		} catch(RuntimeException e) {
			System.out.println(e.getMessage());
			System.out.println("OK");
		}

		System.out.println("[27]");
		try {
			subsub.test2();
			System.out.println("WRONG");
		} catch(RuntimeException e) {
			System.out.println(e.getMessage());
			System.out.println("OK");
		}

		System.out.println("[28]");//<---
		try {
			subsub.test2(3);
			System.out.println("WRONG");
		} catch(RuntimeException e) {
			System.out.println(e.getMessage());
			System.out.println("OK");
		}

		System.out.println("[29]");
		try {
			subsub.test2(4);
			System.out.println("OK");
		} catch(RuntimeException e) {
			System.out.println("WRONG");
		}

		SuperC MrC = new SuperC();

		System.out.println("[30]");
		try { MrC.test1(1,2);
			System.out.println("OK");
		} catch(RuntimeException e) {
			System.out.println("WRONG");
		}

		System.out.println("[31]");
		try { MrC.test1(1,1);
			System.out.println("WRONG");
		} catch(RuntimeException e) {
			System.out.println(e.getMessage());
			System.out.println("OK");
		}

		System.out.println("[32]");
		try {
			TestAvelar test2 = new TestAvelar(1);
			System.out.println("WRONG");
		} catch(RuntimeException e) {
			System.out.println(e.getMessage());
			System.out.println("OK");
		}

		System.out.println("[33]");
		try {
			TestAvelar test3 = new TestAvelar(-1);
			System.out.println("WRONG");
		} catch(RuntimeException e) {
			System.out.println(e.getMessage());
			System.out.println("OK");
		}

		System.out.println("[34]");
		try {
			TestAvelar test4 = new TestAvelar(2);
			System.out.println("OK");
		} catch(RuntimeException e) {
			System.out.println("WRONG");
		}
	}
}
