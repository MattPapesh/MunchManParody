package mechanics;
import java.util.LinkedList;

import components.Enemy;
import components.Stage;
import components.MunchMan;
import fundamentals.mechanic.MechanicBase;

public class EnemyBehaviors extends MechanicBase
{
    
    
    public EnemyBehaviors(EntityMovement enemy_movement, Stage stage, Enemy enemy, MunchMan munch_man) 
    {


        addRequirements(stage, enemy, munch_man);
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
