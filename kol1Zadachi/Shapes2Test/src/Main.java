import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CancellationException;
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

class ShapesApplication{
    List<Canvas> canvases;
    double maxArea;

    public ShapesApplication(double maxArea) {
        this.maxArea = maxArea;
    }

    void readCanvases (InputStream inputStream){
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        reader.lines().map(l ->new Canvas(l,maxArea)).collect(Collectors.toList());
    }
}

enum Type{
    Circle,
    Square;
}

abstract class Shape{
    int size;

    public Shape(int size) {
        this.size=size;
    }
    abstract double area();
    abstract Type getType();
}

class Circle extends Shape{
    public Circle(int size) {
        super(size);
    }

    public double area(){
        return size *size *Math.PI;
    }

    @Override
    Type getType() {
        return Type.Circle;
    }
}

class Square extends Shape{
    public Square(int size) {
        super(size);
    }

    @Override
    double area() {
        return size*size;
    }

    @Override
    Type getType() {
        return Type.Square;
    }
}

class IrregularCanvasException extends Exception{
    IrregularCanvasException(String id, double maxArea) {
        super(String.format("Canvas %d has a shape with area larger than %.2f", id, maxArea));
    }
}

class Canvas{
    List<Shape> shapes = new ArrayList<Shape>();
    String id;

    public Canvas(String information, double area) throws IrregularCanvasException {
            String[] parts = information.split("\\s+");

            id = parts[0];

            for (int i=1; i<parts.length; i=i+2){
                try{
                    if(Integer.parseInt(parts[1])>area)
                        throw new IrregularCanvasException(parts[0], area);
                }
                catch (IrregularCanvasException e){
                    System.out.println(e.getMessage());
                    break;
                }

                if()
            }
    }
}