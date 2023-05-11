import java.io.*;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class F1Test {

    public static void main(String[] args) throws IOException {
        F1Race f1Race = new F1Race();
        f1Race.readResults(System.in);
        f1Race.printSorted(System.out);
    }

}

class F1Race {
    // vashiot kod ovde
    List<Pilot> pilots;
    public F1Race() {
    }

    void readResults(InputStream inputStream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));

        pilots = in.lines()
                .map(pilot->new Pilot(pilot))
                .collect(Collectors.toList());
        in.close();
    }

    void printSorted(OutputStream outputStream){
        PrintWriter writer = new PrintWriter(new PrintStream(outputStream));

        Collections.sort(pilots);

        for (int i=0; i<pilots.size(); i++)
            writer.println(i+1+". " + pilots.get(i));
        writer.close();
    }

    void sortTheList(){
        pilots.sort(Comparator.naturalOrder());
    }
}

class Pilot implements Comparable<Pilot> {
    String name;
    String bestTime;

    public Pilot(String inputLine) {
        String[] contents = inputLine.split(" ");
        name = contents[0];
        int time = Integer.MAX_VALUE;
        int bestIndex = -1;
        for(int i=1;i<contents.length;i++){
            int localRaw = turnTimeToInt(contents[i]);
            if(localRaw<time){
                time = localRaw;
                bestIndex = i;
            }
        }
        bestTime = contents[bestIndex];
    }

    private int turnTimeToInt(String time){
        String[] times = time.split(":");
        return Integer.parseInt(times[0])*60000+Integer.parseInt(times[1])*1000+Integer.parseInt(times[2]);
    }

    @Override
    public String toString() {
        return String.format("%-10s%10s", name, bestTime);
    }

    @Override
    public int compareTo(Pilot o) {
        return Integer.compare(turnTimeToInt(bestTime),o.turnTimeToInt(o.bestTime));
    }


}