import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;


public class ContactsTester {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int tests = scanner.nextInt();
        Faculty faculty = null;

        int rvalue = 0;
        long rindex = -1;

        DecimalFormat df = new DecimalFormat("0.00");

        for (int t = 0; t < tests; t++) {

            rvalue++;
            String operation = scanner.next();

            switch (operation) {
                case "CREATE_FACULTY": {
                    String name = scanner.nextLine().trim();
                    int N = scanner.nextInt();

                    Student[] students = new Student[N];

                    for (int i = 0; i < N; i++) {
                        rvalue++;

                        String firstName = scanner.next();
                        String lastName = scanner.next();
                        String city = scanner.next();
                        int age = scanner.nextInt();
                        long index = scanner.nextLong();

                        if ((rindex == -1) || (rvalue % 13 == 0))
                            rindex = index;

                        Student student = new Student(firstName, lastName, city,
                                age, index);
                        students[i] = student;
                    }

                    faculty = new Faculty(name, students);
                    break;
                }

                case "ADD_EMAIL_CONTACT": {
                    long index = scanner.nextInt();
                    String date = scanner.next();
                    String email = scanner.next();

                    rvalue++;

                    if ((rindex == -1) || (rvalue % 3 == 0))
                        rindex = index;

                    faculty.getStudent(index).addEmailContact(date, email);
                    break;
                }

                case "ADD_PHONE_CONTACT": {
                    long index = scanner.nextInt();
                    String date = scanner.next();
                    String phone = scanner.next();

                    rvalue++;

                    if ((rindex == -1) || (rvalue % 3 == 0))
                        rindex = index;

                    faculty.getStudent(index).addPhoneContact(date, phone);
                    break;
                }

                case "CHECK_SIMPLE": {
                    System.out.println("Average number of contacts: "
                            + df.format(faculty.getAverageNumberOfContacts()));

                    rvalue++;

                    String city = faculty.getStudent(rindex).getCity();
                    System.out.println("Number of students from " + city + ": "
                            + faculty.countStudentsFromCity(city));

                    break;
                }

                case "CHECK_DATES": {

                    rvalue++;

                    System.out.print("Latest contact: ");
                    Contact latestContact = faculty.getStudent(rindex)
                            .getLatestContact();
                    if (latestContact.getType().equals("Email"))
                        System.out.println(((EmailContact) latestContact)
                                .getEmail());
                    if (latestContact.getType().equals("Phone"))
                        System.out.println(((PhoneContact) latestContact)
                                .getPhone()
                                + " ("
                                + ((PhoneContact) latestContact).getOperator()
                                .toString() + ")");

                    if (faculty.getStudent(rindex).getEmailContacts().length > 0 && faculty.getStudent(rindex).getPhoneContacts().length > 0) {
                        System.out.print("Number of email and phone contacts: ");
                        System.out
                                .println(faculty.getStudent(rindex)
                                        .getEmailContacts().length
                                        + " "
                                        + faculty.getStudent(rindex)
                                        .getPhoneContacts().length);

                        System.out.print("Comparing dates: ");
                        int posEmail = rvalue
                                % faculty.getStudent(rindex).getEmailContacts().length;
                        int posPhone = rvalue
                                % faculty.getStudent(rindex).getPhoneContacts().length;

                        System.out.println(faculty.getStudent(rindex)
                                .getEmailContacts()[posEmail].isNewerThan(faculty
                                .getStudent(rindex).getPhoneContacts()[posPhone]));
                    }

                    break;
                }

                case "PRINT_FACULTY_METHODS": {
                    System.out.println("Faculty: " + faculty.toString());
                    System.out.println("Student with most contacts: "
                            + faculty.getStudentWithMostContacts().toString());
                    break;
                }

            }

        }

        scanner.close();
    }
}

abstract class Contact {
    String date;

    public Contact(String date) {
        this.date = date;
    }

    boolean isNewerThan(Contact c) {

        int vrati = date.compareTo(c.getDate());
        if (vrati > 0)
            return true;
        else
            return false;
    }

    abstract String getType();

    public String getDate() {
        return date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Contact contact = (Contact) o;
        return Objects.equals(date, contact.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date);
    }
}

class EmailContact extends Contact {
    String email;

    public EmailContact(String date, String email) {
        super(date);
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    @Override
    String getType() {
        return "Email";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        EmailContact that = (EmailContact) o;
        return Objects.equals(email, that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), email);
    }
}

enum Operator {VIP, ONE, TMOBILE};

class PhoneContact extends Contact {

    String phone;
    Operator operator;

    public PhoneContact(String date, String phone) {
        super(date);
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public Operator getOperator() {
        String k = phone.substring(2, 3);
        int broj = Integer.parseInt(k);

        if (broj >= 0 && broj <= 2)
            operator = Operator.TMOBILE;
        else if (broj == 5 || broj == 6)
            operator = Operator.ONE;
        else if (broj == 7 || broj == 8)
            operator = Operator.VIP;

        return operator;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        PhoneContact that = (PhoneContact) o;
        return Objects.equals(phone, that.phone) && operator == that.operator;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), phone, operator);
    }

    @Override
    String getType() {
        return "Phone";
    }
}

class Student {
    String firstName;
    String lastName;
    String city;
    int age;
    long index;
    Contact[] niza;

    public Student(String firstName, String lastName, String city, int age, long index) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.city = city;
        this.age = age;
        this.index = index;
        niza = new Contact[0];
    }

    public void addEmailContact(String date, String email) {
        Contact[] temp = new Contact[niza.length+1];
        for (int i=0; i<niza.length; i++)
            temp[i] = niza[i];
        temp[niza.length] = new EmailContact(date,email);
        niza=temp;
    }

    public void addPhoneContact(String date, String phone) {
        Contact[] temp = new Contact[niza.length+1];
        for (int i=0; i<niza.length; i++)
            temp[i] = niza[i];
        temp[niza.length] = new PhoneContact(date,phone);
        niza=temp;
    }

    Contact[] getEmailContacts() {
        Contact[]array = new Contact[0];
        for (int i=0; i<niza.length; i++){
            if(niza[i].getType()=="Email"){
                EmailContact temp[] = new EmailContact[array.length+1];
                for (int j=0; j<array.length; j++)
                    temp[j] = (EmailContact) array[j];
                temp[array.length] = (EmailContact) niza[i];
                array=temp;
            }
        }
        return array;
    }

    Contact[] getPhoneContacts() {
        Contact[]array = new Contact[0];
        for (int i=0; i<niza.length; i++){
            if(niza[i].getType()=="Phone"){
                PhoneContact temp[] = new PhoneContact[array.length+1];
                for (int j=0; j<array.length; j++)
                    temp[j] = (PhoneContact) array[j];
                temp[array.length] = (PhoneContact) niza[i];
                array=temp;
            }
        }
        return array;
    }

    Contact getLatestContact() {
        Contact latest = niza[0];

        for (int i = 1; i < niza.length; i++) {
            if (niza[i].isNewerThan(latest))
                latest = niza[i];
        }
        return latest;
    }

    @Override
    public String toString() {
        String str =  "{" +
                "\"ime\":\"" + firstName + "\", " +
                "\"prezime\":\"" + lastName + "\", " +
                "\"vozrast\":" + age + ", " +
                "\"grad\":\"" + city + "\", " +
                "\"indeks\":" + index + ", " +
                "\"telefonskiKontakti\":[";
        for(Contact c : this.getPhoneContacts()){
            PhoneContact pc = (PhoneContact) c;
            str = str.concat("\""+pc.getPhone()+"\"");
            str+=", ";
        }
        if(this.getPhoneContacts().length>0) str = str.substring(0,str.length()-2);
        str+="], \"emailKontakti\":[";
        for(Contact c : this.getEmailContacts()){
            EmailContact pc = (EmailContact) c;
            str = str.concat("\""+pc.getEmail()+"\"");
            str+=", ";
        }
        if(this.getEmailContacts().length>0)  str = str.substring(0,str.length()-2);
        str+="]}";
        return str;
    }

    public int getNumberOfContacts() {
        return niza.length;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getCity() {
        return city;
    }

    public int getAge() {
        return age;
    }

    public long getIndex() {
        return index;
    }
}

class Faculty {
    String name;
    Student[] students;

    public Faculty(String name, Student[] students) {
        this.name = name;
        this.students = students;
    }

    int countStudentsFromCity(String cityName) {
        int count = 0;
        for (Student student : students)
            if (student.getCity().equals(cityName))
                count++;
        return count;
    }

    Student getStudent(long index) {
        int vrati = 0;

        for (int i = 0; i < students.length; i++) {
            if (students[i].getIndex() == index)
                vrati = i;
        }
        return students[vrati];
    }

    double getAverageNumberOfContacts() {
        double sum = 0;

        for (Student student : students) {
            sum += student.getNumberOfContacts();
        }
        return sum / students.length;
    }

    Student getStudentWithMostContacts() {
        int vrati = 0;

        for (int i = 1; i < students.length; i++) {
            if (students[i].getNumberOfContacts() > students[vrati].getNumberOfContacts())
                vrati = i;
            else if (students[i].getNumberOfContacts() == students[vrati].getNumberOfContacts()) {
                if (students[i].getIndex() > students[vrati].getIndex())
                    vrati = i;
            }
        }
        return students[vrati];
    }

    @Override
    public String toString() {
        String str = "{" +
                "\"fakultet\":\"" + name + "\", " +
                "\"studenti\":[";
        for(Student s : students) {
            str+=s.toString()+", ";
        }
        if(students.length>0) str = str.substring(0,str.length()-2);
        str+="]}";
        return str;
    }
}