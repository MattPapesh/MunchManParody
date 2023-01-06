package mechanics;

import components.Enemy;
import fundamentals.Coordinates;
import fundamentals.mechanic.MechanicBase;

public class EnemyMovement extends MechanicBase
{
    private Enemy enemy = null;
    private EntityMovement entity_movement = null; 
    private Coordinates initial_stage_coords = null;
    private Coordinates initial_gran_stage_coords = null; 
    private int delta_stage_x = 0;
    private int delta_stage_y = 0;

    public EnemyMovement(EntityMovement entity_movement, Enemy enemy, int delta_stage_x, int delta_stage_y)
    {
        this.enemy = enemy;
        this.entity_movement = entity_movement;
        addRequirements(enemy);

        if((delta_stage_x != 0 && delta_stage_y == 0) || (delta_stage_x == 0 && delta_stage_y != 0))
        {
            this.delta_stage_x = delta_stage_x;
            this.delta_stage_y = delta_stage_y;
        } 
    }

    @Override
    public void initialize()
    {
        initial_stage_coords = new Coordinates(enemy.getStageCoords().getX(), enemy.getStageCoords().getY(), 0);
        initial_gran_stage_coords = enemy.convertToGranularStageCoords(initial_stage_coords);

        if(delta_stage_x != 0 && delta_stage_y == 0)
        {   
            entity_movement.setTickVelocity((delta_stage_x / Math.abs(delta_stage_x)) * enemy.getSpeed(), 0);
        }
        else if(delta_stage_x == 0 && delta_stage_y != 0)
        {   
            entity_movement.setTickVelocity(0, (delta_stage_y / Math.abs(delta_stage_y)) * enemy.getSpeed());
        }
    }

    @Override
    public void execute() 
    {
        
    }   

    @Override
    public void end(boolean interrupted)
    {
        entity_movement.setTickVelocity(0, 0);
        initial_stage_coords = null;
        initial_gran_stage_coords = null; 
    }

    @Override 
    public boolean isFinished()
    { 
        Coordinates delta_gran_stage = enemy.convertToGranularStageCoords(new Coordinates(delta_stage_x, delta_stage_y, 0));
        
        return initial_stage_coords != null && initial_gran_stage_coords != null 
        && ((delta_stage_x != 0 && delta_stage_y == 0 
        && Math.abs(enemy.getGranularStageCoords().getX() - initial_gran_stage_coords.getX()) >= Math.abs(delta_gran_stage.getX()))
        || (delta_stage_x == 0 && delta_stage_y != 0 
        && Math.abs(enemy.getGranularStageCoords().getY() - initial_gran_stage_coords.getY()) >= Math.abs(delta_gran_stage.getY())) 
        || entity_movement.isMovementObstructed());
    }
}
