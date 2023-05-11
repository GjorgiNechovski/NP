import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class Shapes2Test {

    public static void main(String[] args) {

        ShapesApplication shapesApplication = new ShapesApplication(10000);

        System.out.println("===READING CANVASES AND SHAPES FROM INPUT STREAM===");
        shapesApplication.readCanvases(System.in);

        System.out.println("===PRINTING SORTED CANVASES TO OUTPUT STREAM===");
        shapesApplication.printCanvases(System.out);


    }
}

class InvalidCanvasException extends Exception{
    public InvalidCanvasException(String id,double max) {
        super(String.format("Canvas %s has a shape with area larger than %.2f",id,max));
    }
}

class ShapesApplication{
    double maxArea;
    List<Canvas> canvas = new ArrayList<>();

    public ShapesApplication(double maxArea) {
        this.maxArea = maxArea;
    }

    void readCanvases(InputStream in){
        BufferedReader bf = new BufferedReader(new InputStreamReader(in));

        canvas = bf.lines().map(x-> {
            try {
                return new Canvas(x,maxArea);
            } catch (InvalidCanvasException e) {
                System.out.println(e.getMessage());
                return null;
            }
        })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    void printCanvases(OutputStream os){
        PrintWriter pw = new PrintWriter(new OutputStreamWriter(os));
        canvas.stream().sorted(Comparator.reverseOrder()).forEach(pw::println);
        pw.flush();
    }
}

class Canvas implements Comparable<Canvas>{
    String id;
    List<Shape> shapes = new ArrayList<>();

    public Canvas(String in, double maxArea) throws InvalidCanvasException {
        String[] parts = in.split("\\s+");

        id=parts[0];
        for (int i=1; i<parts.length; i+=2){

            if(Objects.equals(parts[i].charAt(0), 'S')){
               if(Double.parseDouble(parts[i+1])*Double.parseDouble(parts[i+1])>maxArea)
                   throw new InvalidCanvasException(id,maxArea);
               else
                   shapes.add(new Square(Integer.parseInt(parts[i+1])));
            }

            else{
                if(Integer.parseInt(parts[i+1])*Integer.parseInt(parts[i+1])*Math.PI>maxArea)
                    throw new InvalidCanvasException(id,maxArea);
                else
                    shapes.add(new Circle(Integer.parseInt(parts[i+1])));
            }
        }
    }

    public double getArea(){
        return shapes.stream().mapToDouble(Shape::getArea).sum();
    }

    @Override
    public int compareTo(Canvas o) {
        return Double.compare(getArea(),o.getArea());
    }

    private int getCircles(){
        return (int) shapes.stream().filter(x-> Objects.equals(x.getType(), "Circle")).count();
    }


    @Override
    public String toString() {
        DoubleSummaryStatistics s = shapes.stream().mapToDouble(Shape::getArea).summaryStatistics();
        return String.format("%s %d %d %d %.2f %.2f %.2f",id, s.getCount(), getCircles(), shapes.size()-getCircles(), s.getMin(), s.getMax(), s.getAverage());
    }
}

abstract class Shape implements Comparable<Shape>{
    int a;

    public Shape(int a) {
        this.a = a;
    }

    abstract public double getArea();
    abstract public String getType();

    @Override
    public int compareTo(Shape o) {
        return Double.compare(getArea(),o.getArea());
    }
}

class Square extends Shape{
    public Square(int a) {
        super(a);
    }

    @Override
    public double getArea() {
        return a*a;
    }

    @Override
    public String getType() {
        return "Square";
    }
}

class Circle extends Shape{
    public Circle(int a) {
        super(a);
    }

    @Override
    public double getArea() {
        return a*a*Math.PI;
    }

    @Override
    public String getType() {
        return "Circle";
    }
}