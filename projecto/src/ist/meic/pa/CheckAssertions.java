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
            for(Field f : c.getDeclaredFields()){
                for (Annotation a : f.getAnnotations()) {
                    String classname = a.annotationType().getName();
                    System.out.println("FIELD ANNOT NAME = " + classname);
                }
            }
            for(Method m : c.getDeclaredMethods()){
                for (Annotation a : m.getAnnotations()) {
                    String classname = a.annotationType().getName();
                    System.out.println("METHOD ANNOT NAME = " + classname);
                }
            }
        }
    }
}
