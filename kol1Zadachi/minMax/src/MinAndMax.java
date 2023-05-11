import java.util.Scanner;

public class MinAndMax {
    public static void main(String[] args) throws ClassNotFoundException {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        MinMax<String> strings = new MinMax<String>();
        for(int i = 0; i < n; ++i) {
            String s = scanner.next();
            strings.update(s);
        }
        System.out.println(strings);
        MinMax<Integer> ints = new MinMax<Integer>();
        for(int i = 0; i < n; ++i) {
            int x = scanner.nextInt();
            ints.update(x);
        }
        System.out.println(ints);
    }
}

class MinMax<T extends Comparable<T>>{
    T min;
    T max;
    int maxCount;
    int minCount;
    int total=0;
    public MinMax() {
        total=maxCount=minCount=0;
    }

    void update(T element){
        if(total==0)
            min=max=element;

        total++;

        if(max.compareTo(element)==0)
            maxCount++;
        if(min.compareTo(element)==0)
            minCount++;

        if(min.compareTo(element)<0){
            minCount=1;
            min=element;
        }
        else if(max.compareTo(element)>0){
            maxCount=1;
            max=element;
        }
    }

    T max(){
        return max;
    }

    T min(){
        return min;
    }


    @Override
    public String toString() {
        return String.format("%s %s %d\n", max,min,total-minCount-maxCount);
    }

}