package mechanics;

import fundamentals.mechanic.MechanicBase;
import fundamentals.Constants;
import fundamentals.Coordinates;

import java.lang.annotation.Retention;

import javax.lang.model.util.ElementScanner6;

import components.MunchMan;
import components.Stage;

public class PlayerMovement extends MechanicBase 
{
    private MunchMan munch_man = null;
    
    private Coordinates current_stage_coords = new Coordinates(2, 1, 0);
    private Coordinates prev_stage_coords = new Coordinates(0, 0, 0); 
    private Coordinates current_gran_stage_coords = getGranularStageCoords(current_stage_coords);
    private Coordinates prev_gran_stage_coords = new Coordinates(0, 0, 0);

    private int[][] stage_data;
    private int collision_tolerance = 0; 

    private int current_delta_x = 0;
    private int current_delta_y = 0;
    private int prev_delta_x = 0;
    private int prev_delta_y = 0;

    public PlayerMovement(Stage stage, MunchMan munch_man) 
    { 
        this.munch_man = munch_man;
        this.stage_data = stage.getStageData();
        setExecutionalPeriodicDelay(20);
        addRequirements(stage, munch_man);
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
        if(current_delta_x > 0 && current_delta_y == 0)
        {
            munch_man.setAnimation("munchman_right.png");
        }
        else if(current_delta_x < 0 && current_delta_y == 0)
        {
            munch_man.setAnimation("munchman_left.png");
        }
        else if(current_delta_x == 0 && current_delta_y > 0)
        {
            munch_man.setAnimation("munchman_down.png");
        }
        else if(current_delta_x == 0 && current_delta_y < 0)
        {
            munch_man.setAnimation("munchman_up.png");
        }
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
        prev_gran_stage_coords.setCoordinates(current_gran_stage_coords.getX(), current_gran_stage_coords.getY(), 0);
        prev_stage_coords.setCoordinates(current_stage_coords.getX(), current_stage_coords.getY(), 0);
        current_gran_stage_coords.setCoordinates(current_gran_stage_coords.getX() + current_delta_x, current_gran_stage_coords.getY() + current_delta_y, 0);
        current_stage_coords = getApproximateStageCoords(current_gran_stage_coords);
    }

    private void collisionalMovement()
    {  
        int horizontal_collision_diff = current_gran_stage_coords.getX() - getGranularStageCoords(current_stage_coords).getX();
        int vertical_collision_diff = current_gran_stage_coords.getY() - getGranularStageCoords(current_stage_coords).getY();

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
    }
    
    @Override
    public void initialize() 
    {
    
    }

    @Override
    public void execute() 
    {   
        updateCoords();
        collisionalMovement();
        
        munch_man.setCoordinates(current_gran_stage_coords.getX() + munch_man.getDisplacementCoords().getX(), 
        current_gran_stage_coords.getY() + munch_man.getDisplacementCoords().getY(), 0);
        munch_man.setGranularStageCoords(current_gran_stage_coords.getX(), current_gran_stage_coords.getY());
        
        updateDirection();
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
