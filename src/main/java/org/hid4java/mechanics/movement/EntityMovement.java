package org.hid4java.mechanics.movement;

import org.hid4java.fundamentals.mechanic.MechanicBase;
import org.hid4java.fundamentals.Constants;
import org.hid4java.fundamentals.Coordinates;
import org.hid4java.fundamentals.GameMath;
import org.hid4java.components.Enemy;
import org.hid4java.components.EntityBase;
import org.hid4java.components.Stage;

public class EntityMovement extends MechanicBase
{
    private EntityBase entity = null;
    private boolean began = false;  
    private boolean is_movement_obstructed = false; 
    private boolean enable_directional_animations = true;
    private boolean enable_spawn_point_access = false; 

    private Coordinates current_stage_coords = null;
    private Coordinates prev_stage_coords = null;
    private Coordinates current_gran_stage_coords = null;
    private Coordinates prev_gran_stage_coords = null;

    private int[][] stage_data = Constants.STAGE_CHARACTERISTICS.STAGE_DATA;
    private int collision_tolerance = 0; 

    private double current_delta_x = 0;
    private double current_delta_y = 0;
    private double prev_delta_x = 0;
    private double prev_delta_y = 0;

    // Decimal speeds in interval range [0.0, 1.0] that updates integer speed by one when these speeds
    // exceed their +1 upper limit; they then reset to zero. 
    private double dec_speed_x = 0, dec_speed_y = 0;

    public EntityMovement() {}

    /**
     * @see Note: The first four Animations used for the GenericEntity will be used as the entity changes direction;
     * the Animations with respect to direction of movement are described from index 0 to 3: right, left, down, up
     */
    public <GenericEntity extends EntityBase> EntityMovement(Stage stage, GenericEntity entity) 
    { 
        begin(stage, entity);
    }   

    public void enableSpawnPointAccess(boolean enable) 
    {
        enable_spawn_point_access = enable;
    }

    public void enableDirectionalAnimations(boolean enable)
    {
        enable_directional_animations = enable;
    }

    /**
     * Used only when extending this mechanic as a superclass; when doing so, this method must be called. 
     * 
     * @see Note: The first four Animations used for the GenericEntity will be used as the entity changes direction;
     * the Animations with respect to direction of movement are described from index 0 to 3: right, left, down, up
     */
    public <GenericEntity extends EntityBase> void begin(Stage stage, GenericEntity entity)
    {
        if(!began)
        {
            began = true;
            this.entity = (EntityBase)entity;

            current_stage_coords = new Coordinates(entity.getStageCoords().getX(), entity.getStageCoords().getY(), entity.getStageCoords().getDegrees());
            prev_stage_coords = new Coordinates(0, 0, 0); 
            current_gran_stage_coords = entity.convertToGranularStageCoords(current_stage_coords);
            prev_gran_stage_coords = new Coordinates(0, 0, 0);

            addRequirements(stage, this.entity);
        }
    }

    public void setTickVelocity(double delta_x, double delta_y)
    {
        prev_delta_x = current_delta_x;
        prev_delta_y = current_delta_y;
        current_delta_x = delta_x;
        current_delta_y = delta_y;
    }

    public boolean isMovementObstructed()
    {
        return is_movement_obstructed;
    }
     
    // Updates entity direction they are facing. (up/down/left/right)
    private void updateDirection()
    {
        try
        {
            if(current_delta_x > 0 && current_delta_y == 0)
            {
                entity.setStageCoords(entity.getStageCoords().getX(), entity.getStageCoords().getY(), 0);
                if(enable_directional_animations)
                    entity.setAnimation(entity.getAnimation(0).getName());
            }
            else if(current_delta_x < 0 && current_delta_y == 0)
            {
                entity.setStageCoords(entity.getStageCoords().getX(), entity.getStageCoords().getY(), 180);
                if(enable_directional_animations)
                    entity.setAnimation(entity.getAnimation(1).getName());
            }
            else if(current_delta_x == 0 && current_delta_y > 0)
            {
                entity.setStageCoords(entity.getStageCoords().getX(), entity.getStageCoords().getY(), 270);
                if(enable_directional_animations)
                    entity.setAnimation(entity.getAnimation(2).getName());
            }
            else if(current_delta_x == 0 && current_delta_y < 0)
            {
                entity.setStageCoords(entity.getStageCoords().getX(), entity.getStageCoords().getY(), 90);
                if(enable_directional_animations)
                    entity.setAnimation(entity.getAnimation(3).getName());
            }
        }
        catch(NullPointerException e) {}
    }

    public void reset(int stage_x, int stage_y, int degrees) 
    {
        entity.setStageCoords(stage_x, stage_y, degrees);
        setTickVelocity(0, 0);
        current_gran_stage_coords = entity.getGranularStageCoords();
    }

    private void updateCoords() 
    {
        entity.setCoordinates(current_gran_stage_coords.getX() + entity.getDisplacementCoords().getX(), 
        current_gran_stage_coords.getY() + entity.getDisplacementCoords().getY(), entity.getCoordinates().getDegrees());

        entity.setGranularStageCoords(current_gran_stage_coords.getX(), current_gran_stage_coords.getY(), current_gran_stage_coords.getDegrees());
        entity.setStageCoords(current_stage_coords.getX(), current_stage_coords.getY(), current_stage_coords.getDegrees());
    }

    private void collisionalMovement()
    {  
        is_movement_obstructed = false;
        prev_gran_stage_coords.setCoordinates(current_gran_stage_coords.getX(), current_gran_stage_coords.getY(), current_gran_stage_coords.getDegrees());
        prev_stage_coords.setCoordinates(current_stage_coords.getX(), current_stage_coords.getY(), current_stage_coords.getDegrees());
        
        // Whole-number speed
        int speed_x = (int)current_delta_x;
        int speed_y = (int)current_delta_y;
        // Difference get only decimal part of speed. (i.e. 0.XXX)
        dec_speed_x += current_delta_x - speed_x; 
        dec_speed_y += current_delta_y - speed_y;
        
        if(Math.abs(dec_speed_x) >= 1.0) {
            int dx = (int)(dec_speed_x / Math.abs(dec_speed_x));
            speed_x += dx;
            dec_speed_x -= dx;
        }
        if(Math.abs(dec_speed_y) >= 1.0) {
            int dy = (int)(dec_speed_y / Math.abs(dec_speed_y));
            speed_y += dy;
            dec_speed_y -= dy;
        }

        current_gran_stage_coords.setCoordinates(current_gran_stage_coords.getX() + speed_x, current_gran_stage_coords.getY() + speed_y, current_gran_stage_coords.getDegrees());
        current_stage_coords = entity.convertToStageCoords(current_gran_stage_coords);

        int horizontal_collision_diff = current_gran_stage_coords.getX() - entity.convertToGranularStageCoords(current_stage_coords).getX();
        int vertical_collision_diff = current_gran_stage_coords.getY() - entity.convertToGranularStageCoords(current_stage_coords).getY();

        try
        {
            // Vertical to horizontal turn:
            if((Math.abs(prev_delta_x) != Math.abs(current_delta_x) || Math.abs(prev_delta_y) != Math.abs(current_delta_y))
            && current_delta_x == 0 && current_delta_y != 0 
            && (((stage_data[current_stage_coords.getY() - 1][current_stage_coords.getX()] == 1 
            || (enable_spawn_point_access && stage_data[current_stage_coords.getY() - 1][current_stage_coords.getX()] == 2)) 
            && current_delta_y < 0)
            || ((stage_data[current_stage_coords.getY() + 1][current_stage_coords.getX()] == 1 
            || (enable_spawn_point_access && stage_data[current_stage_coords.getY() + 1][current_stage_coords.getX()] == 2)) 
            && current_delta_y > 0)))
            {
                setTickVelocity(current_delta_x, current_delta_y);
                current_gran_stage_coords.setCoordinates(entity.convertToGranularStageCoords(current_stage_coords).getX(), 
                current_gran_stage_coords.getY(), current_gran_stage_coords.getDegrees());
            }// Horizontal to vertical turn:
            else if((Math.abs(prev_delta_x) != Math.abs(current_delta_x) || Math.abs(prev_delta_y) != Math.abs(current_delta_y))
            && current_delta_x != 0 && current_delta_y == 0
            && (((stage_data[current_stage_coords.getY()][current_stage_coords.getX() - 1] == 1
            || (enable_spawn_point_access && stage_data[current_stage_coords.getY()][current_stage_coords.getX() - 1] == 2)) 
            && current_delta_x < 0)
            || ((stage_data[current_stage_coords.getY()][current_stage_coords.getX() + 1] == 1
            || (enable_spawn_point_access && stage_data[current_stage_coords.getY()][current_stage_coords.getX() + 1] == 2)) 
            && current_delta_x > 0)))
            {
                setTickVelocity(current_delta_x, current_delta_y);
                current_gran_stage_coords.setCoordinates(current_gran_stage_coords.getX(),  
                entity.convertToGranularStageCoords(current_stage_coords).getY(), current_gran_stage_coords.getDegrees());
            }
            else if((Math.abs(prev_delta_x) != Math.abs(current_delta_x) || Math.abs(prev_delta_y) != Math.abs(current_delta_y))
            && !(current_delta_x == 0 && current_delta_y == 0))
            {
                setTickVelocity(prev_delta_x, prev_delta_y);
            }

            // Horizontal movement: 
            if(current_stage_coords.getY() == prev_stage_coords.getY() 
            && ((horizontal_collision_diff < collision_tolerance && stage_data[current_stage_coords.getY()][current_stage_coords.getX() - 1] == 0) 
            || (-horizontal_collision_diff < collision_tolerance && stage_data[current_stage_coords.getY()][current_stage_coords.getX() + 1] == 0)))
            {
                current_gran_stage_coords.setCoordinates(prev_gran_stage_coords.getX(), prev_gran_stage_coords.getY(), current_gran_stage_coords.getDegrees());
                current_stage_coords.setCoordinates(prev_stage_coords.getX(), prev_stage_coords.getY(), current_stage_coords.getDegrees());
                is_movement_obstructed = true;
            }// Vertical movement: 
            else if(current_stage_coords.getX() == prev_stage_coords.getX()
            && ((vertical_collision_diff < collision_tolerance && stage_data[current_stage_coords.getY() - 1][current_stage_coords.getX()] == 0) 
            || (-vertical_collision_diff < collision_tolerance && stage_data[current_stage_coords.getY() + 1][current_stage_coords.getX()] == 0)))
            {  
                current_gran_stage_coords.setCoordinates(prev_gran_stage_coords.getX(), prev_gran_stage_coords.getY(), current_gran_stage_coords.getDegrees());
                current_stage_coords.setCoordinates(prev_stage_coords.getX(), prev_stage_coords.getY(), current_stage_coords.getDegrees());
                is_movement_obstructed = true; 
            }
        } 
        catch(ArrayIndexOutOfBoundsException e) {}

        // Teleporting from left/right side of the stage to the other: 
        // Moving left across stage:
        if(current_stage_coords.getX() < 0)
        {
            current_stage_coords.setCoordinates(stage_data[0].length - 1, current_stage_coords.getY(), current_stage_coords.getDegrees());
            current_gran_stage_coords = entity.convertToGranularStageCoords(current_stage_coords);
            
        } // Moving right across stage:
        else if(current_stage_coords.getX() > stage_data[0].length - 1)
        {   
            current_stage_coords.setCoordinates(0, current_stage_coords.getY(), current_stage_coords.getDegrees());
            current_gran_stage_coords = entity.convertToGranularStageCoords(current_stage_coords);
        }
    }

    @Override
    public void execute() 
    {   
        collisionalMovement();
        updateCoords();
        updateDirection();
    }

    @Override
    public boolean isFinished() 
    {
        return false;
    }
}
