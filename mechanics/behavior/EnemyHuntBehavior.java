package mechanics.behavior;
import components.Enemy;
import components.MunchMan;
import components.Stage;
import mechanics.bases.EnemyBehaviorBase;

public class EnemyHuntBehavior extends EnemyBehaviorBase
{
    private double route_completion_pct = 0.2;
    
    public EnemyHuntBehavior(Stage stage, Enemy enemy, MunchMan munch_man)
    {
        super(stage, enemy, munch_man);
    }    

    @Override
    public void initializeBehavior()
    {
        setEnemyTarget(route_completion_pct, getMunchManStageCoords().getX(), getMunchManStageCoords().getY());
    }

    @Override 
    public void executeBehavior() {
        if(isEnemyRouteCompleted())
        {
            setEnemyTarget(route_completion_pct, getMunchManStageCoords().getX(), getMunchManStageCoords().getY());
        }
    }

    @Override
    public void endBehavior(boolean interrupted) 
    {
        setEnemyTarget(0.0, getEnemyStageCoords().getX(), getEnemyStageCoords().getY());
    }

    @Override
    public boolean isBehaviorFinished() 
    {
        return false; 
    }
}
