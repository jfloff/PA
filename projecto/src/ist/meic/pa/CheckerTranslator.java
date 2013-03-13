package ist.meic.pa;

import java.lang.reflect.*;
import java.lang.annotation.*;
import javassist.*;
import javassist.expr.*;

public class CheckerTranslator implements Translator {

	public void start(ClassPool pool) throws NotFoundException, CannotCompileException {}

	public void onLoad(ClassPool pool, String className) throws NotFoundException, CannotCompileException {
		CtClass cc = pool.get(className);
		checkerField(cc);
	}

    public void checkerField(CtClass ctClass) throws NotFoundException, CannotCompileException {
	    final String template =
	        "{" +
	        "  $0.%s = $1;" +
	        "  ist.meic.pa.CheckerTranslator.checkField(%s);" +
	        "}";

	    // PERCORRER O RESTO§
	    for (CtMethod ctMethod : ctClass.getDeclaredMethods()){
	        ctMethod.instrument(new ExprEditor() {
	            public void edit(FieldAccess fa) throws CannotCompileException {
	                if (fa.isWriter()) {
						try{
							String fieldName = fa.getFieldName();
	                    	CtField field = fa.getField();
		                    if(field.hasAnnotation(Assertion.class)){
		                    	Assertion a = (Assertion) field.getAnnotation(Assertion.class);
			                    String formatTemplate = String.format(template, fieldName, a.value());
			                    fa.replace(formatTemplate);
		                    }
	                    } catch (NotFoundException e){}
	                    catch (ClassNotFoundException e){}
	                }
	         	}
	        });
	    }
	}

	public static void checkField(boolean expr) {
        if(expr){
            System.out.println("TRUEE");
        } else {
            System.out.println("FAALSE");
        }
    }
}

