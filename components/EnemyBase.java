package components;

import fundamentals.Constants;
import fundamentals.Coordinates;
import fundamentals.animation.Animation;
import fundamentals.component.ComponentBase;

public class EnemyBase extends ComponentBase
{
    private Animation enemy = new Animation("enemy.png");
    // Enemy coordinates in terms of the stage:  
    private Coordinates stage_coords = new Coordinates(6, 1, 0);
    private Coordinates granular_stage_coords = new Coordinates(stage_coords.getX() * Constants.STAGE_CHARACTERISTICS.STAGE_COORD_SCALER,
    stage_coords.getY() * Constants.STAGE_CHARACTERISTICS.STAGE_COORD_SCALER, 0);
    // Player coordinate displacement on-screen; used to determine the player's coords at stage coords: (0, 0) on screen:
    private Coordinates displacement_coords = new Coordinates(Constants.STAGE_CHARACTERISTICS.COORD_DISPLACEMENT.getX() + 74, 
    Constants.STAGE_CHARACTERISTICS.COORD_DISPLACEMENT.getY() + 96, 0);

    public EnemyBase()
    {
        addRequirements(displacement_coords.getX() + (stage_coords.getX() * Constants.STAGE_CHARACTERISTICS.STAGE_COORD_SCALER), 
        displacement_coords.getY() + (stage_coords.getY() * Constants.STAGE_CHARACTERISTICS.STAGE_COORD_SCALER), 0, enemy);
    }
}
