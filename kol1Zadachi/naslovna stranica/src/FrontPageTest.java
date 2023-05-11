import java.util.*;
import java.util.stream.Collectors;

public class FrontPageTest {
    public static void main(String[] args) {
        // Reading
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();
        String[] parts = line.split(" ");
        Category[] categories = new Category[parts.length];
        for (int i = 0; i < categories.length; ++i) {
            categories[i] = new Category(parts[i]);
        }
        int n = scanner.nextInt();
        scanner.nextLine();
        FrontPage frontPage = new FrontPage(categories);
        Calendar cal = Calendar.getInstance();
        for (int i = 0; i < n; ++i) {
            String title = scanner.nextLine();
            cal = Calendar.getInstance();
            int min = scanner.nextInt();
            cal.add(Calendar.MINUTE, -min);
            Date date = cal.getTime();
            scanner.nextLine();
            String text = scanner.nextLine();
            int categoryIndex = scanner.nextInt();
            scanner.nextLine();
            TextNewsItem tni = new TextNewsItem(title, date, categories[categoryIndex], text);
            frontPage.addNewsItem(tni);
        }

        n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String title = scanner.nextLine();
            int min = scanner.nextInt();
            cal = Calendar.getInstance();
            cal.add(Calendar.MINUTE, -min);
            scanner.nextLine();
            Date date = cal.getTime();
            String url = scanner.nextLine();
            int views = scanner.nextInt();
            scanner.nextLine();
            int categoryIndex = scanner.nextInt();
            scanner.nextLine();
            MediaNewsItem mni = new MediaNewsItem(title, date, categories[categoryIndex], url, views);
            frontPage.addNewsItem(mni);
        }
        // Execution
        String category = scanner.nextLine();
        System.out.println(frontPage);
        for(Category c : categories) {
            System.out.println(frontPage.listByCategory(c).size());
        }
        try {
            System.out.println(frontPage.listByCategoryName(category).size());
        } catch(CategoryNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
}


// Vasiot kod ovde

class CategoryNotFoundException extends Exception{
    public CategoryNotFoundException(String message) {
        super(String.format("Category %s was not found", message));
    }
}

class Category{
    String name;

    public Category(String name) {
        this.name = name;
    }

}

abstract class NewsItem{
    String title;
    Date publish;
    Category category;

    public NewsItem(String title, Date publish, Category category) {
        this.title = title;
        this.publish = publish;
        this.category = category;
    }

    abstract String getTeaser();
    public int getTime() {
        Date now = new Date();
        long ms = now.getTime() - publish.getTime();
        return (int) (ms / 1000) / 60;
    }
}

class TextNewsItem extends NewsItem{
    String text;

    public TextNewsItem(String title, Date publish, Category category, String text) {
        super(title, publish, category);
        this.text = text;
    }


    @Override
    String getTeaser() {
        if (text.length()>80)
            return (String.format("%s\n%d\n%s\n",title,getTime(),text.substring(0,80)));
        return (String.format("%s\n%d\n%s\n",title,getTime(),text));
    }
}



class MediaNewsItem extends NewsItem{
    String url;
    int visits;

    public MediaNewsItem(String title, Date publish, Category category, String url, int visits) {
        super(title, publish, category);
        this.url = url;
        this.visits = visits;
    }

    @Override
    String getTeaser() {
        return String.format("%s\n%d\n%s\n%d\n", title,getTime(),url,visits);
    }
}

class FrontPage{
    List<NewsItem> news;
    Category[] categories;

    public FrontPage(Category[] categories) {
        news = new ArrayList<>();
        this.categories = categories;
    }

    void addNewsItem(NewsItem newsItem){
        news.add(newsItem);
    }

    List<NewsItem> listByCategory(Category category){
        return news.stream().filter(x -> x.category.equals(category)).collect(Collectors.toList());
    }

    List<NewsItem> listByCategoryName(String category) throws CategoryNotFoundException {
       List<NewsItem> temp = news.stream()
                .filter(x -> x.category.name.equals(category))
                .collect(Collectors.toList());

        if(temp.size()==0)
            throw new CategoryNotFoundException(category);
        return temp;

        /*
        int temp = -1;
		for (int i = 0; i < categories.length; ++i) {
			if (categories[i].getName().equals(category)) {
				temp = i;
				break;
			}
		}
		if (temp == -1)
			throw new CategoryNotFoundException(category);

		return listByCategory(categories[temp]);*/

    }

    @Override
    public String toString() {
        StringBuilder bl = new StringBuilder();
        for (NewsItem newsItem : news) bl.append(newsItem.getTeaser());
        return bl.toString();
    }
}