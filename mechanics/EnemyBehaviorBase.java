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
    private EnemyGoNearTarget enemy_go_near_target = null;
    private EntityMovement enemy_movement = null;
    private Coordinates enemy_target = new Coordinates(0, 0, 0);

    @Override public void initializeBehavior() {}
    @Override public void executeBehavior() {}
    @Override public boolean isBehaviorFinished() {return false;}
    @Override public void endBehavior(boolean interrupted) {}

    public boolean isEnemyTravelInitialized()
    {
        if(enemy_go_near_target != null)
        {
            return enemy_go_near_target.isInitialized();
        }

        return false;
    }

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
        if(enemy_go_near_target != null)
        {
            enemy_go_near_target.cancel();
        }

        enemy_target.setCoordinates(target_stage_x, target_stage_y, 0);
        enemy_go_near_target = new EnemyGoNearTarget(completion_pct, enemy_movement, stage, enemy, target_stage_x, target_stage_y);
    }
    
    public double getEnemyTravelTerminatingPercentage()
    {
        if(enemy_go_near_target != null)
        {
            return enemy_go_near_target.getTerminatingCompletionPercentage();
        }

        return 0.0;
    }

    public double getEnemyTravelCompletionPercentage()
    {
        if(enemy_go_near_target != null)
        {
            return enemy_go_near_target.getCompletionPercentage();
        }

        return 0.0;
    }

    public Coordinates getEnemyTarget()
    {
        return new Coordinates(enemy_target.getX(), enemy_target.getY(), enemy_target.getDegrees());
    }

    public void setEnemySpeed(int speed) 
    {
        enemy.setSpeed(speed);
    }

    public double getEnemySpeed() 
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
        if(enemy_go_near_target != null && !enemy_go_near_target.isScheduled())
        {
            enemy_go_near_target.schedule();
        }

        executeBehavior();
    }

    @Override
    public void end(boolean interrupted) 
    {
        endBehavior(interrupted);
    }

    @Override
    public boolean isFinished() 
    {
        return isBehaviorFinished(); 
    }
}