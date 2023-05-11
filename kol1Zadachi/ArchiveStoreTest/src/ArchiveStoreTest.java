import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


public class ArchiveStoreTest {
    public static void main(String[] args) {
        ArchiveStore store = new ArchiveStore();
        Date date = new Date(113, 10, 7);
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
        int n = scanner.nextInt();
        scanner.nextLine();
        scanner.nextLine();
        int i;
        for (i = 0; i < n; ++i) {
            int id = scanner.nextInt();
            long days = scanner.nextLong();
            Date dateToOpen = new Date(date.getTime() + (days * 24 * 60
                    * 60 * 1000));
            LockedArchive lockedArchive = new LockedArchive(id, dateToOpen);
            store.archiveItem(lockedArchive, date);
        }
        scanner.nextLine();
        scanner.nextLine();
        n = scanner.nextInt();
        scanner.nextLine();
        scanner.nextLine();
        for (i = 0; i < n; ++i) {
            int id = scanner.nextInt();
            int maxOpen = scanner.nextInt();
            SpecialArchive specialArchive = new SpecialArchive(id, maxOpen);
            store.archiveItem(specialArchive, date);
        }
        scanner.nextLine();
        scanner.nextLine();
        while(scanner.hasNext()) {
            int open = scanner.nextInt();
            try {
                store.openItem(open, date);
            } catch(NonExistingItemException e) {
                System.out.println(e.getMessage());
            }
        }
        System.out.println(store.getLog());
    }
}

// вашиот код овде

class NonExistingItemException extends Exception{
    public NonExistingItemException(int id) {
        super(String.format("Item with id %d doesn't exist", id));
    }
}

class CantOpen extends Exception{
    public CantOpen(String problem) {
        super(problem);
    }
}

class ArchiveStore{
    List<Archive> archives;
    String log="";

    public ArchiveStore() {
        archives = new ArrayList<Archive>();
    }

    public void archiveItem(Archive item, Date date){
        item.setDateArchived(date);
        archives.add(item);
        log+=String.format("Item %d archived at %s\n", item.id, date);
    }

    void openItem(int id, Date date) throws NonExistingItemException {
        List<Archive> temp = archives.stream().filter(x->x.getId()==id && x.getDateArchived()==date).collect(Collectors.toList());

        if(temp.size()==0)
            throw new NonExistingItemException(id);

        try{
            temp.get(0).checkIfOpenable(date);
            log+=String.format("Item %d opened at %s\n", id,date);
        }
        catch (Exception e){
            log+=e.getMessage();
        }

    }

    public String getLog(){
        log = log.replaceAll("GMT", "UTC");
        return log;
    }
}

abstract class Archive{
    int id;
    Date dateArchived;

    public Archive(int id) {
        this.id = id;
    }

    public void setDateArchived(Date dateArchived) {
        this.dateArchived = dateArchived;
    }

    public int getId() {
        return id;
    }

    public Date getDateArchived() {
        return dateArchived;
    }
    abstract public boolean checkIfOpenable(Date date) throws CantOpen;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Archive archive = (Archive) o;
        return id == archive.id && Objects.equals(dateArchived, archive.dateArchived);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dateArchived);
    }
}

class LockedArchive extends Archive{
    Date dateToOpen;

    public LockedArchive(int id, Date dateToOpen) {
        super(id);
        this.dateToOpen = dateToOpen;
    }

    @Override
    public boolean checkIfOpenable(Date date) throws CantOpen {
        if(date.getTime()<dateToOpen.getTime())
            throw new CantOpen(String.format("Item %d cannot be opened before %s\n", id, dateToOpen));
        return true;
    }
}

class SpecialArchive extends Archive{
    int maxOpen;
    int opened;

    public SpecialArchive(int id, int maxOpen) {
        super(id);
        this.maxOpen = maxOpen;
        opened=0;
    }

    @Override
    public boolean checkIfOpenable(Date date) throws CantOpen {
        opened++;
        if(opened>maxOpen)
            throw new CantOpen(String.format("Item %d cannot be opened more than %d times\n", id, maxOpen));
        return true;
    }
}

