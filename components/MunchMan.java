package components;

import fundamentals.Coordinates;
import fundamentals.animation.Animation;

public class MunchMan extends EntityBase
{
    private Animation[] munch_man = {new Animation("munchman_right.png"), new Animation("munchman_left.png"),
    new Animation("munchman_down.png"), new Animation("munchman_up.png")};
    // Initial player coordinates in terms of the stage:  
    private Coordinates initial_stage_coords = new Coordinates(21, 15, 0);
    
    public MunchMan() 
    {
        begin(initial_stage_coords.getX(), initial_stage_coords.getY(), munch_man);
    }
}
