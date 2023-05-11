import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;

public class PizzaOrderTest {

    public static void main(String[] args) {
        Scanner jin = new Scanner(System.in);
        int k = jin.nextInt();
        if (k == 0) { //test Item
            try {
                String type = jin.next();
                String name = jin.next();
                Item item = null;
                if (type.equals("Pizza")) item = new PizzaItem(name);
                else item = new ExtraItem(name);
                System.out.println(item.getPrice());
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
        }
        if (k == 1) { // test simple order
            Order order = new Order();
            while (true) {
                try {
                    String type = jin.next();
                    String name = jin.next();
                    Item item = null;
                    if (type.equals("Pizza")) item = new PizzaItem(name);
                    else item = new ExtraItem(name);
                    if (!jin.hasNextInt()) break;
                    order.addItem(item, jin.nextInt());
                } catch (Exception e) {
                    System.out.println(e.getClass().getSimpleName());
                }
            }
            jin.next();
            System.out.println(order.getPrice());
            order.displayOrder();
            while (true) {
                try {
                    String type = jin.next();
                    String name = jin.next();
                    Item item = null;
                    if (type.equals("Pizza")) item = new PizzaItem(name);
                    else item = new ExtraItem(name);
                    if (!jin.hasNextInt()) break;
                    order.addItem(item, jin.nextInt());
                } catch (Exception e) {
                    System.out.println(e.getClass().getSimpleName());
                }
            }
            System.out.println(order.getPrice());
            order.displayOrder();
        }
        if (k == 2) { // test order with removing
            Order order = new Order();
            while (true) {
                try {
                    String type = jin.next();
                    String name = jin.next();
                    Item item = null;
                    if (type.equals("Pizza")) item = new PizzaItem(name);
                    else item = new ExtraItem(name);
                    if (!jin.hasNextInt()) break;
                    order.addItem(item, jin.nextInt());
                } catch (Exception e) {
                    System.out.println(e.getClass().getSimpleName());
                }
            }
            jin.next();
            System.out.println(order.getPrice());
            order.displayOrder();
            while (jin.hasNextInt()) {
                try {
                    int idx = jin.nextInt();
                    order.removeItem(idx);
                } catch (Exception e) {
                    System.out.println(e.getClass().getSimpleName());
                }
            }
            System.out.println(order.getPrice());
            order.displayOrder();
        }
        if (k == 3) { //test locking & exceptions
            Order order = new Order();
            try {
                order.lock();
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
            try {
                order.addItem(new ExtraItem("Coke"), 1);
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
            try {
                order.lock();
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
            try {
                order.removeItem(0);
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
        }
    }

}

interface Item{
    int getPrice();
    String getType();
}

class InvalidExtraTypeException extends Exception{
    public InvalidExtraTypeException() {
        super("Invalid extra type");
    }
}

class InvalidPizzaTypeException extends Exception{

    public InvalidPizzaTypeException() {
        super("Invalid pizza type");
    }
}

class OrderLockedException extends Exception{
    public OrderLockedException() {
        super("OrderLockedException");
    }
}

class ItemOutOfStockException extends Exception{
    public ItemOutOfStockException(Item item){
        super();
    }
}

class ArrayIndexOutOfBоundsException extends Exception{
    public ArrayIndexOutOfBоundsException(int idx){
        super();
    }
}

class EmptyOrder extends Exception{
    public EmptyOrder() {
        super("EmptyOrder");
    }
}

class ExtraItem implements Item{
    String type;
    int price;

    public ExtraItem(String type) throws InvalidExtraTypeException {
        if(!Objects.equals(type, "Coke") && !Objects.equals(type, "Ketchup"))
            throw new InvalidExtraTypeException();

        this.type = type;
        if(type.equals("Coke"))
            price=5;
        else
            price=3;
    }

    @Override
    public int getPrice() {
        return price;
    }

    @Override
    public String getType() {
        return type;
    }
}

class PizzaItem implements Item{
    String type;
    int price;
    public PizzaItem(String type) throws InvalidPizzaTypeException {
        if(!Objects.equals(type, "Standard") && !Objects.equals(type, "Pepperoni") && !Objects.equals(type, "Vegetarian"))
            throw new InvalidPizzaTypeException();

        if(type.equals("Standard"))
            price=10;
        else if(type.equals("Pepperoni"))
            price=12;
        else
            price=8;
        this.type = type;
    }

    @Override
    public int getPrice() {
        return price;
    }

    @Override
    public String getType() {
        return type;
    }
}

class Order{
    Item []items;
    int [] counts;
    boolean lock;

    public Order() {
        lock=false;
        items = new Item[0];
        counts = new int[0];
    }

    void addItem(Item item, int count) throws OrderLockedException, ItemOutOfStockException {
        if(lock){
            throw new OrderLockedException();
        }
        if(count>10){
            throw new ItemOutOfStockException(item);
        }

        boolean flag = false;

        for (int i=0; i<items.length; i++){
            if(items[i].getType().equals(item.getType())){
                items[i] = item;
                counts[i] = count;
                flag=true;
                break;
            }
        }
        if(!flag){
            Item []tempI = new Item[items.length+1];
            int []tempC = new int[items.length+1];
            for (int i=0; i<items.length; i++) {
                tempI[i] = items[i];
                tempC[i] = counts[i];
            }
            tempI[items.length] = item;
            tempC[items.length] = count;

            items = tempI;
            counts=tempC;
        }
    }
    int getPrice(){
        int sum=0;
        for (int i=0; i< items.length; i++)
            sum += items[i].getPrice()*counts[i];
        return sum;
    }

    void displayOrder(){
        for (int i=0; i<items.length; i++){
            int sum=items[i].getPrice() * counts[i];
            System.out.format("%3d.%-14s x%2d%5d$\n", i + 1, items[i].getType(), counts[i], counts[i] * items[i].getPrice());
        }
        System.out.format("%-22s%5d$\n","Total:", getPrice());
    }

    void removeItem(int idx) throws ArrayIndexOutOfBоundsException, OrderLockedException {
        boolean flag=false;

        if(lock){
            throw new OrderLockedException();
        }

        if(idx<0 || idx>items.length-1){
            throw new ArrayIndexOutOfBоundsException(idx);
        }

        for (int i=idx; i< items.length-1; i++){
            items[i] = items[i+1];
        }

        items = Arrays.copyOf(items, items.length - 1);
    }

    void lock() throws EmptyOrder {
        if(items.length>0)
            lock=true;
        else
            throw new EmptyOrder();
    }
}