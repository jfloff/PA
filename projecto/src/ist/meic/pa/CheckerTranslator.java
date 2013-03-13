package ist.meic.pa;

import java.lang.reflect.*;
import java.lang.annotation.*;
import javassist.*;
import javassist.expr.*;

public class CheckerTranslator implements Translator {

	final String template =
	        "{" +
	        "  $0.%s = $1;" +
	        "  ist.meic.pa.CheckerTranslator.evalExpr(%s);" +
	        "}";

	public void start(ClassPool pool) throws NotFoundException, CannotCompileException {}

	public void onLoad(ClassPool pool, String className) throws NotFoundException, CannotCompileException {
		CtClass cc = pool.get(className);
		checkBehaviors(cc);
	}

    public void checkBehaviors(CtClass ctClass) throws NotFoundException, CannotCompileException {
	    for (CtBehavior behavior : ctClass.getDeclaredBehaviors()){
	    	// checkBehaviorAnnottation(behavior);
	    	behavior.getLongName();
	    }
	}

	private void checkBehaviorAnnottation(CtBehavior member) throws CannotCompileException {
		 member.instrument(new ExprEditor() {
            public void edit(FieldAccess fa) throws CannotCompileException {
                if (fa.isWriter()) {
					try{
                    	CtField field = fa.getField();
	                    if(field.hasAnnotation(Assertion.class)){
	                    	Assertion a = (Assertion) field.getAnnotation(Assertion.class);
		                    fa.replace(String.format(template, fa.getFieldName(), a.value()));
	                    }
                    }
                    catch (NotFoundException e){}
                    catch (ClassNotFoundException e){}
                }
         	}
        });
	}

	public static void evalExpr(boolean expr) {
        System.out.println(expr);
    }
}
