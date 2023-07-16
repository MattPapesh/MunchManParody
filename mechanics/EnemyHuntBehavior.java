package mechanics;
import components.Enemy;
import components.MunchMan;
import components.Stage;
import fundamentals.Coordinates;

public class EnemyHuntBehavior extends EnemyBehaviorBase
{
    public EnemyHuntBehavior(Stage stage, Enemy enemy, MunchMan munch_man)
    {
        super(stage, enemy, munch_man);
    }    

    private double pct = 1.0;

    @Override
    public void initializeBehavior()
    {
        setEnemyTarget(pct, getMunchManStageCoords().getX(), getMunchManStageCoords().getY());
    }

    @Override 
    public void executeBehavior() {
        if(getEnemyTravelCompletionPercentage() >= pct)
        {
            setEnemyTarget(pct, getMunchManStageCoords().getX(), getMunchManStageCoords().getY());
        }
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
