package builder;

public class Car {

    private Wheels wheels;
    private String color;
    private String brand;

    public Car(CarBuilder builder) {
        this.wheels = builder.getWheels();
        this.brand = builder.getBrand();
        this.color = builder.getColor();
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getBrand() {
        return brand;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getColor() {
        return color;
    }

    public void setWheels(Wheels wheels) {
        this.wheels = wheels;
    }

    public Wheels getWheels() {
        return wheels;
    }

    @Override
    public String toString() {
        return brand + " " + color + " " + wheels.toString();
    }

    public static void main(String[] args) {

        Car myCar = new CarBuilder()
                .withBrand("mercedes")
                .withColor("red")
                .withWheels(new Wheels())
                .build();

        System.out.println(myCar.toString());

    }
}
