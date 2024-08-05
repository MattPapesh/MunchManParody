package org.hid4java.components;

import org.hid4java.fundamentals.Constants;
import org.hid4java.fundamentals.Coordinates;
import org.hid4java.fundamentals.animation.Animation;
import org.hid4java.mechanics.movement.EntityMovement;

public class MunchMan extends EntityBase
{
    private Animation[] munch_man = {new Animation("munchman_right.png"), new Animation("munchman_left.png"),
    new Animation("munchman_down.png"), new Animation("munchman_up.png")};
    private boolean killed = false; 
    private EntityMovement movement = null;
    
    public MunchMan(int stage_x, int stage_y, int degrees) 
    {
        begin(stage_x, stage_y, degrees, munch_man);
    }

    public void appendEntityMovement(EntityMovement movement) 
    {
        this.movement = movement;
    }

    public boolean isKilled() 
    {
        return killed; 
    }

    public void kill() 
    {   
        killed = true;
        importAnimations(Constants.MUNCHMAN_DEATH);
    }

    public void reset() 
    {   
        importAnimations(munch_man);
        if(movement != null && killed) {
            killed = false; 
            Coordinates spawn = Constants.STAGE_CHARACTERISTICS.MUNCHMAN_SPAWN;
            movement.reset(spawn.getX(), spawn.getY(), spawn.getDegrees());
        }
    }
}
