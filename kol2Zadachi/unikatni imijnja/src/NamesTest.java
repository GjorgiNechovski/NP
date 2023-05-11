import java.util.*;
import java.util.stream.Collectors;

public class NamesTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        Names names = new Names();
        for (int i = 0; i < n; ++i) {
            String name = scanner.nextLine();
            names.addName(name);
        }
        n = scanner.nextInt();
        System.out.printf("===== PRINT NAMES APPEARING AT LEAST %d TIMES =====\n", n);
        names.printN(n);
        System.out.println("===== FIND NAME =====");
        int len = scanner.nextInt();
        int index = scanner.nextInt();
        System.out.println(names.findName(len, index));
        scanner.close();

    }
}

// vashiot kod ovde

class Names{
    TreeMap<String, Name> names = new TreeMap<>();

    public void addName(String name){
        Name temp = names.get(name);

        if(temp==null)
            names.put(name, new Name(name));
        else
            temp.increaseNumberOfAppearance();
    }

    public void printN(int n){
        names.values().stream()
                .filter(x->x.getNumberOfAppearance()>=n)
                .forEach(System.out::println);
    }

    public String findName(int len, int x){
        List<String> newMap = names.keySet()
                .stream()
                .filter(i->i.length()<len)
                .collect(Collectors.toList());

        x=x%newMap.size();
        return newMap.get(x);
    }
}

class Name implements Comparable<Name>{
    String name;
    int countOfCharacters;
    int numberOfAppearance;

    public Name(String name) {
        this.name = name;
        this.countOfCharacters = calculateUniqueCharacters(name);
        numberOfAppearance = 1;
    }

    private int calculateUniqueCharacters(String name){
        return (int) name.toLowerCase()
                .chars()
                .distinct()
                .count();
    }

    public void increaseNumberOfAppearance() {
        this.numberOfAppearance++;
    }

    public int getNumberOfAppearance() {
        return numberOfAppearance;
    }

    @Override
    public String toString() {
        return String.format("%s (%d) %d", name, numberOfAppearance, countOfCharacters);
    }

    @Override
    public int compareTo(Name o) {
        return name.compareTo(o.name);
    }
}