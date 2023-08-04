package mechanics.behavior.lowerlevel;

import components.Enemy;
import components.MunchMan;
import components.Stage;
import fundamentals.Coordinates;
import mechanics.movement.EntityMovement;

public class EnemyFlankHuntBehavior extends EnemyHuntBehavior
{
    private double route_completion_pct = 0.2;

    public EnemyFlankHuntBehavior(EntityMovement enemy_movement, Stage stage, Enemy enemy, MunchMan munch_man,
    int direct_hunt_distance_units, int flank_radius_units, int flank_degrees)
    {
        super(enemy_movement, stage, enemy, munch_man,
        direct_hunt_distance_units, flank_radius_units, flank_degrees);
    }

    public EnemyFlankHuntBehavior(EntityMovement enemy_movement, Stage stage, Enemy enemy, MunchMan munch_man, flank_data flank)
    {
        super(enemy_movement, stage, enemy, munch_man, flank);
    }

    private void computeFlankHuntBehavior()
    {
        Coordinates computed_stage_coords = getComputedHuntEnemyStageCoords();
        setEnemyTarget(route_completion_pct, -1.0, computed_stage_coords.getX(), computed_stage_coords.getY(), getMunchManStageCoords());
    }

    @Override
    public void initializeBehavior()
    {
        computeFlankHuntBehavior();
    }

    @Override
    public void executeBehavior()
    {
        if(isEnemyRouteCompleted())
        {
            computeFlankHuntBehavior();
        }
    }

    @Override
    public void endBehavior(boolean interrupted) 
    {
        setEnemyTarget(0, getEnemyStageCoords().getX(), getEnemyStageCoords().getY());
    }

    @Override
    public boolean isBehaviorFinished()
    {
        return false;
    }
}
