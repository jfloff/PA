package ist.meic.pa;

import java.lang.reflect.*;
import java.lang.annotation.*;
import javassist.*;

public class CheckAssertions {

    public static void main(String[] args) throws Exception, Throwable {
        if(args.length != 1) {
            System.err.println("[ERROR] Invalid command line: one and one only filename expected");
            System.exit(1);
        }

        Translator translator = new CheckerTranslator();
        ClassPool pool = ClassPool.getDefault();
        Loader classLoader = new Loader();
        classLoader.addTranslator(pool, translator);
        String[] restArgs = new String[args.length - 1];
        System.arraycopy(args, 1, restArgs, 0, restArgs.length);
        classLoader.run(args[0], restArgs);
    }
}
