import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class Shapes1Test {

    public static void main(String[] args) {
        ShapesApplication shapesApplication = new ShapesApplication();

        System.out.println("===READING SQUARES FROM INPUT STREAM===");
        System.out.println(shapesApplication.readCanvases(System.in));
        System.out.println("===PRINTING LARGEST CANVAS TO OUTPUT STREAM===");
        shapesApplication.printLargestCanvasTo(System.out);

    }
}

class ShapesApplication{
    List<Canvas> canvases;
    public ShapesApplication() {
    }

    int readCanvases(InputStream inputStream){
        BufferedReader input = new BufferedReader(new InputStreamReader(inputStream));

        canvases = input.lines().map(inputs-> new Canvas(inputs)).collect(toList());
        int counter = 0;

        for (int i=0; i<canvases.size(); i++)
            counter+=canvases.get(i).numberOfCanvases();

        return counter;
    }

    void printLargestCanvasTo (OutputStream outputStream){
        PrintWriter out = new PrintWriter(new PrintStream(outputStream));
        out.println(max().toString());
        out.close();

        out.println();
    }

    public Canvas max(){
        return canvases.stream().max(Comparator.naturalOrder()).get();
    }
}

class Canvas implements Comparable<Canvas>{
    String id;
    List<Integer> sizes;

    public Canvas(String everything) {
        String []parts = everything.split("\\s");

        id=parts[0];

        sizes = new ArrayList<Integer>();
        for (int i=1; i< parts.length; i++)
            sizes.add(Integer.parseInt(parts[i]));
    }

    public int numberOfCanvases(){
        return sizes.size();
    }

    public int sum(){
         int sum=0;
         for (int i=0; i<sizes.size(); i++)
             sum+= sizes.get(i);
         return sum*4;
    }

    @Override
    public int compareTo(Canvas o) {
        return Integer.compare(sum(),o.sum());
    }

    @Override
    public String toString() {
        return String.format("%s %d %d",id,sizes.size(),sum());
    }
}