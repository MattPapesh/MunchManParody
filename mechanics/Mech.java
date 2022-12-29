package mechanics;

import components.MunchMan;
import components.Stage;

public class Mech extends EntityMovement 
{
    public Mech(Stage stage, MunchMan munch_man)
    {
        begin(stage, munch_man);
    }

    @Override
    public void initialize()
    {

    }

    @Override
    public void execute()
    {

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
