package mechanics;
import fundamentals.mechanic.MechanicBase;
import components.Stage;
import components.MunchMan;
import components.Enemy;
import fundamentals.Coordinates;

public class EnemyBehaviorBase extends MechanicBase implements EnemyBehaviorInterface
{
    private Stage stage = null; 
    private Enemy enemy = null;
    private MunchMan munch_man = null;
    private EntityMovement enemy_movement = null;
    private Coordinates enemy_target = new Coordinates(0, 0, 0);
    private EnemyGoNearTarget enemy_targeting = null;
    private boolean scheduled_enemy_targeting = false; 

    @Override public void initializeBehavior() {}
    @Override public void executeBehavior() {}
    @Override public boolean isBehaviorFinished() {return false;}
    @Override public void endBehavior(boolean interrupted) {}

    public EnemyBehaviorBase(Stage stage,  Enemy enemy, MunchMan munch_man) 
    {   
        this.stage = stage;
        this.enemy = enemy;
        this.munch_man = munch_man;
        enemy_movement = new EntityMovement(stage, enemy);

        addRequirements(stage, enemy, munch_man);
    }
 
    public void setEnemyTarget(double completion_pct, int target_stage_x, int target_stage_y)
    {
        if(enemy_targeting != null && enemy_targeting.isScheduled())
        {
            enemy_targeting.cancel();
        }

        enemy_targeting = new EnemyGoNearTarget(completion_pct, enemy_movement, stage, enemy, target_stage_x, target_stage_y);
        enemy_targeting.schedule();
        scheduled_enemy_targeting = true;
    }
    
    public double getEnemyRouteTerminatingPercentage()
    {
        return enemy_targeting.getCompletionPercentage();
    }

    public double getEnemyRouteCompletionPercentage()
    {
        return enemy_targeting.getTerminatingCompletionPercentage();
    }

    public boolean isEnemyRouteCompleted()
    {
        return !enemy_targeting.isScheduled() && scheduled_enemy_targeting;
        //return getEnemyRouteCompletionPercentage() >= getEnemyRouteTerminatingPercentage() 
        //|| (getEnemyRouteCompletionPercentage() == 0 && !enemy_go_near_target.isScheduled());
    }

    public Coordinates getEnemyTarget()
    {
        return new Coordinates(enemy_target.getX(), enemy_target.getY(), enemy_target.getDegrees());
    }

    public void setEnemyMovementSpeed(int speed) 
    {
        enemy.setSpeed(speed);
    }

    public int getEnemyMovementSpeed() 
    {
        return enemy.getSpeed();
    }

    public Coordinates getEnemyStageCoords()
    {
        return new Coordinates(enemy.getStageCoords().getX(), enemy.getStageCoords().getY(), enemy.getStageCoords().getDegrees());
    }

    public Coordinates getMunchManStageCoords()
    {
        return new Coordinates(munch_man.getStageCoords().getX(), munch_man.getStageCoords().getY(), munch_man.getStageCoords().getDegrees());
    }

    @Override 
    public void initialize()
    {
        if(enemy_movement != null && !enemy_movement.isScheduled())
        {
            enemy_movement.schedule();    
        }
        
        initializeBehavior();
    }

    @Override
    public void execute() 
    {
        executeBehavior();
        if(isEnemyRouteCompleted())
        {
            scheduled_enemy_targeting = false;
        }
    }

    @Override
    public void end(boolean interrupted) 
    {
        endBehavior(interrupted);
        if(enemy_movement != null && enemy_movement.isScheduled())
        {
            enemy_movement.cancel();
        }
    }

    @Override
    public boolean isFinished() 
    {
        return isBehaviorFinished(); 
    }
}