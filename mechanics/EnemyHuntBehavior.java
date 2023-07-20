package mechanics;
import components.Enemy;
import components.MunchMan;
import components.Stage;

public class EnemyHuntBehavior extends EnemyBehaviorBase
{
    public EnemyHuntBehavior(Stage stage, Enemy enemy, MunchMan munch_man)
    {
        super(stage, enemy, munch_man);
    }    

    private double pct = 0.2;

    @Override
    public void initializeBehavior()
    {
        setEnemyTarget(pct, getMunchManStageCoords().getX(), getMunchManStageCoords().getY());
    }

    @Override 
    public void executeBehavior() {
        if(isEnemyRouteCompleted())
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
