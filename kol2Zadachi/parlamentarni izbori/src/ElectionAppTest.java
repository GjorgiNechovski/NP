import java.util.*;
import java.util.stream.Collectors;

public class ElectionAppTest {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        List<String> parties = Arrays.stream(sc.nextLine().split("\\s+")).collect(Collectors.toList());
        Map<String, Integer> unitPerPoll = new HashMap<>();
        Map<Integer, ElectionUnit> electionUnitMap = new TreeMap<>();

        int totalUnits = Integer.parseInt(sc.nextLine());
        for (int i = 0; i < totalUnits; i++) {
            Map<String, Integer> votersPerPoll = new HashMap<>();
            String line = sc.nextLine();
            String[] parts = line.split("\\s+");
            Integer unit = Integer.parseInt(parts[0]);
            for (int j=1;j<parts.length;j+=2) {
                String pollId = parts[j];
                int voters = Integer.parseInt(parts[j+1]);
                unitPerPoll.putIfAbsent(pollId, unit);
                votersPerPoll.put(pollId, voters);
            }

            electionUnitMap.putIfAbsent(unit, new ElectionUnit(unit, votersPerPoll));
        }
        VotesController controller = new VotesController(parties, unitPerPoll);

        electionUnitMap.values().forEach(controller::addElectionUnit);

        VotersTurnoutApp votersTurnoutApp = new VotersTurnoutApp();
        SeatsApp seatsApp = new SeatsApp();


        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String [] parts = line.split("\\s+");
            String testCase = parts[0];

            if (testCase.equals("subscribe")) { //Example: subscribe votersTurnoutApp 1
                int unit = Integer.parseInt(parts[1]);
                String app = parts[2];
                if (app.equals("votersTurnoutApp")) {
                    electionUnitMap.get(unit).subscribe(votersTurnoutApp);
                } else {
                    electionUnitMap.get(unit).subscribe(seatsApp);
                }
            } else if (testCase.equals("unsubscribe")) { //Example: unsubscribe votersTurnoutApp 1
                int unit = Integer.parseInt(parts[1]);
                String app = parts[2];
                if (app.equals("votersTurnoutApp")) {
                    electionUnitMap.get(unit).unsubscribe(votersTurnoutApp);
                } else {
                    electionUnitMap.get(unit).unsubscribe(seatsApp);
                }
            } else if (testCase.equals("addVotes")) { // Example: addVotes 1234 A 1000
                String pollId = parts[1];
                String party = parts[2];
                int votes = Integer.parseInt(parts[3]);
                try {
                    controller.addVotes(pollId, party, votes);
                } catch (InvalidVotesException e) {
                    e.printStackTrace();
                }
            } else if (testCase.equals("printStatistics")) {
                String app = parts[1];
                if (app.equals("votersTurnoutApp")) {
                    System.out.println("PRINTING STATISTICS FROM VOTERS TURNOUT APPLICATION");
                    votersTurnoutApp.printStatistics();
                } else {
                    System.out.println("PRINTING STATISTICS FROM SEATS APPLICATION");
                    seatsApp.printStatistics();
                }
            }
        }
    }
}

class InvalidVotesException extends Exception{
    public InvalidVotesException() {
    }
}

interface Subscriber{
    void updateVotes(int unit, String pollId, String party, int votes, int totalVotersPerPoll, int totalVotersPerUnit);
}

class VotesController {
    List<String> parties;
    Map<String, Integer> unitPerPoll;
    Map<Integer, ElectionUnit> electionUnits = new HashMap<>();

    public VotesController(List<String> parties, Map<String, Integer> unitPerPoll) {
        this.parties = parties;
        this.unitPerPoll = unitPerPoll;
    }

    void addElectionUnit(ElectionUnit electionUnit){
        electionUnits.put(electionUnit.unit,electionUnit);
    }

    void addVotes(String pollId, String party, int votes) throws InvalidVotesException {
        if(!unitPerPoll.containsKey(pollId))
            throw new InvalidVotesException();

        int unit = unitPerPoll.get(pollId);

        electionUnits.get(unit).addVotes(pollId,party,votes);
    }
}

class ElectionUnit {
    int unit;
    Map<String, Integer> votersByPoll;
    List<Subscriber> subscribers = new ArrayList<>();

    public ElectionUnit(int unit, Map<String, Integer> votersByPoll) {
        this.unit = unit;
        this.votersByPoll = votersByPoll;
    }

    void addVotes(String pollId, String party, int votes){
        subscribers.forEach(subscriber -> subscriber.updateVotes(unit,pollId,party,votes,votersByPoll.get(pollId),
                votersByPoll.values().stream().mapToInt(i->i).sum()));
    }

    void subscribe(Subscriber subscriber){
        subscribers.add(subscriber);
    }

    void unsubscribe(Subscriber subscriber){
        subscribers.remove(subscriber);
    }
}

class VotersTurnoutApp implements Subscriber{

    Map<Integer, Integer> howManyInTotal;
    Map<Integer, Integer> howManyVoted;

    public VotersTurnoutApp() {
        howManyInTotal = new HashMap<>();
        howManyVoted = new HashMap<>();
    }

    void printStatistics(){
        System.out.println("Unit: Casted: Voters: Turnout:");

        for (int key: howManyInTotal.keySet()) {
            System.out.printf("%10d %7d %7d %7.2f%%\n", key, howManyInTotal.get(key), howManyVoted.get(key), howManyVoted.get(key)*100.0/howManyInTotal.get(key));
        }
    }

    @Override
    public void updateVotes(int unit, String pollId, String party, int votes, int totalVotersPerPoll, int totalVotersPerUnit) {
        howManyInTotal.putIfAbsent(unit,0);
        howManyInTotal.put(unit, howManyInTotal.get(unit) + votes);

        howManyVoted.put(unit,totalVotersPerUnit);
    }
}

class SeatsApp implements Subscriber{
    void printStatistics(){

    }
    @Override
    public void updateVotes(int unit, String pollId, String party, int votes, int totalVotersPerPoll, int totalVotersPerUnit) {

    }
}