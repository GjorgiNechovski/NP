import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class XMLTest {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int testCase = sc.nextInt();
        XMLComponent component = new XMLLeaf("student", "Trajce Trajkovski");
        component.addAttribute("type", "redoven");
        component.addAttribute("program", "KNI");

        XMLComposite composite = new XMLComposite("name");
        composite.addComponent(new XMLLeaf("first-name", "trajce"));
        composite.addComponent(new XMLLeaf("last-name", "trajkovski"));
        composite.addAttribute("type", "redoven");
        component.addAttribute("program", "KNI");

        if (testCase==1) {
            System.out.println(component);
            //TODO Print the component object
        } else if(testCase==2) {
            System.out.println(composite);
            //TODO print the composite object
        } else if (testCase==3) {
            XMLComposite main = new XMLComposite("level1");
            main.addAttribute("level","1");
            XMLComposite lvl2 = new XMLComposite("level2");
            lvl2.addAttribute("level","2");
            XMLComposite lvl3 = new XMLComposite("level3");
            lvl3.addAttribute("level","3");
            lvl3.addComponent(component);
            lvl2.addComponent(lvl3);
            lvl2.addComponent(composite);
            lvl2.addComponent(new XMLLeaf("something", "blabla"));
            main.addComponent(lvl2);
            main.addComponent(new XMLLeaf("course", "napredno programiranje"));

            System.out.println(main);
        }
    }
}

interface XMLComponent{
    void addAttribute(String type, String data);
}

abstract class XML implements XMLComponent{
    String id;
    Map<String, String> attributes;

    public XML(String id) {
        this.id = id;
        attributes = new LinkedHashMap<>();
    }

    @Override
    public void addAttribute(String type, String data) {
        attributes.put(type,data);
    }
}

class XMLLeaf extends XML{
    String data;

    public XMLLeaf(String id, String data) {
        super(id);
        this.data = data;
    }
}

class XMLComposite extends XML{
    List<XMLComponent> components;

    public XMLComposite(String id) {
        super(id);
        this.components = new ArrayList<>();
    }

    public void addComponent(XMLComponent component){
        components.add(component);
    }
}