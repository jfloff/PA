package ist.meic.pa;

import java.lang.reflect.*;
import java.lang.annotation.*;
import javassist.*;
import javassist.expr.*;

public class CheckerTranslator implements Translator {

    final String methodTemplate = "ist.meic.pa.CheckerTranslator.evalExpr(%s);";

    public void start(ClassPool pool) throws NotFoundException, CannotCompileException {}

    public void onLoad(ClassPool pool, String className) throws NotFoundException, CannotCompileException {
        CtClass cc = pool.get(className);
        checkBehaviors(cc);
    }

    public void checkBehaviors(CtClass ctClass) throws NotFoundException, CannotCompileException {
        for (CtBehavior behavior : ctClass.getDeclaredBehaviors()){
            behavior.instrument(new ExprEditor() {
                public void edit(FieldAccess fa) throws CannotCompileException {
                    if (fa.isWriter()) {
                        try{
                            CtField field = fa.getField();
                            Assertion a;
                            if((a = getAssertion(field)) != null){
                                final String faTemplate =   "{" +
                                                            "  $0.%s = $1;" +
                                                            "  ist.meic.pa.CheckerTranslator.evalExpr(%s);" +
                                                            "}";
                                fa.replace(String.format(faTemplate, fa.getFieldName(), a.value()));
                            }
                        } catch (NotFoundException e){}
                    }
                }
            });
            Assertion a;
            if(behavior instanceof CtMethod){
                if((a = getAssertion(behavior)) != null){
                    CtMethod m = (CtMethod) behavior;
                    String name = m.getName();
                    m.setName(name + "$original");
                    m = CtNewMethod.copy(m, name, ctClass, null);
                    m.setBody("return ($r)" + name + "$original($$);");
                    ctClass.addMethod(m);
                    m.insertAfter("ist.meic.pa.CheckerTranslator.evalExpr(" + a.value() + ");");
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
