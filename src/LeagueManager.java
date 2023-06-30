import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

public class LeagueManager {

    private List<Match> matches;
    private List<Team> teams;
    private Map<Team,Integer> leagueTable;

    public LeagueManager() {
        this.teams = createTeam();
        this.matches = matchTeams();
        List<Match> originalMatches = this.matches;
    List<Match> tempMatches = new ArrayList<>();
    while (tempMatches.size()<45){
        Collections.shuffle(this.matches);
        IntStream.range(0,9).forEach(i ->{
            tempMatches.addAll(makeRoundsMatches());
        });
        this.matches=originalMatches;
    }
    this.matches = tempMatches;
        this.leagueTable = new HashMap<>();
        this.teams.forEach(i->{
            this.leagueTable.put(i,0);
        });

    }

    private  List<Match> makeRoundsMatches() {
        List<Match> tempMatches = new ArrayList<>();
        IntStream.range(0,5).forEach(i ->{
            this.matches.stream().forEach(match -> {
                if (!match.getHomeTeam().isPlay() && !match.getAwayTeam().isPlay()) {
                    tempMatches.add(match);
                    match.getHomeTeam().setPlay(true);
                    match.getAwayTeam().setPlay(true);
                }
            });
        });
        tempMatches.stream().forEach(match -> {
            match.getHomeTeam().setPlay(false);
            match.getAwayTeam().setPlay(false);
        });
        this.matches.removeAll(tempMatches);
        return tempMatches;
    }

    public void startGame() {
        IntStream.range(0, 45)
                .forEach(i -> {
                    Match match = this.matches.get(i);
                    System.out.println(match.getHomeTeam().getName() + "-------VS-------" + match.getAwayTeam().getName());
                    countDown();
                    match.creatGoals();
                    Map<Team, Integer> teamGoals = printResult(match);
                    System.out.println("League Table:");
                    updateLeagueTable(match, teamGoals);
                    this.leagueTable.forEach((team, score) -> System.out.println(team + ": " + score));
                    match.playGame();
                    if ((i % 5 == 0 && i != 0) || i == 44) {
                        System.out.println("Menu:");
                        System.out.println(Constants.MENU);
                        outcome();
                    }
                });
    }


    private void updateLeagueTable(Match match, Map<Team, Integer> teamGoals) {
        Team homeTeam = match.getHomeTeam();
        Team awayTeam = match.getAwayTeam();

        int homeGoals = teamGoals.get(homeTeam);
        int awayGoals = teamGoals.get(awayTeam);

        int homePoints = (homeGoals > awayGoals) ? 3 : (homeGoals < awayGoals) ? 0 : 1;
        int awayPoints = (awayGoals > homeGoals) ? 3 : (awayGoals < homeGoals) ? 0 : 1;

        this.leagueTable.merge(homeTeam, homePoints, Integer::sum);
        this.leagueTable.merge(awayTeam, awayPoints, Integer::sum);

        this.leagueTable = this.leagueTable.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }


    private Map<Team, Integer> printResult(Match match) {
        Map<Player, Long> playersGoals = match.getGoals().stream()
                .collect(Collectors.groupingBy(Goal::getScorer, Collectors.counting()));

        Map<Team, Integer> teamGoals = this.teams.stream()
                .collect(Collectors.toMap(team -> team, team -> 0));

        playersGoals.forEach((player, goals) ->
                this.teams.stream()
                        .filter(team -> team.getPlayers().contains(player))
                        .forEach(team -> teamGoals.merge(team, goals.intValue(), Integer::sum)));

        System.out.println("Final score of a football match:");
        System.out.println(teamGoals.get(match.getHomeTeam()) + ":" + teamGoals.get(match.getAwayTeam()));

        return teamGoals;
    }




    private void countDown(){
    IntStream.iterate(10, i -> i - 1)
                    .limit(10)
                    .forEach(num -> {
                        System.out.println(num);
                        Utils.sleep(1000);
                    });
}


//1
   private List<Match> findMatchesByTeam(int teamId){
       System.out.println("The matches the team played are");
       return this.matches.stream().filter(match -> match.isSameTeamById(teamId)&&match.isPlay()).toList();
   }
// 2
    public List<Team> findTopScoringTeams(int n) {
        return this.leagueTable.keySet().stream().limit(n).toList();
    }
    //3
    public List<Player> findPlayersWithAtLeastNGoals(int n) {
        Map<Player, Long> playerGoals = this.matches.stream().map(Match::getGoals).flatMap(List::stream).map(Goal::getScorer)
                .collect(groupingBy(player -> player, counting()));
        return playerGoals.entrySet().stream().filter(playerLongEntry -> playerLongEntry.getValue() >= n).map(Map.Entry::getKey).toList();
    }
    //4
    private Team getTeamByPosition(int position) {
        return this.leagueTable.entrySet().stream().map(Map.Entry::getKey).toList().get(position-1);
    }
    //5
    public Map<Integer, Integer> getTopScorers(int n) {
        Map<Integer, Long> playerGoals = this.matches.stream()
                .flatMap(match -> match.getGoals().stream())
                .map(Goal::getScorer)
                .collect(Collectors.groupingBy(Player::getId, Collectors.counting()));
        return playerGoals.entrySet().stream()
                .sorted(Map.Entry.<Integer, Long>comparingByValue().reversed())
                .limit(n)
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().intValue()));

    }


    private List<Team> createTeam(){
        List<String> teamsNames= Utils.readFile();
        return teamsNames.stream().map(Team::new).toList();
    }



    private List<Match> matchTeams() {
        return this.teams.stream()
                .flatMap(homeTeam ->
                        this.teams.stream()
                                .filter(awayTeam -> awayTeam.isIdBigger(homeTeam))
                                .map(awayTeam -> new Match(homeTeam, awayTeam)))
                .collect(Collectors.toList());
    }
    public void outcome() {
        Scanner scanner = new Scanner(System.in);
        boolean endMenu = false;

        while (!endMenu) {
            int userChoice = Utils.getInputInRange(scanner, 1, 6);

            switch (userChoice) {
                case 1 -> {
                    System.out.println("Enter the ID of the team you want:");
                    System.out.println(findMatchesByTeam(getUserChoice()));
                }
                case 2 -> {
                    System.out.println("Enter the number of top scoring teams you want:");
                    System.out.println(findTopScoringTeams(getUserChoice()));
                }
                case 3 -> {
                    System.out.println("Enter the minimum number of goals you want:");
                    System.out.println(findPlayersWithAtLeastNGoals(getUserChoice()));
                }
                case 4 -> {
                    System.out.println("Enter the position of the team you want:");
                    System.out.println(getTeamByPosition(getUserChoice()));
                }
                case 5 -> {
                    System.out.println("Enter the position of the top scorers you want:");
                    System.out.println(getTopScorers(getUserChoice()));
                }
                case 6 -> {
                    endMenu = true;
                }
            }
        }
    }

    private int getUserChoice(){
        Scanner scanner = new Scanner(System.in);
        int userChoice;
        try{
             userChoice = scanner.nextInt();
        }catch (Exception e){
            System.out.println("Invalid input. Please enter a valid number.");
            scanner.nextLine();
            return getUserChoice();
        }
        return userChoice;
    }
}
