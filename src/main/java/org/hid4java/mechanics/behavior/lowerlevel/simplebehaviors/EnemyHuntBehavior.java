package org.hid4java.mechanics.behavior.lowerlevel.simplebehaviors;

import org.hid4java.components.Enemy;
import org.hid4java.components.MunchMan;
import org.hid4java.components.Stage;
import org.hid4java.fundamentals.Coordinates;
import org.hid4java.fundamentals.GameMath;
import org.hid4java.mechanics.behaviorbases.EnemyBehaviorBase;
import org.hid4java.mechanics.movement.EntityMovement;

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

    protected Coordinates getComputedHuntEnemyStageCoords()
    {
        Coordinates delta_coords = GameMath.getRadialDisplacement(flank.getFlankRadius(), getMunchManStageCoords().getDegrees() + flank.getFlankDegrees());
        int x = Math.max(Math.min(getMunchManStageCoords().getX() + delta_coords.getX(), stage_data[0].length - 1), 0);
        int y = Math.max(Math.min(getMunchManStageCoords().getY() - delta_coords.getY(), stage_data.length - 1), 0);
        double calc_direct_hunt_distance_units = Math.pow(Math.pow(getEnemyStageCoords().getX() - getMunchManStageCoords().getX(), 2) + 
        Math.pow(getEnemyStageCoords().getY() - getMunchManStageCoords().getY(), 2), 0.5);
        x = (calc_direct_hunt_distance_units > flank.getDirectHuntDistanceUnits()) ? x : getMunchManStageCoords().getX();
        y = (calc_direct_hunt_distance_units > flank.getDirectHuntDistanceUnits()) ? y : getMunchManStageCoords().getY();
        
        return new Coordinates(x, y, 0);
    }

    private void computeHuntBehavior()
    {
        Coordinates enemy_coords = getComputedHuntEnemyStageCoords();
        setEnemyTarget(route_completion_pct, enemy_coords.getX(), enemy_coords.getY());
    }

    @Override
    public void initializeBehavior()
    {
        computeHuntBehavior();
    }

    @Override
    public void executeBehavior()
    {
        if(isEnemyRouteCompleted())
        {
            computeHuntBehavior();
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
