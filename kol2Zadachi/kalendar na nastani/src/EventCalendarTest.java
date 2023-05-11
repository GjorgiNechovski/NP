import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

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
        eventCalendar.listByMonth();
    }
}

// vashiot kod ovde

class WrongDateException extends Exception{
    public WrongDateException() {
        super();
    }
}

class EventCalendar{
    Set<Event> events = new TreeSet<>();
    int year;

    public EventCalendar(int year) {
        this.year = year;
    }

    public void addEvent(String name, String location, Date date) throws WrongDateException {
        if(getYear()!=this.year)
            throw new WrongDateException();
        events.add(new Event(name,location,date));
    }

    public void listEvents(Date date){
        events.forEach(System.out::println);
    }
    public void listByMonth(){

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