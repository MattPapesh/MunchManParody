package components;

import fundamentals.Coordinates;
import fundamentals.animation.Animation;

public class EnemyBase extends EntityBase
{
    // Initial enemy coordinates in terms of the stage:  
    private Coordinates initial_stage_coords = null;
    // The magnitude of delta_x & delta_y in granular stage coordinates: 
    private int speed = 0; 
    private boolean began = false;  

    public EnemyBase() {}
    public EnemyBase(int stage_x, int stage_y, int speed, Animation... enemy)
    {
        begin(initial_stage_coords.getX(), initial_stage_coords.getY(), speed, enemy);   
    }

    // Called only when EnemyBase is extended as a superclass:
    public void begin(int stage_x, int stage_y, int speed, Animation... animations)
    {
        if(!began)
        {
            began = true;
            this.speed = speed; 
            initial_stage_coords = new Coordinates(stage_x, stage_y, 0);
            setStagePosition(stage_x, stage_y);
            super.begin(stage_x, stage_y, animations);
        }
    }

    public void setStagePosition(int stage_x, int stage_y)
    {
        Coordinates gran_stage_coords = convertToGranularStageCoords(new Coordinates(stage_x, stage_y, 0));
        Coordinates raw_coords = new Coordinates(gran_stage_coords.getX() + getDisplacementCoords().getX(), 
        gran_stage_coords.getY() + getDisplacementCoords().getY(), 0);

        setStageCoords(stage_x, stage_y);
        setGranularStageCoords(gran_stage_coords.getX(), gran_stage_coords.getY());
        setCoordinates(raw_coords.getX(), raw_coords.getY(), 0);
    }

    public void moveToStagePosition(int stage_x, int stage_y)
    {

    }

    public int getSpeed()
    {
        return speed; 
    }
}
