package ist.meic.pa;
import java.lang.reflect.*;
import java.lang.annotation.*;
import javassist.*;
import javassist.expr.*;
import java.util.Arrays;

public class CheckerTranslator implements Translator {

    public void start(ClassPool pool) throws NotFoundException, CannotCompileException {}

    public void onLoad(ClassPool pool, String className) throws NotFoundException, CannotCompileException {
        CtClass cc = pool.get(className);
        checkBehaviors(cc);
        reCheckFields(cc);
    }

    public void checkBehaviors(CtClass ctClass) throws NotFoundException, CannotCompileException {
        for (CtBehavior behavior : ctClass.getDeclaredBehaviors()){
            checkFieldAccess(ctClass, behavior);
            // if its a method and its not declared in an abstract or interface class
            if(!javassist.Modifier.isInterface(ctClass.getModifiers()) && (behavior instanceof CtMethod)){
                CtMethod m = (CtMethod) behavior;
                String name = m.getName();
                String[] templates = inheritedAssertions(ctClass, name, m.getSignature());
                if(!Arrays.equals(templates, new String[]{"", ""})){
                    String newName = "m$" + Math.abs(m.hashCode());
                    m.setName(newName);
                    m = CtNewMethod.copy(m, name, ctClass, null);
                    m.setBody("return ($r)" + newName + "($$);");
                    ctClass.addMethod(m);
                    m.insertBefore(templates[1]);
                    m.insertAfter(templates[0]);
                }
            }
            if((behavior instanceof CtConstructor) && hasAssertion(behavior)){
                CtConstructor c = (CtConstructor) behavior;
                c.insertBeforeBody(getExprTemplate(getAssertionValue(c)));
            }
        }
    }

    final String setTemplate = "static java.util.HashSet f$writes = new java.util.HashSet();";
    private void checkFieldAccess(CtClass ct, CtBehavior behavior) throws NotFoundException, CannotCompileException {
        // adds field access 'f$writes' hash to current class to check variable access
        if(getFieldByClassAndName(ct, "f$writes") == null){
            CtField writesField = CtField.make(setTemplate, ct);
            ct.addField(writesField);
        }
        // injects code to check assertion and initialization
        behavior.instrument(new ExprEditor() {
            public void edit(FieldAccess fa) throws CannotCompileException {
                CtField field = getFieldByFieldAccess(fa);
                if((field != null) && hasAssertion(field)){
                    if (fa.isReader()){
                        fa.replace(getInitTemplate("f$writes.contains(($w) " + field.hashCode() + ")", field.getName())
                            + "$_ = $proceed();");
                    }
                    if (fa.isWriter()) {
                        fa.replace( "$proceed($$);"
                            + "f$writes.add(($w) " + field.hashCode() + ");"
                            + getExprTemplate(getAssertionValue(field)));
                    }
                }
            }
        });
    }

    private void reCheckFields(CtClass ctClass) throws NotFoundException, CannotCompileException {
        for (CtBehavior behavior : ctClass.getDeclaredBehaviors()){
            checkFieldAccess(ctClass, behavior);
        }
    }

    // Returns array with all class-tree value assertions in the first position and entry in the second position
    private String[] inheritedAssertions(CtClass ct, String name, String signature){
        if(ct != null){
            String[] result = inheritedAssertions(getSuperclass(ct), name, signature);
            CtMethod m = getMethod(ct, name, signature);
            if((m != null) && hasAssertion(m)){
                result[0] += getExprTemplate(getAssertionValue(m));
                result[1] += getEntryTemplate(getAssertionValue(m));
            }
            return result;
        }
        return new String[] {"", ""};
    }

    private String getInitTemplate(String val, String fieldName){
        return masterTemplate(val, "\"Error: " + fieldName + " was not initialized\"");
    }

    final String msgTemplate = "\"The assertion \" + \"%s\" + \" is \" + (%s)";
    private String getExprTemplate(String[] val){
        return masterTemplate(val[0], String.format(msgTemplate, val[0], val[0]));
    }
    private String getEntryTemplate(String[] val){
        return masterTemplate(val[1], String.format(msgTemplate, val[1], val[1]));

    }

    private String masterTemplate(String val, String msg){
        // return "if(!" + val + ") throw new RuntimeException(" + msg + ");";
        return "if(!(" + val + ")) System.out.println(" + msg + "); ";
    }

    private boolean hasAssertion(CtMember m){
        return m.hasAnnotation(Assertion.class);
    }

    // Returns an Assertion if there is one, otherwise returns null
    private String[] getAssertionValue(CtMember m){
        try{
            Assertion a = (Assertion) m.getAnnotation(Assertion.class);
            return new String[] { a.value(), a.entry() };
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
