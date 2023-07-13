package mechanics;
import components.Enemy;
import components.MunchMan;
import components.Stage;
import fundamentals.Coordinates;
import fundamentals.mechanic.MechanicScheduler;

public class EnemyHuntBehavior extends EnemyBehaviorBase
{
    private double update_millis = 100;
    private double initial_millis = 0;
    public EnemyHuntBehavior(Stage stage, Enemy enemy, MunchMan munch_man)
    {
        super(stage, enemy, munch_man);
        //setExecutionalPeriodicDelay(100);
    }    

    @Override
    public void initializeBehavior()
    {
        
    }

    @Override 
    public void executeBehavior()
    {
        Coordinates player = getMunchManStageCoords();
        setEnemyTarget(player.getX(), player.getY());
    }

    @Override
    public void endBehavior(boolean interrupted) 
    {

    }

    @Override
    public boolean isBehaviorFinished() 
    {
        return false; 
    }
}
