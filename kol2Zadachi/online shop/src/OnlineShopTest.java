import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

enum COMPARATOR_TYPE {
    NEWEST_FIRST,
    OLDEST_FIRST,
    LOWEST_PRICE_FIRST,
    HIGHEST_PRICE_FIRST,
    MOST_SOLD_FIRST,
    LEAST_SOLD_FIRST
}

class ProductNotFoundException extends Exception {
    ProductNotFoundException(String message) {
        super(message);
    }
}


class Product {
    String category;
    String id;
    String name;
    LocalDateTime createdAt;
    double price;
    int sold;
    public Product(String category, String id, String name, LocalDateTime createdAt, double price) {
        this.category = category;
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public double getPrice() {
        return price;
    }

    public int getSold() {
        return sold;
    }

    public void setSold(int sold) {
        this.sold = sold;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", createdAt=" + createdAt +
                ", price=" + price +
                ", quantitySold=" + sold +
                '}';
    }
}


class OnlineShop {
    HashMap<String, Product> products;

    OnlineShop() {
        products = new HashMap<>();
    }

    void addProduct(String category, String id, String name, LocalDateTime createdAt, double price){
        products.put(id, new Product(category,id,name,createdAt,price));
    }

    double buyProduct(String id, int quantity) throws ProductNotFoundException{
        Product temp = products.get(id);
        if(temp==null)
            throw new ProductNotFoundException(String.format("Product with id %s does not exist in the online shop!", id));

        temp.setSold(temp.sold+quantity);
        return temp.price*quantity;
    }

    List<List<Product>> listProducts(String category, COMPARATOR_TYPE comparatorType, int pageSize) {
        List<List<Product>> result = new ArrayList<>();

        List<Product> temp;
        if(category!=null) temp = products.values().stream()
                .filter(x-> x.category.equals(category))
                .collect(Collectors.toList());
        else{
            temp = new ArrayList<>(products.values());
        }

        switch (comparatorType){
            case NEWEST_FIRST:{
                temp = temp.stream()
                        .sorted(Comparator.comparing(Product::getCreatedAt).reversed())
                        .collect(Collectors.toList());
                break;
            }
            case OLDEST_FIRST:{
                temp = temp.stream()
                        .sorted(Comparator.comparing(Product::getCreatedAt))
                        .collect(Collectors.toList());
                break;
            }
            case LEAST_SOLD_FIRST:{
                temp = temp.stream()
                        .sorted(Comparator.comparing(Product::getSold))
                        .collect(Collectors.toList());
                break;
            }
            case MOST_SOLD_FIRST:{
                temp = temp.stream()
                        .sorted(Comparator.comparing(Product::getSold).reversed())
                        .collect(Collectors.toList());
                break;
            }
            case LOWEST_PRICE_FIRST:{
                temp = temp.stream()
                        .sorted(Comparator.comparing(Product::getPrice))
                        .collect(Collectors.toList());
                break;
            }
            case HIGHEST_PRICE_FIRST:
                temp = temp.stream()
                        .sorted(Comparator.comparing(Product::getPrice).reversed())
                        .collect(Collectors.toList());
                break;
        }

        for (int i=0; i<temp.size(); i+=pageSize){
            result.add(temp.stream().skip(i).limit(pageSize).collect(Collectors.toList()));
        }
        return result;
    }

}

public class OnlineShopTest {

    public static void main(String[] args) {
        OnlineShop onlineShop = new OnlineShop();
        double totalAmount = 0.0;
        Scanner sc = new Scanner(System.in);
        String line;
        while (sc.hasNextLine()) {
            line = sc.nextLine();
            String[] parts = line.split("\\s+");
            if (parts[0].equalsIgnoreCase("addproduct")) {
                String category = parts[1];
                String id = parts[2];
                String name = parts[3];
                LocalDateTime createdAt = LocalDateTime.parse(parts[4]);
                double price = Double.parseDouble(parts[5]);
                onlineShop.addProduct(category, id, name, createdAt, price);
            } else if (parts[0].equalsIgnoreCase("buyproduct")) {
                String id = parts[1];
                int quantity = Integer.parseInt(parts[2]);
                try {
                    totalAmount += onlineShop.buyProduct(id, quantity);
                } catch (ProductNotFoundException e) {
                    System.out.println(e.getMessage());
                }
            } else {
                String category = parts[1];
                if (category.equalsIgnoreCase("null"))
                    category=null;
                String comparatorString = parts[2];
                int pageSize = Integer.parseInt(parts[3]);
                COMPARATOR_TYPE comparatorType = COMPARATOR_TYPE.valueOf(comparatorString);
                printPages(onlineShop.listProducts(category, comparatorType, pageSize));
            }
        }
        System.out.println("Total revenue of the online shop is: " + totalAmount);

    }

    private static void printPages(List<List<Product>> listProducts) {
        for (int i = 0; i < listProducts.size(); i++) {
            System.out.println("PAGE " + (i + 1));
            listProducts.get(i).forEach(System.out::println);
        }
    }
}

