package ist.meic.pa;

import java.lang.reflect.*;
import java.lang.annotation.*;

public class CheckAssertions {
    static int passed = 0, failed = 0;
    public static void main(String[] args) throws Exception {
        if(args.length != 1) {
            System.err.println("[ERROR] Invalid command line: one and one only filename expected");
            System.exit(1);
        }
        parseAssertions(Class.forName(args[0]));
    }

    private static void parseAssertions(Class c){
        if (c.getSuperclass() != null){
            parseAssertions(c.getSuperclass());
            for(Method m : c.getDeclaredMethods()){
                Annotation[] annotations = m.getAnnotations();
                for (Annotation a : annotations) {
                    String classname = a.annotationType().getName();
                    System.out.println("ANNOT NAME = " + classname);
                }
            }
        }
    }
}
