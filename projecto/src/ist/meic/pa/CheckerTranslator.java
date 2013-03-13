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
	        "  ist.meic.pa.CheckerTranslator.checkField(%s);" + // Tirar isto daqui??
	        "  $0.%s = $1;" +
	        "}";

	    final CtClass ctClassTmp = ctClass;

	    for (CtMethod ctMethod : ctClass.getDeclaredMethods()){
	        ctMethod.instrument(new ExprEditor() {
	            public void edit(FieldAccess fa) throws CannotCompileException {
	                if (fa.isWriter()) {
	                    String name = fa.getFieldName();
	                    // falta tratar do null
	                    String expr = parseAssertion(ctClassTmp, name);
	                    String formatTemplate = String.format(template, expr, name);
	                    System.out.println(formatTemplate);
	                    fa.replace(formatTemplate);
	                }
	         	}
	        });
	    }
	}

	private String parseAssertion(CtClass ctClass, String name) {
		try{
			CtField ctField = ctClass.getField(name);
			Assertion a = (Assertion) ctField.getAnnotation(Assertion.class);
			return a.value();
		} catch (NotFoundException e){
			return null;
		} catch (ClassNotFoundException e){
			return null;
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

