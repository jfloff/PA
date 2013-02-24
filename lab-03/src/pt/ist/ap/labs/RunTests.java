package pt.ist.ap.labs;

public class RunTests {

    public static void main(String[] args){

        // command line parameter
        if(args.length != 1) {
            System.err.println("[ERROR] Invalid command line: one and one only filename expected");
            System.exit(1);
        }

        for (String s : args) {
            System.out.println(s);
        }

        // FileInputStream fstream = new FileInputStream(argv[0]);
        // Get the object of DataInputStream


    }
}
