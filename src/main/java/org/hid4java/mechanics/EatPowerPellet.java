package org.hid4java.mechanics;

import org.hid4java.components.MunchMan;
import org.hid4java.components.PowerPellet;
import org.hid4java.fundamentals.GameMath;
import org.hid4java.fundamentals.mechanic.MechanicBase;

public class EatPowerPellet extends MechanicBase
{
    private MunchMan munch_man = null; 
    private PowerPellet pellet = null; 
    public EatPowerPellet(MunchMan munch_man, PowerPellet pellet) 
    {
        addRequirements(munch_man, pellet);
        this.munch_man = munch_man;
        this.pellet = pellet;
    }

    public void reset() 
    {
        pellet.reset();
    }

    @Override
    public void execute() 
    {
        pellet.run();
        if(!pellet.getEaten() && GameMath.isCoordsEqual(munch_man.getStageCoords(), pellet.getStageCoords())) {
            pellet.eat();
        }
    }
}
