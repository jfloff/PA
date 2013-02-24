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

        for(Method m : Class.forName(args[0]).getMethods()){
            // if(m.isAnnotationPresent(Test.class)){
            //     // test for exceptions on the methods
            //     try {
            //        m.invoke(null);
            //        passed++;
            //     } catch (Throwable ex) {
            //        System.out.printf("Test %s failed: %s %n", m, ex.getCause());
            //        failed++;
            //     }
            // }
        }
    }
}
