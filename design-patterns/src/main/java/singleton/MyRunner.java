package singleton;

public class MyRunner {

    public static void main(String[] args) {
        //MySingleton mySingleton = new MySingleton();
        MySingleton mySingleton = MySingleton.createMe();
        //MySingleton mySingleton2 = MySingleton.createMe(); <--- uncomment me!
        System.out.println(mySingleton);
    }
}
