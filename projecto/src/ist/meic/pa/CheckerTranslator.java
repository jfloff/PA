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
        String setTemplate = "static java.util.HashSet $writes = new java.util.HashSet();";
        for (CtBehavior behavior : ctClass.getDeclaredBehaviors()){
            // adds $writes hash to current class
            if(getFieldByClassAndName(ctClass, "$writes") == null){
                CtField writesField = CtField.make(setTemplate, ctClass);
                ctClass.addField(writesField);
            }
            // goes trough all field accesses
            behavior.instrument(new ExprEditor() {
                public void edit(FieldAccess fa) throws CannotCompileException {
                    CtField field = getFieldByFieldAccess(fa);
                    if((field != null) && hasAssertion(field)){
                        if (fa.isReader()){
                            fa.replace("{ "
                                + getInitTemplate("($writes.contains(($w) " + field.hashCode() + "))", field.getName())
                                + "$_ = $proceed(); }");
                        }
                        if (fa.isWriter()) {
                            fa.replace("{ $writes.add(($w) " + field.hashCode() + ");"
                                + "$proceed($$);"
                                + getEvalExprTemplate(getAssertionValue(field)) + " }");
                        }
                    }
                }
            });
            // if its a method and its not declared in an abstract or interface class
            if(!javassist.Modifier.isInterface(ctClass.getModifiers()) && (behavior instanceof CtMethod)){
                CtMethod m = (CtMethod) behavior;
                String name = m.getName();
                String expr = inheritedAssertions(ctClass, name, m.getSignature());
                // if method has assertions in class tree
                if(!expr.equals("(true)")){
                    m.setName(name + "$orig");
                    m = CtNewMethod.copy(m, name, ctClass, null);
                    m.setBody("return ($r)" + name + "$orig($$);");
                    ctClass.addMethod(m);
                    m.insertAfter(getEvalExprTemplate(expr));
                }
            }
        }
    }

    // Transverses the class tree to find out inherited assertions '&&' chaining them
    private String inheritedAssertions(CtClass ct, String name, String signature) throws CannotCompileException {
        if(ct != null){
            CtMethod mSuper = getMethod(ct, name, signature);
            String append = ((mSuper != null) && hasAssertion(mSuper)) ? getAssertionValue(mSuper) + " && " : "";
            return append + inheritedAssertions(getSuperclass(ct), name, signature);
        } else {
            return "(true)";
        }
    }

    private String getInitTemplate(String val, String fieldName){
        return exceptionTemplate(val, "\"Error: " + fieldName + " was not initialized\"");
    }

    private String getEvalExprTemplate(String val){
        return exceptionTemplate(val, "\"The assertion \" + \""+ val + "\" + \" is \" + (" + val + ")");
    }

    private String exceptionTemplate(String val, String msg){
        // return "if(!" + val + ") throw new RuntimeException(" + msg + ");";
        return "System.out.println(" + msg + ");";
    }

    private boolean hasAssertion(CtMember m){
        return m.hasAnnotation(Assertion.class);
    }

    // Returns an Assertion if there is one, otherwise returns null
    private String getAssertionValue(CtMember m){
        try{
            return "(" + ((Assertion) m.getAnnotation(Assertion.class)).value() + ")";
        } catch (ClassNotFoundException e){
            return null;
        }
    }

    private CtField getFieldByFieldAccess(FieldAccess fa){
        try{
            return fa.getField();
        } catch (NotFoundException e){
            return null;
        }
    }

    private CtField getFieldByClassAndName(CtClass ct, String name){
        try{
            return ct.getField(name);
        } catch (NotFoundException e){
            return null;
        }
    }

    private CtMethod getMethod(CtClass ct, String name, String signature){
        try{
            return ct.getMethod(name, signature);
        } catch (NotFoundException e){
            return null;
        }
    }

    private CtClass getSuperclass(CtClass ct){
        try{
            return ct.getSuperclass();
        } catch (NotFoundException e){
            return null;
        }
    }
}
