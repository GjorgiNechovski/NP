import java.util.*;

public class StaduimTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        String[] sectorNames = new String[n];
        int[] sectorSizes = new int[n];
        String name = scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(";");
            sectorNames[i] = parts[0];
            sectorSizes[i] = Integer.parseInt(parts[1]);
        }
        Stadium stadium = new Stadium(name);
        stadium.createSectors(sectorNames, sectorSizes);
        n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(";");
            try {
                stadium.buyTicket(parts[0], Integer.parseInt(parts[1]),
                        Integer.parseInt(parts[2]));
            } catch (SeatNotAllowedException e) {
                System.out.println("SeatNotAllowedException");
            } catch (SeatTakenException e) {
                System.out.println("SeatTakenException");
            }
        }
        stadium.showSectors();
    }
}

class SeatTakenException extends Exception{
    public SeatTakenException() {
    }
}

class SeatNotAllowedException extends Exception{
    public SeatNotAllowedException() {
    }
}

class Sector{
    String code;
    int size;
    Map<Integer, Integer> seats;
    int sectorInfo;

    public Sector(String code, int size) {
        this.code = code;
        this.size = size;
        sectorInfo = 0;
        seats = new HashMap<>();
    }

    public boolean checkTaken(int i){
        return seats.containsValue(i);
    }

    public void takeSeat(int i){
        seats.put(i,i);
    }

    public void setSectorInfo(int sectorInfo) {
        this.sectorInfo = sectorInfo;
    }

    public int getSectorInfo() {
        return sectorInfo;
    }

    public int freeSeats(){
        return size - seats.size();
    }

    public String getCode() {
        return code;
    }

    @Override
    public String toString() {
        return String.format("%s\t%d/%d\t%.1f%%", code,freeSeats(), size, (float)(size-freeSeats())/size*100);
    }
}

class Stadium{
    String name;
    Map<String,Sector> sectors;

    public Stadium(String name) {
        this.name = name;
        sectors = new HashMap<>();
    }

    void createSectors(String[] sectorNames, int[] sizes){
        for(int i=0; i<sectorNames.length; i++)
            sectors.put(sectorNames[i],new Sector(sectorNames[i],sizes[i]));
    }

    void buyTicket(String sectorName, int seat, int type) throws SeatTakenException, SeatNotAllowedException {
        Sector temp = sectors.get(sectorName);

        if(temp.checkTaken(seat))
            throw new SeatTakenException();


        if(temp.getSectorInfo()!=0){
            if((temp.getSectorInfo()==1 && type==2) || (temp.getSectorInfo()==2 && type==1))
                throw new SeatNotAllowedException();
        }
        if(type!=0)
            temp.setSectorInfo(type);
        temp.takeSeat(seat);
    }

    void showSectors(){
        sectors.values().stream()
                .sorted(Comparator.comparing(Sector::freeSeats)
                                .reversed()
                        .thenComparing(Sector::getCode))
                .forEach(System.out::println);
    }
}