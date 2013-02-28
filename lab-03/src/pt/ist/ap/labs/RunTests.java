package pt.ist.ap.labs;
import java.lang.reflect.*;

public class RunTests {
    static int passed = 0, failed = 0;
    public static void main(String[] args) throws Exception {
        if(args.length != 1) {
            System.err.println("[ERROR] Invalid command line: one and one only filename expected");
            System.exit(1);
        }
        runTests(Class.forName(args[0]));
        System.out.println("Passed: " + passed + ", Failed: " + failed);
    }
    private static void runTests(Class c){
        if (c.getSuperclass() != null){
            runTests(c.getSuperclass());
            for(Method m : c.getDeclaredMethods()){
                if(m.isAnnotationPresent(Test.class)){
                    try {
                        Test t = (Test) m.getAnnotation(Test.class);
                        for (String name : t.value()) {
                            findSetup(c, name);
                        }
                        invokeMethod(m);
                        passed++;
                        System.out.println("Test " + m + " OK!");
                    } catch (Throwable ex) {
                        failed++;
                        System.out.println("Test " + m + " FAILED!");
                    }
                }
            }
        }
    }
    private static void findSetup(Class c, String name) throws Throwable {
        if (c.getSuperclass() != null){
            findSetup(c.getSuperclass(), name);
            for (Method mSetup : c.getDeclaredMethods()) {
                if(mSetup.isAnnotationPresent(Setup.class)){
                    Setup s = (Setup) mSetup.getAnnotation(Setup.class);
                    if(name.equals("*")){
                        invokeMethod(mSetup);
                        continue;
                    }
                    if(s.value().equals(name)){
                        invokeMethod(mSetup);
                        return ;
                    }
                }
            }
        }
    }
    private static void invokeMethod(Method m) throws Throwable {
        m.setAccessible(true);
        m.invoke(null);
    }
}
