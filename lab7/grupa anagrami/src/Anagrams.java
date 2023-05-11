import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

public class Anagrams {

    public static void main(String[] args) {
        findAll(System.in);
    }

    public static void findAll(InputStream inputStream) {
        BufferedReader bf = new BufferedReader(new InputStreamReader(inputStream));

        Map<String, TreeSet<String>> words = new TreeMap<>();

        bf.lines().forEach(x->{
            char[] wordRn = x.toCharArray();
            Arrays.sort(wordRn);

            boolean flag = true;
            for(String key: words.keySet()){
                char[] wordFromMap = key.toCharArray();
                Arrays.sort(wordFromMap);

                if(Arrays.equals(wordRn,wordFromMap)){
                    words.get(key).add(x);
                    flag = false;
                    break;
                }
            }
            if(flag){
                TreeSet<String> temp = new TreeSet<>();
                temp.add(x);
                words.put(x,temp);
            }
        });

        for(String key: words.keySet()){
            if(words.get(key).size()>=5){
                StringBuilder sb = new StringBuilder();

                words.get(key).forEach(x->sb.append(x).append(" "));
                sb.delete(sb.length()-1, sb.length());
                System.out.println(sb);
            }
        }
    }
}
