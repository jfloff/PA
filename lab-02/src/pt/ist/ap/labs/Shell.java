package pt.ist.ap.labs;
import java.util.*;

public class Shell {

    // Last object to be kept
    static Object last;
    static Map<String, Object> stored;

    public static void main(String[] args) throws Exception
    {
        last = null;
        stored = new HashMap<String, Object>();

        while(true){
            System.out.println("$ ");
            Scanner scanner = new Scanner (System.in);
            String command = scanner.next();

            if (command.equals("Class"))
                byClass(scanner.next());
            else if (command.equals("Set"))
                bySet(scanner.next());
            else if (command.equals("Get"))
                byGet(scanner.next());
            else if (command.equals("Index"))
                byIndex(scanner.next());
            else if (command.equals("\\q"))
                return;
            else
                byMethod(command);
        }
    }

    public static void byClass(String classname)
    {
        try{
            Class c = Class.forName(classname);
            last = c.newInstance();
        } catch (ClassNotFoundException e){
            System.out.println("[ERROR] Class '" + classname + "' not found.");
        } catch (InstantiationException e){
            System.out.println("[ERROR] Error instanciating '" + classname + "'.");
        } catch (IllegalAccessException e){
            System.out.println("[ERROR] Error instanciating '" + classname + "'.");
        }

        System.out.println(last.getClass());
    }

    public static void bySet(String toset)
    {
        if(last == null){
            System.out.println("[ERROR] No object available to store.");
            return;
        }

        stored.put(toset, last);
        System.out.println("Saved name for object of type: " + last.getClass());
        System.out.println(last.getClass());
    }

    public static void byGet(String toget)
    {
        Object tmp = stored.get(toget);
        if (tmp == null){
            System.out.println("No object stored with key: " + toget);
        } else {
            last = temp;
        }

        System.out.println(last.getClass());
    }

    public static void byIndex(String index)
    {
        System.out.println("'" + index + "'");
    }

    public static void byMethod(String methodname)
    {
        System.out.println("'" + methodname + "'");
    }
}
