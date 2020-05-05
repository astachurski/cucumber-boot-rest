package builder;

public class Wheels {
    private String wheel1 = "wheel1";
    private String wheel2 = "wheel2";
    private String wheel3 = "wheel3";
    private String wheel4 = "wheel4";

    public String getWheel1() {
        return wheel1;
    }

    public void setWheel1(String wheel1) {
        this.wheel1 = wheel1;
    }

    public String getWheel2() {
        return wheel2;
    }

    public void setWheel2(String wheel2) {
        this.wheel2 = wheel2;
    }

    public String getWheel3() {
        return wheel3;
    }

    public void setWheel3(String wheel3) {
        this.wheel3 = wheel3;
    }

    public String getWheel4() {
        return wheel4;
    }

    public void setWheel4(String wheel4) {
        this.wheel4 = wheel4;
    }

    @Override
    public String toString() {
        return String.format("%s, %s, %s, %s", getWheel1(), getWheel2(), getWheel3(), getWheel4());
    }
}
