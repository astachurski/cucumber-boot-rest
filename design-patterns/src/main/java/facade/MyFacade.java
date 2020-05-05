package facade;

public class MyFacade {

    public int performComplexCalculationsAndReturn(int a, int b) {
        ComplexSystem complexSystem = new ComplexSystem();
        int result = complexSystem.multiply(b, complexSystem.divide(a, complexSystem.add(a, b)));
        return result;
    }

    public static void main(String[] args) {
        int result = (new MyFacade()).performComplexCalculationsAndReturn(3, 4);
        System.out.println(result);
    }
}
