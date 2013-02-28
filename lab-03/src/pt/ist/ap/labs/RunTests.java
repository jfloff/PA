package pt.ist.ap.labs;
import java.lang.reflect.*;

public class RunTests {

    public static void main(String[] args) throws Exception {
        // command line parameter check
        if(args.length != 1) {
            System.err.println("[ERROR] Invalid command line: one and one only filename expected");
            System.exit(1);
        }

        int passed = 0, failed = 0;

        for(Method m : Class.forName(args[0]).getDeclaredMethods()){
            if(m.isAnnotationPresent(Test.class)){
                try {
                    Test t = (Test) m.getAnnotation(Test.class);
                    for (String name : t.value()) {
                        for (Method mSetup : Class.forName(args[0]).getDeclaredMethods()) {
                            if(mSetup.isAnnotationPresent(Setup.class)){
                                Setup s = (Setup) mSetup.getAnnotation(Setup.class);
                                if(name.equals("*") || s.value().equals(name)){
                                    mSetup.setAccessible(true);
                                    mSetup.invoke(null);
                                }
                            }
                        }
                    }

                    m.setAccessible(true);
                    m.invoke(null);
                    System.out.println("Test " + m + " OK!");
                    passed++;
                } catch (Throwable ex) {
                    System.out.println("Test " + m + " FAILED!");
                    failed++;
                }
            }
        }

        System.out.println("Passed: " + passed + ", Failed: " + failed);
    }
}
