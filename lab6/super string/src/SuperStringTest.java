import java.sql.Array;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class SuperStringTest {

    public static void main(String[] args) {
        Scanner jin = new Scanner(System.in);
        int k = jin.nextInt();
        if (  k == 0 ) {
            SuperString s = new SuperString();
            while ( true ) {
                int command = jin.nextInt();
                if ( command == 0 ) {//append(String s)
                    s.append(jin.next());
                }
                if ( command == 1 ) {//insert(String s)
                    s.insert(jin.next());
                }
                if ( command == 2 ) {//contains(String s)
                    System.out.println(s.contains(jin.next()));
                }
                if ( command == 3 ) {//reverse()
                    s.reverse();
                }
                if ( command == 4 ) {//toString()
                    System.out.println(s);
                }
                if ( command == 5 ) {//removeLast(int k)
                    s.removeLast(jin.nextInt());
                }
                if ( command == 6 ) {//end
                    break;
                }
            }
        }
    }

}

class SuperString{
    List<String> strings;
    List<Integer> lastIndexes;

    public SuperString() {
        strings = new LinkedList<>();
        lastIndexes = new LinkedList<>();
    }

    void append(String s){
        strings.add(s);
        lastIndexes.add(1);
    }

    void insert(String s){
        strings.add(0,s);
        lastIndexes.add(-1);
    }

    boolean contains(String s){
        return toString().contains(s);
    }

    void reverse(){
        strings = strings.stream().map(x->{
            StringBuilder bl = new StringBuilder();
            bl.append(x);
            return bl.reverse().toString();
        }).collect(Collectors.toList());

        Collections.reverse(strings);

        lastIndexes = lastIndexes.stream().map(x->x*-1).collect(Collectors.toList());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (String s:strings){
            sb.append(s);
        }
        return sb.toString();
    }

    void removeLast(int k){
        for (int i=0; i<k; i++){
            if(lastIndexes.get(lastIndexes.size()-1)==-1){
                strings.remove(strings.get(0));
                lastIndexes.remove(lastIndexes.size()-1);
            }
            else{
                strings.remove(strings.size()-1);
                lastIndexes.remove(lastIndexes.size()-1);
            }
        }
    }

}