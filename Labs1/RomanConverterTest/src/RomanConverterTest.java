import java.util.Scanner;
import java.util.stream.IntStream;

public class RomanConverterTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        IntStream.range(0, n)
                .forEach(x -> System.out.println(RomanConverter.toRoman(scanner.nextInt())));
        scanner.close();
    }
}


class RomanConverter {
    /**
     * Roman to decimal converter
     *
     * @param n number in decimal format
     * @return string representation of the number in Roman numeral
     */
    public static String toRoman(int n) {
        // your solution here

        String[][] a = {{"I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX"},
                {"X", "XX","XXX", "XL","L", "LX", "LXX", "LXXX", "XC"},
                {"C", "CC", "CCC", "CD", "D", "DC", "DCC", "DCCC", "CM"}};

        int i;

        String []skladi = new String[100];
        for (i=0; i<3; i++)
        {
            int k=n%10;
            n/=10;
            if(k==0)
                continue;
            skladi[i] = a[i][k-1];
        }

        while(n!=0)
        {
            skladi[i++] = "M";
            n--;
        }

        for (--i; i>=0; i--)
        {
            if(skladi[i]==null)
                continue;
            System.out.print(skladi[i]);
        }
        return "";
    }

}
