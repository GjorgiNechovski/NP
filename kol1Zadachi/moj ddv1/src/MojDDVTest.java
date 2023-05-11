import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class MojDDVTest {

    public static void main(String[] args) throws IOException {

        MojDDV mojDDV = new MojDDV();

        System.out.println("===READING RECORDS FROM INPUT STREAM===");
        mojDDV.readRecords(System.in);

        System.out.println("===PRINTING TAX RETURNS RECORDS TO OUTPUT STREAM ===");
        mojDDV.printTaxReturns(System.out);

    }
}

class MojDDV{
    List<Fiskalna> fiskalni = new ArrayList<>();

    public MojDDV() {
    }

    void readRecords(InputStream in) throws IOException {
        BufferedReader bf = new BufferedReader(new InputStreamReader(in));

        fiskalni=bf.lines().map(fiskalna ->{
            try{
                return new Fiskalna(fiskalna);
            }
            catch (Exception e){
                System.out.println(e.getMessage());;
                return null;
            }
        })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        bf.close();

    }

    void printTaxReturns (OutputStream outputStream){
        PrintWriter pw = new PrintWriter(new OutputStreamWriter(outputStream));

        fiskalni.forEach(fiskalna -> pw.println(fiskalna.toString()));
        pw.flush();

    }
}

class AmountNotAllowedException extends Exception{
    public AmountNotAllowedException(String message) {
        super(message);
    }
}
class Fiskalna{
    String ID;
    List<Item> items = new ArrayList<>();
    public Fiskalna(String fiskalna) throws AmountNotAllowedException {
        String[] parts = fiskalna.split("\\s+");
        ID = parts[0];

        int sum = checkMoney(parts);
        if(sum<=30000) {
            for (int i = 1; i < parts.length; i+=2) {
                items.add(new Item(Integer.parseInt(parts[1]), parts[i + 1].charAt(0)));
            }
        }
        else{
            throw new AmountNotAllowedException(String.format("Receipt with amount %d is not allowed to be scanned" ,sum));
        }
    }

    private int checkMoney(String[] values){
        int sum=0;
        for (int i=1; i<values.length; i+=2)
            sum+=Integer.parseInt(values[i]);

        return sum;
    }

    private int getMoney(){
        return items.stream().mapToInt(Item::getPrice).sum();
    }

    private double tax(){
        return items.stream().mapToDouble(item -> item.getTax()*item.getPrice()).sum();
    }

    @Override
    public String toString() {
        return String.format("%s %d %.2f", ID, getMoney(), tax());
    }
}

class Item{
    int price;
    double tax;

    public Item(int price, char tax) {
        this.price = price;

        if(tax=='A')
            this.tax = 0.15* 0.18;
        else if(tax=='B')
            this.tax = 0.15*0.05;
        else
            this.tax = 0;
    }

    public double getTax() {
        return tax;
    }

    public int getPrice() {
        return price;
    }
}