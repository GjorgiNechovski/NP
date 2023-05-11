import java.util.*;
import java.util.stream.Collectors;

public class MoviesTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        MoviesList moviesList = new MoviesList();
        int n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String title = scanner.nextLine();
            int x = scanner.nextInt();
            int[] ratings = new int[x];
            for (int j = 0; j < x; ++j) {
                ratings[j] = scanner.nextInt();
            }
            scanner.nextLine();
            moviesList.addMovie(title, ratings);
        }
        scanner.close();
        List<Movie> movies = moviesList.top10ByAvgRating();
        System.out.println("=== TOP 10 BY AVERAGE RATING ===");
        for (Movie movie : movies) {
            System.out.println(movie);
        }
        movies = moviesList.top10ByRatingCoef();
        System.out.println("=== TOP 10 BY RATING COEFFICIENT ===");
        for (Movie movie : movies) {
            System.out.println(movie);
        }
    }
}

// vashiot kod ovde

class MoviesList{
    List<Movie> movies = new ArrayList<>();

    public void addMovie(String title, int[] ratings){
        movies.add(new Movie(title,ratings));
    }

    public List<Movie> top10ByAvgRating(){
        return movies.stream()
                .sorted(Comparator.comparing(Movie::rating)
                    .reversed()
                    .thenComparing(Movie::getTitle))
                .limit(10)
                .collect(Collectors.toList());

    }
    //просечен ретјтинг на филмот x вкупно број на рејтинзи на филмот / максимален број на рејтинзи (од сите филмови во листата)
    public List<Movie> top10ByRatingCoef(){
        return movies.stream()
                .sorted(Comparator.comparing(this::coefficient)
                        .reversed()
                        .thenComparing(Movie::getTitle))
                .limit(10).collect(Collectors.toList());
    }

    private double coefficient(Movie temp){
        return temp.rating()*temp.size()/
                movies.stream().mapToDouble(Movie::rating).max().orElse(1);
    }
}

class Movie{
    String title;
    List<Integer> ratings = new ArrayList<>();

    public Movie(String title, int[] ratings) {
        this.title = title;
        Arrays.stream(ratings).forEach(x->this.ratings.add(x));
    }

    public double rating(){
        double sum =0;
        for (Integer rating : ratings)
            sum += rating;

        return sum/ratings.size();
    }

    public String getTitle() {
        return title;
    }

    public int size(){
        return ratings.size();
    }

    @Override
    public String toString() {
        return String.format("%s (%.2f) of %d ratings", title,rating(),ratings.size());
    }
}