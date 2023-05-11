import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

enum Color {
    RED, GREEN, BLUE
}
public class ShapesTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Canvas canvas = new Canvas();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split(" ");
            int type = Integer.parseInt(parts[0]);
            String id = parts[1];
            if (type == 1) {
                Color color = Color.valueOf(parts[2]);
                float radius = Float.parseFloat(parts[3]);
                canvas.add(id, color, radius);
            } else if (type == 2) {
                Color color = Color.valueOf(parts[2]);
                float width = Float.parseFloat(parts[3]);
                float height = Float.parseFloat(parts[4]);
                canvas.add(id, color, width, height);
            } else if (type == 3) {
                float scaleFactor = Float.parseFloat(parts[2]);
                System.out.println("ORIGNAL:");
                System.out.print(canvas);
                canvas.scale(id, scaleFactor);
                System.out.printf("AFTER SCALING: %s %.2f\n", id, scaleFactor);
                System.out.print(canvas);
            }

        }
    }
}

interface Stackable {
    public float weight();
}

interface Scalable  {
    public void scale(float scaleFactor);
}


class Canvas {
    String id;
    Color color;

    List<Shape> shapes;
    public Canvas() {
        shapes= new ArrayList<>();
    }

    void add(String id, Color color, float radius){
        Circle temp = new Circle(id,color,radius);

        int tempi = findIndex(temp);

        shapes.add(tempi,temp);
    }

    void add(String id, Color color, float width, float height){
        Rectangle temp = new Rectangle(id,color,width,height);

        int tempi = findIndex(temp);

        shapes.add(tempi,temp);
    }

    private int findIndex(Shape temp){
        int tempi=shapes.size();
        for (int i=0; i<shapes.size(); i++){
            if(shapes.get(i).weight()<temp.weight()){
                tempi=i;
                break;
            }
        }
        return tempi;
    }

    void scale(String id, float scaleFactor){
        int tempi=1;

        for (int i=0; i<shapes.size(); i++){
            if(Objects.equals(shapes.get(i).id, id)){
                tempi=i;
                break;
            }
        }

        Shape temp=shapes.get(tempi);
        shapes.remove(shapes.get(tempi));
        temp.scale(scaleFactor);
        int tempj = findIndex(temp);
        shapes.add(tempj,temp);
    }

    @Override
    public String toString() {
        StringBuilder bl = new StringBuilder();
        for (Shape s:shapes)
            bl.append(s.toString());
        return bl.toString();
    }
}

abstract class Shape implements Scalable,Stackable{
    String id;
    Color color;

    public Shape(String id, Color color) {
        this.id = id;
        this.color = color;
    }

    abstract public float weight();

    abstract public void scale(float scaleFactor);
}

class Circle extends Shape{
    float radius;

    public Circle(String id, Color color, float radius) {
        super(id, color);
        this.radius = radius;
    }

    @Override
    public float weight() {
        return (float) (radius*radius*Math.PI);
    }

    @Override
    public void scale(float scaleFactor) {
        radius*=scaleFactor;
    }

    @Override
    public String toString() {
        return String.format("C: %-5s%-10s%10.2f\n", id,color,weight());
    }
}

class Rectangle extends Shape{
    float width;
    float height;

    public Rectangle(String id, Color color, float width, float height) {
        super(id, color);
        this.width = width;
        this.height = height;
    }

    @Override
    public float weight() {
        return width*height;
    }

    @Override
    public void scale(float scaleFactor) {
        width*=scaleFactor;
        height*=scaleFactor;
    }

    @Override
    public String toString() {
        return String.format("R: %-5s%-10s%10.2f\n", id,color,weight());
    }
}


