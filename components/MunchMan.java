package components;

import fundamentals.Constants;
import fundamentals.Coordinates;
import fundamentals.animation.Animation;
import fundamentals.component.ComponentBase;

public class MunchMan extends ComponentBase 
{
    private Animation munch_man = new Animation("munchman.png");
    // Player coordinates in terms of the stage:  
    private Coordinates stage_coords = new Coordinates(1, 1, 0);
    private Coordinates granular_stage_coords = new Coordinates(stage_coords.getX() * Constants.STAGE_CHARACTERISTICS.STAGE_COORD_SCALER,
    stage_coords.getY() * Constants.STAGE_CHARACTERISTICS.STAGE_COORD_SCALER, 0);
    // Player coordinate displacement on-screen; used to determine the player's coords at stage coords: (0, 0) on screen:
    private Coordinates displacement_coords = new Coordinates(24, 96, 0);
    
    public MunchMan() 
    {
        addRequirements(displacement_coords.getX() + (stage_coords.getX() * Constants.STAGE_CHARACTERISTICS.STAGE_COORD_SCALER), 
        displacement_coords.getY() + (stage_coords.getY() * Constants.STAGE_CHARACTERISTICS.STAGE_COORD_SCALER), 0, munch_man);
    }

    public void setStageCoords(int stage_x, int stage_y)
    {
        stage_coords.setCoordinates(stage_x, stage_y, 0);
    }

    public void setGranularStageCoords(int granular_x, int granular_y)
    {
        granular_stage_coords.setCoordinates(granular_x, granular_y, 0);
    }

    public Coordinates getGranularStageCoords()
    {
        return granular_stage_coords;
    }

    // Returns player coordinates in terms of the stage.
    public Coordinates getStageCoords()
    {
        return stage_coords;
    }

    // Returns player coordinate displacement on-screen; used to determine the player's coords at stage coords: (0, 0) on screen.
    public Coordinates getDisplacementCoords()
    {
        return displacement_coords;
    }
}
