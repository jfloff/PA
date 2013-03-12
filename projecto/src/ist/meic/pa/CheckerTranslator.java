package ist.meic.pa;

import java.lang.reflect.*;
import java.lang.annotation.*;
import javassist.*;

public class CheckerTranslator implements Translator {

	public void start(ClassPool pool)
		throws NotFoundException, CannotCompileException {
	// Do noting
	}
	public void onLoad(ClassPool pool, String className)
		throws NotFoundException, CannotCompileException {

		// Obtain the compile time class
		CtClass cc = pool.get(className);
		// Modify the class


		// That's all. The class will now be automatically
		// loaded from the modified byte code
	}

}

