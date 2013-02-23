package pt.ist.ap.labs;
import java.util.*;
import java.lang.reflect.*;

@SuppressWarnings("unchecked")
public class Shell {

    // Last object to be kept
    static Object last;
    static Map<String, Object> stored;

    public static void main(String[] args) throws Exception
    {
        last = null;
        stored = new HashMap<String, Object>();

        while(true){
            System.out.println("Command:> ");
            Scanner scanner = new Scanner (System.in);
            String command = scanner.next();

            if (command.equals("Class"))
                byClass(scanner.next());
            else if (command.equals("Set"))
                bySet(scanner.next());
            else if (command.equals("Get"))
                byGet(scanner.next());
            else if (command.equals("Index"))
                byIndex(scanner.nextInt());
            else
                byMethod(command + scanner.nextLine());
        }
    }

    public static void byClass(String classname)
    {
        try{
            Class c = Class.forName(classname);
            last = c.asSubclass(c);
            System.out.println(last);
        } catch (ClassNotFoundException e){
            System.out.println("[ERROR] Class not found: " + classname);
        }
    }

    public static void bySet(String toset)
    {
        if(last == null){
            System.out.println("[ERROR] No object available to store.");
            return;
        }

        stored.put(toset, last);
        System.out.println("Saved name for object of type: " + last.getClass());
        System.out.println(last);
    }

    public static void byGet(String toget)
    {
        Object tmp = stored.get(toget);
        if (tmp == null){
            System.out.println("No object stored with key: " + toget);
        } else {
            last = tmp;
        }

        System.out.println(last);
    }

    public static void byIndex(int index)
    {
        if(last.getClass().isArray()){
            Object[] tmp = (Object[]) last;
            last = tmp[index];
        }
        // if not an array returns itself
        System.out.println(last);
    }

    public static void byMethod(String methodcall)
    {
        if(last == null){
            System.out.println("[ERROR] No object available to call method on");
            return;
        }
        System.out.println("Trying generic command: " + methodcall);

        // First space if the method name, others are method arguments
        String[] temp = methodcall.split(" ");
        String name = temp[0];
        Object[] args = (Object[]) Arrays.copyOfRange(temp, 1, temp.length);

        Class c = last.getClass();

        try{
            Method m = c.getMethod(name);
            Object result = m.invoke(last, args);

            if(result.getClass().isArray()){
                for (Object o : (Object[]) result) {
                    System.out.println(o);
                }
            } else {
                System.out.println(result);
            }
            last = result;
        } catch (NoSuchMethodException e) {
            System.out.println("[ERROR] No such method available " + name + " for class " + last);
        } catch (IllegalAccessException e){
            System.out.println("[ERROR] Error invoking method: " + name);
        } catch (InvocationTargetException e){
            System.out.println("[ERROR] Error invoking method: " + name);
        } catch (IllegalArgumentException e){
            System.out.println("[ERROR] Wrong number of arguments for method: " + name);
        }
    }
}
