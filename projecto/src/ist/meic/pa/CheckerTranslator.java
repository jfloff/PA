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
	    	behavior.instrument(new ExprEditor() {
	    		Assertion a;
				public void edit(FieldAccess fa) throws CannotCompileException {
	                if (fa.isWriter()) {
	                	try{
	                    	CtField field = fa.getField();
		                    if((a = getAssertion(field)) != null){
			                    fa.replace(String.format(template, fa.getFieldName(), a.value()));
		                    }
	                	} catch (NotFoundException e){}
	                }
	         	}
	        });
	        Assertion a;
	        if((a = getAssertion(behavior)) != null){
		        if(behavior instanceof CtMethod){
		        	behavior.insertAfter("System.out.println(\"OLA METODO\");");
		        }
	        }
	    }
	}

	// Returns an Assertion if there is on, otherwise returns null
	private Assertion getAssertion(CtMember member){
		try{
			return (Assertion) member.getAnnotation(Assertion.class);
		} catch (ClassNotFoundException e){ return null; }
	}

	public static void evalExpr(boolean expr) {
        System.out.println(expr);
    }
}
