import java.util.Random;

public class Goal {
    private int id;
    private int minute;
    private Player scorer;
    private static int counter=0;


    public Goal(Player scorer){
        setGoalId();
        setGoalMinute();
        this.scorer = scorer;
    }


    private void setGoalId() {
        counter++;
        this.id=counter;
    }

    public Player getScorer() {
        return scorer;
    }

    public int getId() {
        return id;
    }

    private void setGoalMinute() {
        Random random = new Random();
        this.minute = random.nextInt(91);
    }
    public String toString() {
        return "Goal{" +
                "id=" + id +
                ", minute=" + minute + ":90"+
                 "scorer name: " +this.scorer.getId()+
                '}';
    }
}
