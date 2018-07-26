package special;

public class Formater {
    public Formater() {
    }

    public static double format(double val) {
        return Double.parseDouble(String.format("%.2f", val));
    }
}
