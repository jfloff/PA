package pt.ist.pa.labs;
import java.util.Scanner;

public class Say {

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner (System.in);
        System.out.println("Introduza a classe da mensagem pretendida: ");
        String classname = scanner.nextLine();
        System.out.println("\nClasse pretendida: " + classname);

        // Reflection
        Class c = Class.forName("pt.ist.pa.labs." + classname);
        Message m = (Message) c.newInstance();
        System.out.print("Mensagem: ");
        m.say();
    }
}
