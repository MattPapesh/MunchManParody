package org.hid4java.components;

import org.hid4java.fundamentals.Coordinates;
import org.hid4java.fundamentals.animation.Animation;

public class MunchMan extends EntityBase
{
    private Animation[] munch_man = {new Animation("munchman_right.png"), new Animation("munchman_left.png"),
    new Animation("munchman_down.png"), new Animation("munchman_up.png")};
    // Initial player coordinates in terms of the stage:  
    private Coordinates initial_stage_coords = null;
    
    public MunchMan(int stage_x, int stage_y, int degrees) 
    {
        initial_stage_coords = new Coordinates(stage_x, stage_y, degrees);
        begin(stage_x, stage_y, degrees, munch_man);
    }
}
