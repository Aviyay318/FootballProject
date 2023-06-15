import java.util.List;
import java.util.stream.Stream;

public class Team {
    private int id;
    private String name;
    private List <Player> players;

    private int winningPoints;

    public Team(String team){
      String [] teamData = team.split(",");
      this.id = Integer.parseInt(teamData[Constants.ID_INDEX]);
      this.name = teamData[Constants.NAME_INDEX];
      this.players = createPlayers();
      this.winningPoints = 0;
    }
    public void addPoints(){
        this.winningPoints++;
    }
    public  List<Player> createPlayers() {
        return Stream.generate((Player::new)).limit(15).toList();
    }
    public boolean isIdBigger(Team team){
        boolean isIdBigger = false;
        if (this.id>team.id){
            isIdBigger = true;
        }
        return isIdBigger;
    }

    public String getName() {
        return name;
    }

    public boolean checkId(int id){
        boolean isIdentical = false;
        if (this.id==id){
            isIdentical = true;
        }
        return isIdentical;
    }

    public List<Player> getPlayers() {
        return players;
    }
    public String toString() {
        return  "Id: "+this.id+" "+"Team Name: " + name ;
    }
}
