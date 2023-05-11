import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class WeatherStationTest {
    public static void main(String[] args) throws ParseException {
        Scanner scanner = new Scanner(System.in);
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        int n = scanner.nextInt();
        scanner.nextLine();
        WeatherStation ws = new WeatherStation(n);
        while (true) {
            String line = scanner.nextLine();
            if (line.equals("=====")) {
                break;
            }
            String[] parts = line.split(" ");
            float temp = Float.parseFloat(parts[0]);
            float wind = Float.parseFloat(parts[1]);
            float hum = Float.parseFloat(parts[2]);
            float vis = Float.parseFloat(parts[3]);
            line = scanner.nextLine();
            Date date = df.parse(line);
            ws.addInformation(temp, wind, hum, vis, date);
        }
        String line = scanner.nextLine();
        Date from = df.parse(line);
        line = scanner.nextLine();
        Date to = df.parse(line);
        scanner.close();
        System.out.println(ws.total());
        try {
            ws.status(from, to);
        } catch (RuntimeException e) {
            System.out.println(e);
        }
    }
}

// vashiot kod ovde


class WeatherStation{
    List<Information> information = new LinkedList<>();
    int days;

    public WeatherStation(int days) {
        this.days = days;
    }

    public void addInformation(float temperature, float wind, float humidity, float visibility, Date date) {

        boolean flag=true;
        for (int i=0; i<information.size(); i++)
            if(date.getTime()-information.get(i).getDate().getTime()<2.5*60*1000){
                flag=false;
                break;
            }
        if(flag)
            information.add(new Information(temperature, wind, humidity, visibility, date));
        information.removeIf(x->date.getTime()-x.getDate().getTime()> (long) days *24*60*60*1000);

    }

    public int total(){
        return information.size();
    }

    public void status(Date from, Date to){
        AtomicReference<Double> sum= new AtomicReference<>((double) 0);
        AtomicReference<Double> temperatura= new AtomicReference<>((double) 0);

        AtomicInteger count= new AtomicInteger();
        information.stream()
                .filter(x->x.getDate().compareTo(from)>=0 && x.getDate().compareTo(to)<=0)
                .sorted().forEach(s->{
                    System.out.println(s.toString());
                    count.getAndIncrement();
                    sum.updateAndGet(v -> (double) (v + s.getTemp()));
                    temperatura.updateAndGet(v -> (double) (v + s.getTemp()));
                });

        if(count.get()==0)
            throw new RuntimeException();

        System.out.printf("Average temperature: %.2f%n", temperatura.get()/count.get());
    }
}

class Information implements Comparable<Information>{
    double temp;
    double humidity;
    double wind;
    double see;
    Date date;

    public Information(double temp, double humidity, double wind, double see, Date date) {
        this.temp = temp;
        this.humidity = humidity;
        this.wind = wind;
        this.see = see;
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    public double getTemp() {
        return temp;
    }

    public double getHumidity() {
        return humidity;
    }

    public double getWind() {
        return wind;
    }

    public double getSee() {
        return see;
    }

    @Override
    public int compareTo(Information o) {
        return date.compareTo(o.getDate());
    }

    @Override
    public String toString() {
        return String.format("%.1f %.1f km/h %.1f%% %.1f km %s",temp, humidity,wind,see,date);
    }
}