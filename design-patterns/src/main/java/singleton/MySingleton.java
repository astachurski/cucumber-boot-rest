package singleton;

public class MySingleton {

    private static MySingleton instance;

    private MySingleton() {
        super();
    }

    public static MySingleton createMe() {
        if (instance == null)
            instance = new MySingleton();
        return instance;
    }
}
