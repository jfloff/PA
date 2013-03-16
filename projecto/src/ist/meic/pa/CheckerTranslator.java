package ist.meic.pa;

import java.lang.reflect.*;
import java.lang.annotation.*;
import javassist.*;
import javassist.expr.*;

public class CheckerTranslator implements Translator {

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
                            if(hasAssertion(field)){
                                fa.replace("{ $0." + fa.getFieldName() + "=$1;" +
                                    getEvalExprTemplate(getAssertionValue(field)) + " }");
                            }
                        } catch (NotFoundException e){}
                    }
                }
            });
            if(behavior instanceof CtMethod){
                CtMethod m = (CtMethod) behavior;
                if(hasAssertion(m)){
                    String name = m.getName();
                    String template = getEvalExprTemplate(inheritedAssertions(ctClass, name, m.getSignature()));
                    m.setName(name + "$original");
                    m = CtNewMethod.copy(m, name, ctClass, null);
                    m.setBody("return ($r)" + name + "$original($$);");
                    ctClass.addMethod(m);
                    m.insertAfter(template);
                }
            }
        }
    }

    // Transverses the class tree to find out inherited assertions, 'and' chaining them
    private String inheritedAssertions(CtClass ct, String name, String signature) throws CannotCompileException {
        try{
            CtMethod mSuper = ct.getMethod(name, signature);
            if(hasAssertion(mSuper)){
                return getAssertionValue(mSuper) + " && " + inheritedAssertions(ct.getSuperclass(), name, signature);
            }
        } catch(NotFoundException e){}
        return "true";
    }

    private String getEvalExprTemplate(String val){
        return "ist.meic.pa.CheckerTranslator.evalExpr(" + val + ");";
    }

    private boolean hasAssertion(CtMember m){
        return m.hasAnnotation(Assertion.class);
    }

    // Returns an Assertion if there is one, otherwise returns null
    private String getAssertionValue(CtMember m){
        try{
            return ((Assertion) m.getAnnotation(Assertion.class)).value();
        } catch (ClassNotFoundException e){ return null; }
    }

    public static void evalExpr(boolean expr) {
        System.out.println(expr);
    }
}
