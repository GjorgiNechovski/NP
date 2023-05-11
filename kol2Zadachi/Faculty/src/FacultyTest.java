import java.util.*;
import java.util.stream.Collectors;

public class FacultyTest {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Faculty faculty = new Faculty();

        while (sc.hasNext()) {
            String line = sc.nextLine();
            String[] parts = line.split("\\s++");
            if (parts[0].equals("addInfo")) {
                String courseId = parts[1];
                String studentId = parts[2];
                int totalPoints = Integer.parseInt(parts[3]);
                faculty.addInfo(courseId, studentId, totalPoints);
            } else if (parts[0].equals("printCourseReport")) {
                String courseId = parts[1];
                String comparator = parts[2];
                boolean descending = Boolean.parseBoolean(parts[3]);
                faculty.printCourseReport(courseId, comparator, descending);
            } else if (parts[0].equals("printStudentReport")) { //printStudentReport
                String studentId = parts[1];
                faculty.printStudentReport(studentId);
            } else {
                String courseId = parts[1];
                Map<Integer, Integer> grades = faculty.gradeDistribution(courseId);
                grades.forEach((key, value) -> System.out.println(String.format("%2d -> %3d", key, value)));
            }
        }
    }
}

class Faculty{
    Map<String, List<Student>> studentsOnCourse;
    Map<String, List<Student>> courses;
    public Faculty() {
        studentsOnCourse = new HashMap<>();
        courses = new HashMap<>();
    }

    void addInfo (String courseId, String studentId, int totalPoints){
        Student addMe = new Student(studentId,totalPoints, courseId);

        studentsOnCourse.putIfAbsent(courseId, new ArrayList<>());
        studentsOnCourse.get(courseId).add(addMe);

        courses.putIfAbsent(studentId,new ArrayList<>());
        courses.get(studentId).add(addMe);
    }

    void printCourseReport (String courseId, String comparator, boolean descending){

        Comparator<Student> comparator1;

        if(comparator.equalsIgnoreCase("byid"))
            comparator1 = Comparator.comparing(Student::getId);
        else
            comparator1 = Comparator.comparing(Student::getGrade)
                    .thenComparing(Student::getTotalPoints)
                    .thenComparing(Student::getId);

        if(descending)
            comparator1 = comparator1.reversed();

        studentsOnCourse.get(courseId)
                .stream()
                .sorted(comparator1)
                .forEach(System.out::println);
    }

    void printStudentReport (String studentId){
        Comparator<Student> comparator = Comparator.comparing(Student::getCourseId);

        courses.get(studentId)
                .stream()
                .sorted(comparator)
                .forEach(x->System.out.println(x.courseGrade()));
    }

    Map<Integer, Integer> gradeDistribution (String courseId){
        Map<Integer,Integer> returnThis = new HashMap<>();

        studentsOnCourse.get(courseId)
                .forEach(x->{
                    int grade = x.getGrade();
                    if(returnThis.get(grade)==null)
                        returnThis.put(grade,0);
                    else{
                        int frequency = returnThis.get(grade);
                        frequency++;
                        returnThis.put(grade,frequency);
                    }
                });
        return returnThis;
    }
}

class Student{
    String courseId;
    String id;
    int totalPoints;

    public Student(String id, int totalPoints, String courseId) {
        this.id = id;
        this.totalPoints = totalPoints;
        this.courseId = courseId;
    }

    public String getId() {
        return id;
    }

    public int getTotalPoints() {
        return totalPoints;
    }

    public int getGrade(){
        int grade = totalPoints/10;
        grade++;

        if(grade<=5)
            return 5;
        return grade;
    }

    public String getCourseId() {
        return courseId;
    }

    @Override
    public String toString() {
        return String.format("%s %d (%d)", id,totalPoints,getGrade());
    }

    public String courseGrade(){
        return String.format("%s %d (%d)", courseId,totalPoints,getGrade());
    }
}

