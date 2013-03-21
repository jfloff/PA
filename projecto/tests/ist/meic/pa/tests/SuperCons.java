package ist.meic.pa.tests;
import ist.meic.pa.Assertion;

class SuperCons {

    public SuperCons(){
        System.out.println("SUPEEEEER COOOONS!");
    }

    @Assertion("$1.length() > 0")
    public SuperCons(String ola){
        System.out.println(ola);
    }
}
