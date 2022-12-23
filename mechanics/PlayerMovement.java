package mechanics;

import fundamentals.mechanic.MechanicBase;
import fundamentals.Constants;
import fundamentals.Coordinates;
import components.MunchMan;
import components.Stage;

public class PlayerMovement extends MechanicBase 
{
    private Stage stage = null;
    private MunchMan munch_man = null;

    private int[][] stage_data;
    private int delta_x = 0;
    private int delta_y = 0;

    public PlayerMovement(Stage stage, MunchMan munch_man, int delta_x, int delta_y) 
    {
        this.stage = stage; 
        this.munch_man = munch_man;
        this.delta_x = delta_x;
        this.delta_y = delta_y;
        this.stage_data = stage.getStageData();
        setExecutionalPeriodicDelay(100);
        addRequirements(stage, munch_man);
    }   
    
    @Override
    public void initialize() 
    {
        
    }

    @Override
    public void execute() 
    {   
        Coordinates stage_coords = new Coordinates(munch_man.getStageCoords().getX() + delta_x, munch_man.getStageCoords().getY() + delta_y, 0);
        munch_man.setStageCoords(stage_coords.getX(), stage_coords.getY());
        munch_man.setCoordinates((stage_coords.getX() * Constants.STAGE_CHARACTERISTICS.STAGE_COORD_SCALER) + munch_man.getDisplacementCoords().getX(), 
        (stage_coords.getY() * Constants.STAGE_CHARACTERISTICS.STAGE_COORD_SCALER) + munch_man.getDisplacementCoords().getY(), 0);

        boolean on_path = false;
        int[][] stage_data = stage.getStageData();
        if(stage_data[stage_coords.getY()][stage_coords.getX()] == 1)
        {
            on_path = true;
        }

        System.out.println("X: " + stage_coords.getX() + ",  Y: " + stage_coords.getY() + " On-Path: " + on_path);
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
