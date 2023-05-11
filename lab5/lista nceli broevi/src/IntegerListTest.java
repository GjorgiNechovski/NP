import java.util.*;
import java.util.stream.Collectors;

public class IntegerListTest {

    public static void main(String[] args) {
        Scanner jin = new Scanner(System.in);
        int k = jin.nextInt();
        if ( k == 0 ) { //test standard methods
            int subtest = jin.nextInt();
            if ( subtest == 0 ) {
                IntegerList list = new IntegerList();
                while ( true ) {
                    int num = jin.nextInt();
                    if ( num == 0 ) {
                        list.add(jin.nextInt(), jin.nextInt());
                    }
                    if ( num == 1 ) {
                        list.remove(jin.nextInt());
                    }
                    if ( num == 2 ) {
                        print(list);
                    }
                    if ( num == 3 ) {
                        break;
                    }
                }
            }
            if ( subtest == 1 ) {
                int n = jin.nextInt();
                Integer a[] = new Integer[n];
                for ( int i = 0 ; i < n ; ++i ) {
                    a[i] = jin.nextInt();
                }
                IntegerList list = new IntegerList(a);
                print(list);
            }
        }
        if ( k == 1 ) { //test count,remove duplicates, addValue
            int n = jin.nextInt();
            Integer a[] = new Integer[n];
            for ( int i = 0 ; i < n ; ++i ) {
                a[i] = jin.nextInt();
            }
            IntegerList list = new IntegerList(a);
            while ( true ) {
                int num = jin.nextInt();
                if ( num == 0 ) { //count
                    System.out.println(list.count(jin.nextInt()));
                }
                if ( num == 1 ) {
                    list.removeDuplicates();
                }
                if ( num == 2 ) {
                    print(list.addValue(jin.nextInt()));
                }
                if ( num == 3 ) {
                    list.add(jin.nextInt(), jin.nextInt());
                }
                if ( num == 4 ) {
                    print(list);
                }
                if ( num == 5 ) {
                    break;
                }
            }
        }
        if ( k == 2 ) { //test shiftRight, shiftLeft, sumFirst , sumLast
            int n = jin.nextInt();
            Integer a[] = new Integer[n];
            for ( int i = 0 ; i < n ; ++i ) {
                a[i] = jin.nextInt();
            }
            IntegerList list = new IntegerList(a);
            while ( true ) {
                int num = jin.nextInt();
                if ( num == 0 ) { //count
                    list.shiftLeft(jin.nextInt(), jin.nextInt());
                }
                if ( num == 1 ) {
                    list.shiftRight(jin.nextInt(), jin.nextInt());
                }
                if ( num == 2 ) {
                    System.out.println(list.sumFirst(jin.nextInt()));
                }
                if ( num == 3 ) {
                    System.out.println(list.sumLast(jin.nextInt()));
                }
                if ( num == 4 ) {
                    print(list);
                }
                if ( num == 5 ) {
                    break;
                }
            }
        }
    }

    public static void print(IntegerList il) {
        if ( il.size() == 0 ) System.out.print("EMPTY");
        for ( int i = 0 ; i < il.size() ; ++i ) {
            if ( i > 0 ) System.out.print(" ");
            System.out.print(il.get(i));
        }
        System.out.println();
    }

}

class IntegerList{
    ArrayList<Integer> lista;
    public IntegerList() {
        lista=new ArrayList<Integer>();
    }

    public IntegerList(Integer... numbers) {
        lista=new ArrayList<Integer>();
        lista.addAll(Arrays.asList(numbers));
    }

    public void add(int el, int idx){
        if(idx<=lista.size())
            lista.add(idx,el);
        else{
            while (lista.size()<idx)
                lista.add(0);
            lista.add(idx,el);
        }
    }

    public void remove(int idx){
        lista.remove(idx);
    }

    public void set(int e1, int idx){
        lista.add(e1,idx);
        lista.remove(idx+1);
    }

    public int get(int idx){
        return lista.get(idx);
    }

    public int size(){
        return lista.size();
    }

    public int count(int el){
        int count=0;
        for (int i=0; i<lista.size(); i++){
            if(lista.get(i)==el)
                count++;
        }
        return count;
    }

    public void removeDuplicates(){
        Collections.reverse(lista);
        lista = (ArrayList<Integer>) lista.stream().distinct().collect(Collectors.toList());
        Collections.reverse(lista);
    }

    public int sumFirst(int k){
        return lista.stream().mapToInt(Integer::intValue).limit(k).sum();
    }

    public int sumLast(int k){
        Collections.reverse(lista);
        int sum=lista.stream().mapToInt(Integer::intValue).limit(k).sum();
        Collections.reverse(lista);
        return sum;
    }

    public void shiftRight(int idx, int k){
        int newIndex = (idx + k) % size();
        if (newIndex != idx) {
            int temp = get(idx);
            remove(idx);
            add(temp, newIndex);
        }
    }

    public void shiftLeft(int idx,int k){
        int newIndex;
        if (idx - k < 0)
            newIndex = size() - (Math.abs(idx - k) % size());
        else
            newIndex = idx - k;
        if (newIndex != idx) {
            int temp = get(idx);
            remove(idx);
            add(temp, newIndex);
        }

    }

    public IntegerList addValue(int value){
        IntegerList temp = new IntegerList();
        temp.lista = (ArrayList<Integer>) lista.stream().map(integer -> (integer+value)).collect(Collectors.toList());
        return temp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IntegerList that = (IntegerList) o;
        return Objects.equals(lista, that.lista);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lista);
    }
}