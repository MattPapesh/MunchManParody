package org.hid4java.components;

import org.hid4java.fundamentals.Constants;
import org.hid4java.fundamentals.Coordinates;
import org.hid4java.fundamentals.animation.Animation;
import org.hid4java.fundamentals.component.ComponentBase;

public class EntityBase extends ComponentBase 
{
    // Entity coordinates in terms of the stage:  
    private Coordinates stage_coords = new Coordinates(0, 0, 0);
    private Coordinates granular_stage_coords = new Coordinates(stage_coords.getX() * Constants.STAGE_CHARACTERISTICS.STAGE_COORD_SCALER,
    stage_coords.getY() * Constants.STAGE_CHARACTERISTICS.STAGE_COORD_SCALER, 0);
    // Entity coordinate displacement on-screen; used to determine the entity's coords at stage coords: (0, 0) on screen:
    private Coordinates displacement_coords = new Coordinates(Constants.STAGE_CHARACTERISTICS.COORD_DISPLACEMENT.getX() + 74
    - Constants.STAGE_CHARACTERISTICS.STAGE_COORD_SCALER, 
    Constants.STAGE_CHARACTERISTICS.COORD_DISPLACEMENT.getY() + 96, 0);
    
    public void begin(int stage_x, int stage_y, Animation... animations) 
    {
        stage_coords.setCoordinates(stage_x, stage_y, 0);
        addRequirements(displacement_coords.getX() + (stage_coords.getX() * Constants.STAGE_CHARACTERISTICS.STAGE_COORD_SCALER), 
        displacement_coords.getY() + (stage_coords.getY() * Constants.STAGE_CHARACTERISTICS.STAGE_COORD_SCALER), 0, animations);
    }

    // Rounds to the nearest stage coordinate based on granular stage coordinates. 
    public Coordinates convertToStageCoords(Coordinates granular_stage_coords)
    {
        return new Coordinates((int)(Math.round((double)(granular_stage_coords.getX()) / (double)Constants.STAGE_CHARACTERISTICS.STAGE_COORD_SCALER)), 
        (int)(Math.round((double)(granular_stage_coords.getY()) / (double)Constants.STAGE_CHARACTERISTICS.STAGE_COORD_SCALER)), granular_stage_coords.getDegrees());
    }

    public Coordinates convertToGranularStageCoords(Coordinates stage_coords)
    {
        return new Coordinates(stage_coords.getX() * Constants.STAGE_CHARACTERISTICS.STAGE_COORD_SCALER, 
        stage_coords.getY() * Constants.STAGE_CHARACTERISTICS.STAGE_COORD_SCALER, stage_coords.getDegrees());
    }

    public void setStageCoords(int stage_x, int stage_y, int degrees)
    {
        stage_coords.setCoordinates(stage_x, stage_y, degrees);
        granular_stage_coords.setCoordinates(granular_stage_coords.getX(), granular_stage_coords.getY(), degrees);
    }

    public void setGranularStageCoords(int granular_x, int granular_y, int degrees)
    {
        granular_stage_coords.setCoordinates(granular_x, granular_y, degrees);
        stage_coords.setCoordinates(stage_coords.getX(), stage_coords.getY(), degrees);
    }

    public Coordinates getGranularStageCoords()
    {
        return granular_stage_coords;
    }

    // Returns entity coordinates in terms of the stage.
    public Coordinates getStageCoords()
    {
        return stage_coords;
    }

    // Returns entity coordinate displacement on-screen; used to determine the entity's coords at stage coords: (0, 0) on screen.
    public Coordinates getDisplacementCoords()
    {
        return displacement_coords;
    }
}
