import java.io.*;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class TermFrequencyTest {
    public static void main(String[] args) throws FileNotFoundException {
        String[] stop = new String[] { "во", "и", "се", "за", "ќе", "да", "од",
                "ги", "е", "со", "не", "тоа", "кои", "до", "го", "или", "дека",
                "што", "на", "а", "но", "кој", "ја" };
        TermFrequency tf = new TermFrequency(System.in,
                stop);
        System.out.println(tf.countTotal());
        System.out.println(tf.countDistinct());
        System.out.println(tf.mostOften(10));
    }
}
// vasiot kod ovde

class TermFrequency {
    Map<String, Integer> words;
    TermFrequency(InputStream inputStream, String[] stopWords){
        words = new TreeMap<>();

        Scanner in = new Scanner(inputStream);
        List<String> noWords = Arrays.asList(stopWords);

        while (in.hasNext()){
            String word = in.next();
            word = word.toLowerCase().replace(',', ' ').replace('.', ' ').trim();

            if(!word.isEmpty() && !noWords.contains(word)) {
                words.computeIfPresent(word, (k, v) -> ++v);
                words.putIfAbsent(word, 1);
            }
        }
    }

    int countTotal(){
        return words.values().stream().mapToInt(x->x).sum();
    }

    int countDistinct(){
        return words.values().size();
    }

    List<String> mostOften(int k){
        return words.keySet().stream()
                .sorted(Comparator.comparing(words::get)
                        .reversed()
                        .thenComparing(Object::toString))
                .limit(k)
                .collect(Collectors.toList());
    }
}