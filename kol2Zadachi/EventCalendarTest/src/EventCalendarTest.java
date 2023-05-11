import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class EventCalendarTest {
    public static void main(String[] args) throws ParseException {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        int year = scanner.nextInt();
        scanner.nextLine();
        EventCalendar eventCalendar = new EventCalendar(year);
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(";");
            String name = parts[0];
            String location = parts[1];
            Date date = df.parse(parts[2]);
            try {
                eventCalendar.addEvent(name, location, date);
            } catch (WrongDateException e) {
                System.out.println(e.getMessage());
            }
        }
        Date date = df.parse(scanner.nextLine());
        eventCalendar.listEvents(date);
//        eventCalendar.listByMonth();
    }
}

// vashiot kod ovde

class WrongDateException extends Exception{
    public WrongDateException() {
        super("hellooo");
    }
}
class Event implements Comparable<Event>{
    String name;
    String location;
    Date time;

    public Event(String name, String location, Date time) {
        this.name = name;
        this.location = location;
        this.time = time;
    }

    @Override
    public int compareTo(Event o) {
        if(time.compareTo(o.time)==0)
            return name.compareTo(o.name);

        return time.compareTo(o.time);
    }

    @Override
    public String toString() {
        return String.format("%s at %s, %s", time,location,name);
    }
}
class EventCalendar{
    int year;
    Map<Date,TreeSet<Event>> events = new HashMap<>();

    public EventCalendar(int year) {
        this.year = year;
    }

    public void addEvent(String name, String location, Date date) throws WrongDateException {
        if(date.getYear()+1900!=year)
            throw new WrongDateException();

        Event event = new Event(name, location,date);
        TreeSet<Event> list = events.get(date);

        if(list==null)
            list = new TreeSet<>();

        list.add(event);
    }

    public void listEvents(Date date){
        events.values().forEach(System.out::println);
    }
}