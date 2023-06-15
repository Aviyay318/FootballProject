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
        Collections.shuffle(this.matches);
        this.leagueTable = new HashMap<>();
        this.teams.forEach(i->{
            this.leagueTable.put(i,0);
        });

    }


    public void startGame(){
        IntStream.range(0,45).forEach(i->{
            System.out.println(this.matches.get(i).getAwayTeam().getName()+"-------VS-------"+this.matches.get(i).getHomeTeam().getName());
              countDown();
                this.matches.get(i).creatGoals();
            Map<Team,Integer> teamGoals = printResult( this.matches.get(i));
            updateLeagueTable(this.matches.get(i),teamGoals);
            this.leagueTable.forEach((team,score)->{
                System.out.println(team + ": " + score);

            });
                if ((i%5==0&&i!=0)||i==44){
                    System.out.println(Constants.MENU);
               outcome();
            }
        });
    }

    private void updateLeagueTable(Match match, Map<Team, Integer> teamGoals) {
        if (teamGoals.get(match.getHomeTeam())>teamGoals.get(match.getAwayTeam())){
            int temp = this.leagueTable.get(match.getHomeTeam())+3;
            this.leagueTable.put(match.getHomeTeam(),temp);
        } else if (teamGoals.get(match.getHomeTeam())<teamGoals.get(match.getAwayTeam())) {
            int temp = this.leagueTable.get(match.getAwayTeam())+3;
            this.leagueTable.put(match.getAwayTeam(),temp);
        }else {
            int temp = this.leagueTable.get(match.getHomeTeam())+1;
            this.leagueTable.put(match.getHomeTeam(),temp);
             temp = this.leagueTable.get(match.getAwayTeam())+1;
            this.leagueTable.put(match.getAwayTeam(),temp);
        }
        this.leagueTable = this.leagueTable.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));


    }

    private Map<Team,Integer> printResult(Match match){
    Map<Player,Long> playersGoals = match.getGoals().stream().collect(Collectors.groupingBy(Goal::getScorer,counting()));
    Map<Team,Integer> teamGoals = new HashMap<>();
    teamGoals.put(match.getHomeTeam(),0);
    teamGoals.put(match.getAwayTeam(),0);
    playersGoals.forEach((player,goals)->{
        this.teams.forEach(j->{
         if (j.getPlayers().contains(player)) {
             Integer temp = teamGoals.get(j);
                 teamGoals.put(j,temp+goals.intValue());

         }
        });
    });
    System.out.println("Result");
    System.out.println(teamGoals.get(match.getHomeTeam())+":"+teamGoals.get(match.getAwayTeam()));
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
       return this.matches.stream().filter(match -> match.isSameTeamById(teamId)&&match.getGoals().size()!=0).toList();
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
        int userChoice;
        boolean endMenu =false;
        while (!endMenu){
            switch (Utils.getInputInRange(scanner,1,6)){
                case 1->{
                    System.out.println("enter an id of team you want");
                    userChoice = scanner.nextInt();
                    System.out.println(findMatchesByTeam(userChoice));
                }
                case 2->{
                    System.out.println("enter how much team you want: ");
                    userChoice = scanner.nextInt();
                    System.out.println(findTopScoringTeams(userChoice));
                }
                case 3->{
                    System.out.println("enter how much goals you want: ");
                    userChoice = scanner.nextInt();
                    System.out.println(findPlayersWithAtLeastNGoals(userChoice));
                }
                case 4->{
                    System.out.println("enter position of team you want: ");
                    userChoice = scanner.nextInt();
                    System.out.println(getTeamByPosition(userChoice));
                }
                case 5->{
                    System.out.println("enter position of scorers you want: ");
                    userChoice = scanner.nextInt();
                    System.out.println(getTopScorers(userChoice));
                }
                case 6->{
                    endMenu=true;
                }
            }
        }

}}
