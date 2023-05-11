import java.util.*;

public class ComponentTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String name = scanner.nextLine();
        Window window = new Window(name);
        Component prev = null;
        while (true) {
            try {
                int what = scanner.nextInt();
                scanner.nextLine();
                if (what == 0) {
                    int position = scanner.nextInt();
                    window.addComponent(position, prev);
                } else if (what == 1) {
                    String color = scanner.nextLine();
                    int weight = scanner.nextInt();
                    Component component = new Component(color, weight);
                    prev = component;
                } else if (what == 2) {
                    String color = scanner.nextLine();
                    int weight = scanner.nextInt();
                    Component component = new Component(color, weight);
                    prev.addComponent(component);
                    prev = component;
                } else if (what == 3) {
                    String color = scanner.nextLine();
                    int weight = scanner.nextInt();
                    Component component = new Component(color, weight);
                    prev.addComponent(component);
                } else if(what == 4) {
                    break;
                }

            } catch (InvalidPositionException e) {
                System.out.println(e.getMessage());
            }
            scanner.nextLine();
        }

        System.out.println("=== ORIGINAL WINDOW ===");
        System.out.println(window);
        int weight = scanner.nextInt();
        scanner.nextLine();
        String color = scanner.nextLine();
        window.changeColor(weight, color);
        System.out.println(String.format("=== CHANGED COLOR (%d, %s) ===", weight, color));
        System.out.println(window);
        int pos1 = scanner.nextInt();
        int pos2 = scanner.nextInt();
        System.out.println(String.format("=== SWITCHED COMPONENTS %d <-> %d ===", pos1, pos2));
        window.swichComponents(pos1, pos2);
        System.out.println(window);
    }
}

// вашиот код овде

class InvalidPositionException extends Exception{
    public InvalidPositionException(int position) {
        super(String.format("Invalid position %d, alredy taken!", position));
    }
}

class Component implements Comparable<Component>{
    String color;
    int weight;
    Set<Component> components = new TreeSet<>();

    public Component(String color, int weight) {
        this.color = color;
        this.weight = weight;
    }

    void addComponent(Component component){
        components.add(component);
    }

    @Override
    public int compareTo(Component o) {
        if(weight == o.weight)
            return color.compareTo(o.color);

        return Integer.compare(weight,o.weight);
    }

    public String toString(String prefix) {
        StringBuilder str = new StringBuilder();
        str.append(prefix+weight+":"+color);
        components.forEach(x -> str.append("\n"+x.toString(prefix+"---")));
        return str.toString();
    }

    public void setColor(String color, int weight) {
        if(this.weight<weight)
            this.color = color;
        components.forEach(x->x.setColor(color,weight));
    }
}

class Window{
    String name;
    Map<Integer,Component> components = new TreeMap<>();

    public Window(String name) {
        this.name = name;
    }

    void addComponent(int position, Component component) throws InvalidPositionException {
        if(components.get(position)!=null)
            throw new InvalidPositionException(position);
        components.put(position,component);
    }
    void changeColor(int weight, String color){
        components.values()
                .forEach(x->x.setColor(color,weight));
    }

    void swichComponents(int pos1, int pos2){
        Component temp1 = components.get(pos1);
        Component temp2 = components.get(pos2);

        components.put(pos1,temp2);
        components.put(pos2,temp1);
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("WINDOW "+name);
        components.forEach((k,v) -> str.append("\n"+k+":"+v.toString("")));
        return str.toString()+"\n";
    }
}