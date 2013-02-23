package pt.ist.ap.labs;
import java.util.Scanner;

public class Shell {

    public static void main(String[] args) throws Exception
    {
        while(true){
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
        System.out.println("'" + classname + "'");
    }

    public static void bySet(String toset)
    {
        System.out.println("'" + toset + "'");
    }

    public static void byGet(String toget)
    {
        System.out.println("'" + toget + "'");
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
