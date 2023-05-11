import java.util.Collections;
import java.util.Scanner;

public class TripleTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int a = scanner.nextInt();
        int b = scanner.nextInt();
        int c = scanner.nextInt();
        Triple<Integer> tInt = new Triple<Integer>(a, b, c);
        System.out.printf("%.2f\n", tInt.max());
        System.out.printf("%.2f\n", tInt.avarage());
        tInt.sort();
        System.out.println(tInt);
        float fa = scanner.nextFloat();
        float fb = scanner.nextFloat();
        float fc = scanner.nextFloat();
        Triple<Float> tFloat = new Triple<Float>(fa, fb, fc);
        System.out.printf("%.2f\n", tFloat.max());
        System.out.printf("%.2f\n", tFloat.avarage());
        tFloat.sort();
        System.out.println(tFloat);
        double da = scanner.nextDouble();
        double db = scanner.nextDouble();
        double dc = scanner.nextDouble();
        Triple<Double> tDouble = new Triple<Double>(da, db, dc);
        System.out.printf("%.2f\n", tDouble.max());
        System.out.printf("%.2f\n", tDouble.avarage());
        tDouble.sort();
        System.out.println(tDouble);
    }
}
// vasiot kod ovde
// class Triple

class Triple<T extends Number>{
    T number1;
    T number2;
    T number3;

    public Triple(T number1, T number2, T number3) {
        this.number1 = number1;
        this.number2 = number2;
        this.number3 = number3;
    }

    double max(){
        if(number1.doubleValue()>number2.doubleValue()){
            if(number1.doubleValue()>number3.doubleValue())
                return number1.doubleValue();
            if (number2.doubleValue()>number3.doubleValue())
                return number2.doubleValue();
        }
        if(number2.doubleValue()>number3.doubleValue())
            return number2.doubleValue();
        return number3.doubleValue();
    }

    double avarage(){
        return (number1.doubleValue()+number2.doubleValue()+number3.doubleValue())/3;
    }

    public void sort() {
        if (number1.doubleValue() > number2.doubleValue()) {
            T temp = number2;
            number2 = number1;
            number1 = temp;
        }
        if (number2.doubleValue() > number3.doubleValue()) {
            T temp = number3;
            number3 = number2;
            number2 = temp;
        }
        if (number1.doubleValue() > number2.doubleValue()) {
            T temp = number2;
            number2 = number1;
            number1 = temp;
        }
    }

    @Override
    public String toString() {
        return String.format("%.2f %.2f %.2f", number1.doubleValue(),number2.doubleValue(),number3.doubleValue());
    }
}