package builder;

public class CarBuilder {

    private String color;
    private String brand;
    private Wheels wheels;

    public String getBrand() {
        return brand;
    }

    public String getColor() {
        return color;
    }

    public Wheels getWheels() {
        return wheels;
    }

    public CarBuilder() {
    }

    public CarBuilder withBrand(String brand) {
        this.brand = brand;
        return this;
    }

    public CarBuilder withWheels(Wheels wheels) {
        this.wheels = wheels;
        return this;
    }

    public CarBuilder withColor(String color) {
        this.color = color;
        return this;
    }

    public Car build() {
        return new Car(this);
    }
}
