import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * I partial exam 2016
 */
public class DailyTemperatureTest {
    public static void main(String[] args) {
        DailyTemperatures dailyTemperatures = new DailyTemperatures();
        dailyTemperatures.readTemperatures(System.in);
        System.out.println("=== Daily temperatures in Celsius (C) ===");
        dailyTemperatures.writeDailyStats(System.out, 'C');
        System.out.println("=== Daily temperatures in Fahrenheit (F) ===");
        dailyTemperatures.writeDailyStats(System.out, 'F');
    }
}

// Vashiot kod ovde

class DailyTemperatures{
    List<TemperatureForDay> temperatures;

    public DailyTemperatures() {
        temperatures= new ArrayList<>();
    }

    void readTemperatures(InputStream inputStream){
        BufferedReader bf = new BufferedReader(new InputStreamReader(inputStream));

        temperatures = bf.lines().map(TemperatureForDay::new).collect(Collectors.toList());
    }

    void writeDailyStats(OutputStream outputStream, char scale){
        PrintWriter pw = new PrintWriter(new OutputStreamWriter(outputStream));

        temperatures.stream()
                .sorted()
                .forEach(x->pw.println(String.format("%3s: %s", x.getDay(), x.summary(scale))));
        pw.flush();
    }
}

class TemperatureForDay implements Comparable<TemperatureForDay>{
    int day;
    List<Temperature> temperatures = new ArrayList<>();

    public TemperatureForDay(String x) {

        String[]parts = x.split("\\s+");
        day = Integer.parseInt(parts[0]);

        temperatures = Arrays.stream(parts)
                .skip(1)
                .map(k->new Temperature(k.substring(0,k.length()-1),k.charAt(k.length()-1)))
                .collect(Collectors.toList());
    }

    public String summary(char scale){
        DoubleSummaryStatistics sumary = temperatures.stream().mapToDouble(x->x.getTemperature(scale)).summaryStatistics();

        return String.format("Count: %3d Min: %6.2f%c Max: %6.2f%c Avg: %6.2f%c",
                sumary.getCount(),
                sumary.getMin(), scale,
                sumary.getMax(), scale,
                sumary.getAverage(), scale);

    }

    public int getDay() {
        return day;
    }

    @Override
    public int compareTo(TemperatureForDay o) {
        return Integer.compare(day,o.day);
    }
}

class Temperature implements Comparable<Temperature>{
    int temperature;
    char type;

    public Temperature(String temperature, char type) {
        this.temperature = Integer.parseInt(temperature);
        this.type = type;
    }

    double toCelsius(){
        if(type == 'C')
            return (double) temperature;
        return (double) (temperature - 32) * 5 / 9;
    }

    double toFarencheit(){
        if(type=='F')
            return (double) temperature;
        return (double) temperature*9/5+32;
    }

    public double getTemperature(char scale) {
        if(scale=='F')
            return toFarencheit();
        return toCelsius();
    }

    @Override
    public int compareTo(Temperature o) {
        return Integer.compare(temperature, o.temperature);
    }
}