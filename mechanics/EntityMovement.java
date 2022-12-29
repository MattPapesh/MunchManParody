package mechanics;

import fundamentals.mechanic.InstantMechanic;
import fundamentals.mechanic.MechanicBase;
import fundamentals.Constants;
import fundamentals.Coordinates;

import components.EntityBase;
import components.Stage;

public class EntityMovement extends MechanicBase
{
    private EntityBase entity = null;
    private boolean began = false;  
    private boolean is_superclass = true; 

    private Coordinates current_stage_coords = null;
    private Coordinates prev_stage_coords = null;
    private Coordinates current_gran_stage_coords = null;
    private Coordinates prev_gran_stage_coords = null;

    private int[][] stage_data;
    private int collision_tolerance = 0; 

    private int current_delta_x = 0;
    private int current_delta_y = 0;
    private int prev_delta_x = 0;
    private int prev_delta_y = 0;

    public EntityMovement() {}

    /**
     * @see Note: The first four Animations used for the GenericEntity will be used as the entity changes direction;
     * the Animations with respect to direction of movement are described from index 0 to 3: right, left, down, up
     * 
     */
    public <GenericEntity extends EntityBase> EntityMovement(Stage stage, GenericEntity entity) 
    { 
        is_superclass = false; 
        begin(stage, entity);
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
            this.stage_data = stage.getStageData();

            current_stage_coords = new Coordinates(entity.getStageCoords().getX(), entity.getStageCoords().getY(), 0);
            prev_stage_coords = new Coordinates(0, 0, 0); 
            current_gran_stage_coords = getGranularStageCoords(current_stage_coords);
            prev_gran_stage_coords = new Coordinates(0, 0, 0);

            setExecutionalPeriodicDelay(20);
            addRequirements(stage, this.entity);
        }

        if(is_superclass)
        {
            InstantMechanic indefinite_entity_movement = new InstantMechanic(()->
            {
                collisionalMovement();
                updateCoords();
                updateDirection(); 
            });

            indefinite_entity_movement.setExecutionalPeriodicDelay(20);
            indefinite_entity_movement.continuouslyLoopMechanic(true);
            indefinite_entity_movement.schedule();
        }
    }

    public void setTickVelocity(int delta_x, int delta_y)
    {
        prev_delta_x = current_delta_x;
        prev_delta_y = current_delta_y;
        current_delta_x = delta_x;
        current_delta_y = delta_y;
    }
     
    // Updates player direction they are facing. (up/down/left/right)
    private void updateDirection()
    {
        try
        {
            if(current_delta_x > 0 && current_delta_y == 0)
            {
                entity.setAnimation(entity.getAnimation(0).getName());
            }
            else if(current_delta_x < 0 && current_delta_y == 0)
            {
                entity.setAnimation(entity.getAnimation(1).getName());
            }
            else if(current_delta_x == 0 && current_delta_y > 0)
            {
                entity.setAnimation(entity.getAnimation(2).getName());
            }
            else if(current_delta_x == 0 && current_delta_y < 0)
            {
                entity.setAnimation(entity.getAnimation(3).getName());
            }
        }
        catch(NullPointerException e) {}
        
    }

    // Rounds to the nearest stage coordinate based on granular stage coordinates. 
    private Coordinates getApproximateStageCoords(Coordinates granular_stage_coords)
    {
        return new Coordinates((int)(Math.round((double)(granular_stage_coords.getX()) / (double)Constants.STAGE_CHARACTERISTICS.STAGE_COORD_SCALER)), 
        (int)(Math.round((double)(granular_stage_coords.getY()) / (double)Constants.STAGE_CHARACTERISTICS.STAGE_COORD_SCALER)), 0);
    }

    private Coordinates getGranularStageCoords(Coordinates stage_coords)
    {
        return new Coordinates(stage_coords.getX() * Constants.STAGE_CHARACTERISTICS.STAGE_COORD_SCALER, 
        stage_coords.getY() * Constants.STAGE_CHARACTERISTICS.STAGE_COORD_SCALER, 0);
    }

    private void updateCoords() 
    {
        entity.setCoordinates(current_gran_stage_coords.getX() + entity.getDisplacementCoords().getX(), 
        current_gran_stage_coords.getY() + entity.getDisplacementCoords().getY(), 0);
        entity.setGranularStageCoords(current_gran_stage_coords.getX(), current_gran_stage_coords.getY());
        entity.setStageCoords(current_stage_coords.getX(), current_stage_coords.getY());
    }

    private void collisionalMovement()
    {  
        prev_gran_stage_coords.setCoordinates(current_gran_stage_coords.getX(), current_gran_stage_coords.getY(), 0);
        prev_stage_coords.setCoordinates(current_stage_coords.getX(), current_stage_coords.getY(), 0);
        current_gran_stage_coords.setCoordinates(current_gran_stage_coords.getX() + current_delta_x, current_gran_stage_coords.getY() + current_delta_y, 0);
        current_stage_coords = getApproximateStageCoords(current_gran_stage_coords);

        int horizontal_collision_diff = current_gran_stage_coords.getX() - getGranularStageCoords(current_stage_coords).getX();
        int vertical_collision_diff = current_gran_stage_coords.getY() - getGranularStageCoords(current_stage_coords).getY();

        try
        {
            // Vertical to horizontal turn:
            if((Math.abs(prev_delta_x) != Math.abs(current_delta_x) || Math.abs(prev_delta_y) != Math.abs(current_delta_y))
            && current_delta_x == 0 && current_delta_y != 0 
            && ((stage_data[current_stage_coords.getY() - 1][current_stage_coords.getX()] == 1 && current_delta_y < 0)
            || (stage_data[current_stage_coords.getY() + 1][current_stage_coords.getX()] == 1 && current_delta_y > 0)))
            {
                setTickVelocity(current_delta_x, current_delta_y);
                current_gran_stage_coords.setCoordinates(getGranularStageCoords(current_stage_coords).getX(), 
                current_gran_stage_coords.getY(), 0);
            }// Horizontal to vertical turn:
            else if((Math.abs(prev_delta_x) != Math.abs(current_delta_x) || Math.abs(prev_delta_y) != Math.abs(current_delta_y))
            && current_delta_x != 0 && current_delta_y == 0
            && ((stage_data[current_stage_coords.getY()][current_stage_coords.getX() - 1] == 1 && current_delta_x < 0)
            || (stage_data[current_stage_coords.getY()][current_stage_coords.getX() + 1] == 1 && current_delta_x > 0)))
            {
                setTickVelocity(current_delta_x, current_delta_y);
                current_gran_stage_coords.setCoordinates(current_gran_stage_coords.getX(),  
                getGranularStageCoords(current_stage_coords).getY(), 0);
            }
            else if((Math.abs(prev_delta_x) != Math.abs(current_delta_x) || Math.abs(prev_delta_y) != Math.abs(current_delta_y)))
            {
                setTickVelocity(prev_delta_x, prev_delta_y);
            }

            // Horizontal movement: 
            if(current_stage_coords.getY() == prev_stage_coords.getY()
            && ((horizontal_collision_diff < collision_tolerance && stage_data[current_stage_coords.getY()][current_stage_coords.getX() - 1] == 0) 
            || (-horizontal_collision_diff < collision_tolerance && stage_data[current_stage_coords.getY()][current_stage_coords.getX() + 1] == 0)))
            {
                current_gran_stage_coords.setCoordinates(prev_gran_stage_coords.getX(), prev_gran_stage_coords.getY(), 0);
                current_stage_coords.setCoordinates(prev_stage_coords.getX(), prev_stage_coords.getY(), 0);
            }
            // Vertical movement: 
            else if(current_stage_coords.getX() == prev_stage_coords.getX()
            && ((vertical_collision_diff < collision_tolerance && stage_data[current_stage_coords.getY() - 1][current_stage_coords.getX()] == 0) 
            || (-vertical_collision_diff < collision_tolerance && stage_data[current_stage_coords.getY() + 1][current_stage_coords.getX()] == 0)))
            {  
                current_gran_stage_coords.setCoordinates(prev_gran_stage_coords.getX(), prev_gran_stage_coords.getY(), 0);
                current_stage_coords.setCoordinates(prev_stage_coords.getX(), prev_stage_coords.getY(), 0);
            }
        } // Teleporting from left/right side of the stage to the other: 
        catch(ArrayIndexOutOfBoundsException e)
        {
            // Moving left across stage:
            if(current_stage_coords.getX() < -2)
            {
                current_stage_coords.setCoordinates(stage_data[0].length + 1, current_stage_coords.getY(), 0);
                current_gran_stage_coords = getGranularStageCoords(current_stage_coords);
                
            } // Moving right across stage:
            else if(current_stage_coords.getX() > stage_data[0].length + 1)
            {   
                current_stage_coords.setCoordinates(-2, current_stage_coords.getY(), 0);
                current_gran_stage_coords = getGranularStageCoords(current_stage_coords);
            }
        }
    }
    
    @Override
    public void initialize() 
    {
        
    }

    @Override
    public void execute() 
    {   
        if(!is_superclass)
        {
            collisionalMovement();
            updateCoords();
            updateDirection();
        }
    }

    @Override 
    public void end(boolean interrupted) 
    {

    }

    @Override
    public boolean isFinished() 
    {
        return false;
    }
}
