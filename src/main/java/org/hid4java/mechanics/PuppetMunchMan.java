package org.hid4java.mechanics;

import org.hid4java.components.MunchMan;
import org.hid4java.fundamentals.Constants;
import org.hid4java.fundamentals.mechanic.MechanicBase;

public class PuppetMunchMan extends MechanicBase 
{
    private MunchMan player = null;
    private MunchMan left_puppet = null;
    private MunchMan right_puppet = null;
    private int left_puppet_delta_x = -46 * Constants.STAGE_CHARACTERISTICS.STAGE_COORD_SCALER - 6;
    private int right_puppet_delta_x = 46 * Constants.STAGE_CHARACTERISTICS.STAGE_COORD_SCALER + 6;

    public PuppetMunchMan(MunchMan player, MunchMan left_puppet, MunchMan right_puppet)
    {
        this.player = player; 
        this.left_puppet = left_puppet;
        this.right_puppet = right_puppet;
        addRequirements(player, left_puppet, right_puppet);
    }

    @Override
    public void initialize()
    {
        left_puppet.setOpacity(1.0);
        right_puppet.setOpacity(1.0);
    }

    // Updates entity direction they are facing. (up/down/left/right)
    private void updateDirection(MunchMan munch_man, MunchMan puppet)
    {
        switch(munch_man.getStageCoords().getDegrees())
        {
            case(0): 
            {
                puppet.setAnimation(puppet.getAnimation(0).getName());
                break;
            }
            case(180):
            {
                puppet.setAnimation(puppet.getAnimation(1).getName());
                break;
            }
            case(270):
            {
                puppet.setAnimation(puppet.getAnimation(2).getName());
                break;
            }
            case(90):
            {
                puppet.setAnimation(puppet.getAnimation(3).getName());
                break;
            }
        }   
    }

    @Override
    public void execute()
    {
        int raw_x = player.getCoordinates().getX();
        int raw_y = player.getCoordinates().getY();
        int degrees = player.getCoordinates().getDegrees();
        
        left_puppet.setCoordinates(raw_x + left_puppet_delta_x, raw_y, degrees);
        right_puppet.setCoordinates(raw_x + right_puppet_delta_x, raw_y, degrees);  
        updateDirection(player, left_puppet);
        updateDirection(player, right_puppet);
    }

    @Override 
    public void end(boolean interrupted)
    {
        left_puppet.setOpacity(0.0);
        right_puppet.setOpacity(0.0);
    }
}
