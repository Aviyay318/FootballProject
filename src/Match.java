import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Match {
    private int id;
    private Team homeTeam;
    private Team awayTeam;
    private List<Goal>goals;
    private boolean isPlay;
    private static int counter=0;

    public Match(Team homeTeam, Team awayTeam) {
        setMatchId();
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.goals = new ArrayList<>();
        this.isPlay = false;
    }
    private void setMatchId() {
        counter++;
        this.id=counter;
    }

    public void playGame() {
        isPlay = true;
    }

    public boolean isPlay() {
        return isPlay;
    }

    public List<Goal> getGoals() {
        return goals;
    }

    public void creatGoals(){
        Random random = new Random();
        IntStream.range(0,random.nextInt(0,10)).forEach(i->{
            Goal newGoal = new Goal(getRandomScorer());
            this.goals.add(newGoal);
        });

    }

    private Player getRandomScorer(){
        Random random = new Random();
        List<Player> players = new ArrayList<>(this.homeTeam.getPlayers());
        players.addAll(this.awayTeam.getPlayers());
        return players.get(random.nextInt(0,players.size()));

    }

    public Team getHomeTeam() {
        return homeTeam;
    }

    public Team getAwayTeam() {
        return awayTeam;
    }

    public boolean isSameTeamById(int id){
        boolean isSameTeam = false;
        if (this.homeTeam.checkId(id)||this.awayTeam.checkId(id)){
            isSameTeam=true;
        }
        return isSameTeam;
    }

    public String toString() {
        return "Match{ " +
                " id=" + id +
                " homeTeam= " + homeTeam +
                " awayTeam= " + awayTeam +
                " goals=" + this.goals +
                '}'+"\n";
    }

}
