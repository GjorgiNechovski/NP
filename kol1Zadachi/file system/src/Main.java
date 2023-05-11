import java.nio.file.FileSystem;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.stream.Collectors;

public class FileSystemTest {

    public static Folder readFolder (Scanner sc)  {

        Folder folder = new Folder(sc.nextLine());
        int totalFiles = Integer.parseInt(sc.nextLine());

        for (int i=0;i<totalFiles;i++) {
            String line = sc.nextLine();

            if (line.startsWith("0")) {
                String fileInfo = sc.nextLine();
                String [] parts = fileInfo.split("\\s+");
                try {
                    folder.addFile(new File(parts[0], Long.parseLong(parts[1])));
                } catch (FileNameExistsException e) {
                    System.out.println(e.getMessage());
                }
            }
            else {
                try {
                    folder.addFile(readFolder(sc));
                } catch (FileNameExistsException e) {
                    System.out.println(e.getMessage());
                }
            }
        }

        return folder;
    }

    public static void main(String[] args)  {

        //file reading from input

        Scanner sc = new Scanner(System.in);

        System.out.println("===READING FILES FROM INPUT===");
        FileSystem fileSystem = new FileSystem();
        try {
            fileSystem.addFile(readFolder(sc));
        } catch (FileNameExistsException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("===PRINTING FILE SYSTEM INFO===");
        System.out.println(fileSystem.toString());

        System.out.println("===PRINTING FILE SYSTEM INFO AFTER SORTING===");
        fileSystem.sortBySize();
        System.out.println(fileSystem.toString());

        System.out.println("===PRINTING THE SIZE OF THE LARGEST FILE IN THE FILE SYSTEM===");
        System.out.println(fileSystem.findLargestFile());




    }
}

interface IFile{
    String getFileName();
    long getFileSize();
    String getFileInfo(int index);
    void sortBySize();
    int findLargestFile();
}

class File implements IFile, Comparable<File>{
    String name;
    long size;


    @Override
    public String getFileName() {
        return name;
    }

    @Override
    public long getFileSize() {
        return size;
    }

    @Override
    public String getFileInfo(int index) {
        return String.format(("%s"));
    }

    @Override
    public void sortBySize() {
    }

    @Override
    public int findLargestFile() {
        return 0;
    }

    @Override
    public int compareTo(File o) {
        return Long.compare(size,o.getFileSize());
    }
}

class FileNameExistsException extends Exception{
    public FileNameExistsException(String name) {
        super(name);
    }
}

class Folder implements IFile, Comparable<Folder>{
    String name;
    long size;
    List<IFile> files;

    void checkName(String name) throws FileNameExistsException {
        ArrayList<IFile> temp = new ArrayList<>();
        temp = (ArrayList<IFile>) files.stream().filter(x-> Objects.equals(x.getFileName(), name)).collect(Collectors.toList());
        if(temp.size()>0)
            throw new FileNameExistsException(name);
    }

    void addFile (IFile file) throws FileNameExistsException {
        checkName(file.getFileName());

        files.add(file);
    }

    @Override
    public String getFileName() {
        return name;
    }

    @Override
    public long getFileSize() {
        size = files.stream().mapToLong(IFile::getFileSize).sum();
        return size;
    }

    @Override
    public String getFileInfo(int index) {
        return null;
    }

    @Override
    public void sortBySize() {

    }

    @Override
    public int findLargestFile() {
        return 0;
    }

    @Override
    public int compareTo(Folder o) {
        return Long.compare(size,o.size);
    }
}