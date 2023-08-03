package mechanics.behavior;

import components.Enemy;
import components.MunchMan;
import components.Stage;
import fundamentals.Coordinates;
import fundamentals.GameMath;
import mechanics.bases.EnemyBehaviorBase;
import mechanics.movement.EntityMovement;

public class EnemyHuntBehavior extends EnemyBehaviorBase
{
    private flank_data flank = null;
    private int[][] stage_data = null;
    private double route_completion_pct = 0.2;

    public interface flank_data 
    {
        int getDirectHuntDistanceUnits();
        int getFlankDegrees();
        int getFlankRadius();
    }

    public EnemyHuntBehavior(EntityMovement enemy_movement, Stage stage, Enemy enemy, MunchMan munch_man,
    int direct_hunt_distance_units, int flank_radius_units, int flank_degrees)
    {
        super(enemy_movement, stage, enemy, munch_man);
        stage_data = stage.getStageData().clone();
        flank = new flank_data() 
        {
            @Override public int getDirectHuntDistanceUnits() {return direct_hunt_distance_units;}
            @Override public int getFlankDegrees() {return flank_degrees;}
            @Override public int getFlankRadius() {return flank_radius_units;}
        };
    }

    public EnemyHuntBehavior(EntityMovement enemy_movement, Stage stage, Enemy enemy, MunchMan munch_man,
    flank_data flank)
    {
        super(enemy_movement, stage, enemy, munch_man);
        stage_data = stage.getStageData().clone();
        this.flank = flank;
    }

    private void computeFlankHuntBehavior()
    {
        Coordinates delta_stage_coords = GameMath.getRadialDisplacement(flank.getFlankRadius(), getMunchManStageCoords().getDegrees() + flank.getFlankDegrees());
        int stage_x = Math.max(Math.min(getMunchManStageCoords().getX() + delta_stage_coords.getX(), stage_data[0].length - 1), 0);
        int stage_y = Math.max(Math.min(getMunchManStageCoords().getY() - delta_stage_coords.getY(), stage_data.length - 1), 0);
        double calc_direct_hunt_distance_units = Math.pow(Math.pow(getEnemyStageCoords().getX() - getMunchManStageCoords().getX(), 2) + 
        Math.pow(getEnemyStageCoords().getY() - getMunchManStageCoords().getY(), 2), 0.5);
        stage_x = (calc_direct_hunt_distance_units > flank.getDirectHuntDistanceUnits()) ? stage_x : getMunchManStageCoords().getX();
        stage_y = (calc_direct_hunt_distance_units > flank.getDirectHuntDistanceUnits()) ? stage_y : getMunchManStageCoords().getY();
        setEnemyTarget(route_completion_pct, stage_x, stage_y);
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

    }

    @Override
    public boolean isBehaviorFinished()
    {
        return false;
    }
}
