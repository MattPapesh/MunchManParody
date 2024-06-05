package org.hid4java.mechanics.behavior.lowerlevel.simplebehaviors;

import org.hid4java.components.Enemy;
import org.hid4java.components.MunchMan;
import org.hid4java.components.Stage;
import org.hid4java.fundamentals.Coordinates;
import org.hid4java.mechanics.movement.EntityMovement;

public class EnemyFlankHuntBehavior extends EnemyHuntBehavior
{
    private Coordinates enemy_coords = new Coordinates(0, 0, 0);
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
        enemy_coords = getComputedHuntEnemyStageCoords();
        setEnemyTarget(route_completion_pct, -1.0, enemy_coords.getX(), enemy_coords.getY(), getMunchManStageCoords());
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
