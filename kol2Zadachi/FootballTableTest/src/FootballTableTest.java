import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Partial exam II 2016/2017
 */
public class FootballTableTest {
    public static void main(String[] args) throws IOException {
        FootballTable table = new FootballTable();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        reader.lines()
                .map(line -> line.split(";"))
                .forEach(parts -> table.addGame(parts[0], parts[1],
                        Integer.parseInt(parts[2]),
                        Integer.parseInt(parts[3])));
        reader.close();
        System.out.println("=== TABLE ===");
        System.out.printf("%-19s%5s%5s%5s%5s%5s\n", "Team", "P", "W", "D", "L", "PTS");
        table.printTable();
    }
}

// Your code here

class FootballTable {
    Map<String, Team> teams;

    public FootballTable() {
        teams = new HashMap<>();
    }

    public void addGame(String homeTeam, String awayTeam, int homeGoals, int awayGoals){
        teams.putIfAbsent(homeTeam, new Team(homeTeam));
        teams.putIfAbsent(awayTeam, new Team(awayTeam));

        teams.get(homeTeam).update(homeGoals,awayGoals);
        teams.get(awayTeam).update(awayGoals,homeGoals);
    }

    public  void printTable(){
        List<Team> table = teams.values()
                .stream()
                .sorted(Comparator.comparing((Team::getPoints))
                        .thenComparing(Team::getGoals)
                        .reversed()
                        .thenComparing(Team::getName))
                .collect(Collectors.toList());

        IntStream.range(0,table.size()).forEach(i->System.out.printf("%2d. %s%n", i+1, table.get(i)));
    }
}

class Team{
    String name;
    int playedGames;
    int wins;
    int draws;
    int loses;
    int goals;
    int points;

    public Team(String name) {
        this.name = name;
    }

    public void update(int homeGoals, int awayGoals){
        if(homeGoals>awayGoals) {
            wins++;
            points+=3;
        }
        else if (homeGoals<awayGoals)
            loses++;
        else {
            draws++;
            points++;
        }

        goals+=homeGoals-awayGoals;
        playedGames++;
    }

    public String getName() {
        return name;
    }

    public int getGoals() {
        return goals;
    }

    public int getPoints() {
        return points;
    }

    @Override
    public String toString() {
        return String.format("%-15s%5d%5d%5d%5d%5d", name, playedGames, wins, draws, loses, points);
    }

}
