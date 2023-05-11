import java.util.*;

public class AuditionTest {
    public static void main(String[] args) {
        Audition audition = new Audition();
        List<String> cities = new ArrayList<String>();
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split(";");
            if (parts.length > 1) {
                audition.addParticpant(parts[0], parts[1], parts[2],
                        Integer.parseInt(parts[3]));
            } else {
                cities.add(line);
            }
        }
        for (String city : cities) {
            System.out.printf("+++++ %s +++++\n", city);
            audition.listByCity(city);
        }
        scanner.close();
    }

}

class Audition{
    HashMap<String, HashSet<Candidate>> candidates = new HashMap<>();

    void addParticpant(String city, String code, String name, int age){
        HashSet<Candidate> cities = candidates.get(city);
        Candidate candidate = new Candidate(city,code,name,age);

        if(cities==null){
            cities = new HashSet<>();
            candidates.put(city, cities);
        }
        cities.add(candidate);
    }
    void listByCity(String city){
        candidates.get(city)
                .stream()
                .sorted(Comparator.comparing(Candidate::getName)
                        .thenComparing(Candidate::getAge))
                .forEach(System.out::println);
    }
}

class Candidate{
    String city;
    String cde;
    String name;
    int age;

    public Candidate(String city, String cde, String name, int age) {
        this.city = city;
        this.cde = cde;
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    @Override
    public String toString() {
        return String.format("%s %s %d", cde,name,age);
    }

    public boolean equals(Object obj) {
        Candidate p = (Candidate) obj;
        return cde.equals(p.cde);
    }

    @Override
    public int hashCode() {
        return cde.hashCode();
    }
}