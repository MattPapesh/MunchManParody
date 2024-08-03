package org.hid4java.mechanics.movement;

import org.hid4java.components.Enemy;
import org.hid4java.fundamentals.Coordinates;
import org.hid4java.fundamentals.mechanic.MechanicBase;

public class EnemyPathFollowing extends MechanicBase
{
    private Enemy enemy = null;
    private EntityMovement enemy_movement = null; 
    private Coordinates initial_stage_coords = null;
    private Coordinates initial_gran_stage_coords = null; 
    private int delta_stage_x = 0;
    private int delta_stage_y = 0;

    public EnemyPathFollowing(EntityMovement enemy_movement, Enemy enemy, int delta_stage_x, int delta_stage_y)
    {
        this.enemy = enemy;
        this.enemy_movement = enemy_movement;
        this.delta_stage_x = delta_stage_x;
        this.delta_stage_y = delta_stage_y;
        addRequirements(enemy);
    }

    @Override
    public void initialize()
    {
        initial_stage_coords = new Coordinates(enemy.getStageCoords().getX(), enemy.getStageCoords().getY(), enemy.getStageCoords().getDegrees());
        initial_gran_stage_coords = enemy.convertToGranularStageCoords(initial_stage_coords);

        if(delta_stage_x != 0 && delta_stage_y == 0)
        {   
            enemy_movement.setTickVelocity((((double)delta_stage_x / (double)Math.abs(delta_stage_x)) * enemy.getSpeed()), 0);
        }
        else if(delta_stage_x == 0 && delta_stage_y != 0)
        {   
            enemy_movement.setTickVelocity(0, (((double)delta_stage_y / (double)Math.abs(delta_stage_y)) * enemy.getSpeed()));
        }
    }

    @Override
    public void end(boolean interrupted)
    {
        enemy_movement.setTickVelocity(0, 0);
        initial_stage_coords = null;
        initial_gran_stage_coords = null; 
    }

    @Override 
    public boolean isFinished()
    { 
        Coordinates delta_gran_stage = enemy.convertToGranularStageCoords(
        new Coordinates(delta_stage_x, delta_stage_y, enemy.getStageCoords().getDegrees()));
        
        return initial_stage_coords != null && initial_gran_stage_coords != null 
        && ((delta_stage_x != 0 && delta_stage_y == 0 
        && Math.abs(enemy.getGranularStageCoords().getX() - initial_gran_stage_coords.getX()) >= Math.abs(delta_gran_stage.getX()))
        || (delta_stage_x == 0 && delta_stage_y != 0 
        && Math.abs(enemy.getGranularStageCoords().getY() - initial_gran_stage_coords.getY()) >= Math.abs(delta_gran_stage.getY())) 
        || enemy_movement.isMovementObstructed());
    }
}
