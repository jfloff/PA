package ist.meic.pa;

import java.lang.reflect.*;
import java.lang.annotation.*;
import javassist.*;
import javassist.expr.*;

public class CheckerTranslator implements Translator {

	public void start(ClassPool pool)
		throws NotFoundException, CannotCompileException {
	// Do noting
	}
	public void onLoad(ClassPool pool, String className)
		throws NotFoundException, CannotCompileException {

		CtClass cc = pool.get(className);
		checkerField(cc);
	}

    public void checkerField(CtClass ctClass) throws NotFoundException, CannotCompileException {
	    final String template =
	        "{" +
	        "  Checker.checkField();" +
	        "  $0.%s = $1;" +
	        "}";
	    for (CtMethod ctMethod : ctClass.getDeclaredMethods()) {
	        ctMethod.instrument(new ExprEditor() {
	            public void edit(FieldAccess fa) throws CannotCompileException {
	                    if (fa.isWriter()) {
	                        String name = fa.getFieldName();
	                        fa.replace(String.format(template, name));
	                    }
	         	}
	        });
	    }
	}
}

