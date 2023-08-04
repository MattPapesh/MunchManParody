package mechanics.behaviorbases;
import fundamentals.mechanic.MechanicBase;
import fundamentals.mechanic.MechanicScheduler;
import mechanics.movement.EnemyGoNearTarget;
import mechanics.movement.EntityMovement;
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
    private Coordinates enemy_target = new Coordinates(1, 1, 0);
    private EnemyGoNearTarget enemy_targeting = null;
    private boolean scheduled_enemy_targeting = false; 

    private int max_scheduled_millis = -1;
    private int initial_scheduled_millis = 0;
    private int self_scheduling_cooldown_millis = 0;
    private int final_scheduled_millis = 0;

    @Override public void initializeBehavior() {}
    @Override public void executeBehavior() {}
    @Override public boolean isBehaviorFinished() {return false;}
    @Override public void endBehavior(boolean interrupted) {}

    public EnemyBehaviorBase(EntityMovement enemy_movement, Stage stage,  Enemy enemy, MunchMan munch_man) 
    {   
        this.stage = stage;
        this.enemy = enemy;
        this.munch_man = munch_man;
        this.enemy_movement = enemy_movement;

        addRequirements(stage, enemy, munch_man);
    }
 
    public void setEnemyTarget(double completion_pct, int target_stage_x, int target_stage_y)
    {
        if(enemy_targeting != null && enemy_targeting.isScheduled())
        {
            enemy_targeting.cancel();
        }

        enemy_targeting = new EnemyGoNearTarget(enemy_movement, stage, enemy, completion_pct, target_stage_x, target_stage_y);
        enemy_targeting.schedule();
        scheduled_enemy_targeting = true;
    }

    public void setEnemyTarget(double completion_pct, double turn_around_pct, int target_stage_x, int target_stage_y)
    {
        if(enemy_targeting != null && enemy_targeting.isScheduled())
        {
            enemy_targeting.cancel();
        }

        enemy_targeting = new EnemyGoNearTarget(enemy_movement, stage, enemy, completion_pct, turn_around_pct, target_stage_x, target_stage_y);
        enemy_targeting.schedule();
        scheduled_enemy_targeting = true;
    }

    public void setEnemyTarget(double completion_pct, int target_stage_x, int target_stage_y, Coordinates... avoiding_coords)
    {
        if(enemy_targeting != null && enemy_targeting.isScheduled())
        {
            enemy_targeting.cancel();
        }

        enemy_targeting = new EnemyGoNearTarget(enemy_movement, stage, enemy, completion_pct, target_stage_x, target_stage_y, avoiding_coords);
        enemy_targeting.schedule();
        scheduled_enemy_targeting = true;
    }

    public void setEnemyTarget(double completion_pct, double turn_around_pct, int target_stage_x, int target_stage_y, Coordinates... avoiding_coords)
    {
        if(enemy_targeting != null && enemy_targeting.isScheduled())
        {
            enemy_targeting.cancel();
        }

        enemy_targeting = new EnemyGoNearTarget(enemy_movement, stage, enemy, completion_pct, turn_around_pct, target_stage_x, target_stage_y, avoiding_coords);
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
    }

    public Coordinates getNearStageCoords(int stage_x, int stage_y)
    {
        if(enemy_targeting != null)
        {
            return enemy_targeting.getNearTargetStageCoords(stage_x, stage_y);
        }

        return null;
    }

    public boolean getCurrentEnemyRouteTurnAroundStatus()
    {
        if(enemy_targeting != null)
        {
            return enemy_targeting.getTurnAroundStatus();
        }

        return false;
    }

    public int getRouteLength(int stage_x, int stage_y, boolean turn_around)
    {
        if(enemy_targeting != null)
        {
            Coordinates near_stage_coords = getNearStageCoords(stage_x, stage_y);
            return enemy_targeting.getRouteLength(near_stage_coords.getX(), near_stage_coords.getY());
        }

        return 0;
    }

    public int getCurrentRoutePathIndex()
    {
        if(enemy_targeting != null)
        {
            return enemy_targeting.getCurrentPathIndexScheduled();
        }

        return 0;
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

    public void setMaxScheduledMillis(int millis)
    {   
        max_scheduled_millis = (millis != -1) ? Math.max(millis, 0) : -1;
    }

    public int getMaxScheduledMillis()
    {
        return max_scheduled_millis;
    }

    public void setSchedulingCooldownMillis(int millis)
    {
        self_scheduling_cooldown_millis = Math.max(millis, 0);
    }

    public int getSchedulingCooldownMillis()
    {
        return self_scheduling_cooldown_millis;
    }

    @Override 
    public void initialize()
    {   
        if(Math.abs(final_scheduled_millis - MechanicScheduler.getElapsedMillis()) >= self_scheduling_cooldown_millis)
        {
            initial_scheduled_millis = MechanicScheduler.getElapsedMillis();
            initializeBehavior();
        }
    }

    @Override
    public void execute() 
    {
        if(Math.abs(final_scheduled_millis - MechanicScheduler.getElapsedMillis()) >= self_scheduling_cooldown_millis)
        {
            executeBehavior();
            if(isEnemyRouteCompleted())
            {
                scheduled_enemy_targeting = false;
            }
        }
    }

    @Override
    public void end(boolean interrupted) 
    {
        if(Math.abs(final_scheduled_millis - MechanicScheduler.getElapsedMillis()) >= self_scheduling_cooldown_millis)
        {
            endBehavior(interrupted);
            final_scheduled_millis = MechanicScheduler.getElapsedMillis();
        }
    }

    @Override
    public boolean isFinished() 
    {
        return (max_scheduled_millis != -1
        && Math.abs(MechanicScheduler.getElapsedMillis() - initial_scheduled_millis) >= max_scheduled_millis)
        || Math.abs(final_scheduled_millis - MechanicScheduler.getElapsedMillis()) < self_scheduling_cooldown_millis
        || isBehaviorFinished(); 
    }
}